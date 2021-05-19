package de.stroehmi.diving.zhl16c.algorithm.zhl16c
import de.stroehmi.diving.zhl16c.algorithm.CnsList
import de.stroehmi.diving.zhl16c.algorithm.OtuList
import de.stroehmi.diving.zhl16c.environment.PressureAmbient
import de.stroehmi.diving.zhl16c.plan.Dive
import de.stroehmi.diving.zhl16c.plan.Step
import kotlin.math.pow
/**
 * Calculates deco stops for dive based on gradient factor slope, compartment saturation and breathing gas.
 *
 * @property dive
 */
@Suppress("JoinDeclarationAndAssignment")
class Buehlmann(private val dive : Dive) {
    //  Aaturation vapor pressure of water at T=37°C
    private val psH2O       = 0.063
    // List of default 16 compartments defined in ZHL16-C
    private val halftime    = CompartmentHalftimeList.create()
    // Ambient pressure based on type of water and altitude
    private val pressure    = PressureAmbient(dive.air, dive.water)
    // Lookup table for otu values
    private val otuList                 = OtuList.create()
    // Lookup table for cns toxitity
    private val cnsList             = CnsList.create()
    // default partial pressure of nitrogen on surface
    private val defaultPartialPressureNitrogenInTissue = 0.75

    /**
     * Calculates saturation for planned steps followed by the decompression and gas changing steps.
     *
     */
    fun calc() {
        for(idx in 0..dive.steps.lastIndex) {
            if(idx >= 1) calculateStep(dive.steps[idx], dive.steps[idx-1], dive.gfLow)
            else calculateStep(dive.steps[idx], null, dive.gfLow)
        }

        calculateDecompression()

        for(step in dive.steps) {
            val pressureAmbient             = pressure.getAmbientPressure(step.depth)
            val pressureInspiratoryOxygen   = calculateInspiratoryPartialPressureGas(pressureAmbient, step.breathingGas.oxygen)
            dive.cnsTotal += cnsList.calculateCnsPercentage(pressureInspiratoryOxygen, step.duration)
            dive.otuTotal += otuList.calculateTotalOxygenToxitityUnits(pressureInspiratoryOxygen, step.duration)
        }
    }
    /**
     * Calculates saturation of current step based on data from last step.
     *
     * @param currentStep Current step.
     * @param previousStep previous step.
     * @param gradientFactor Current gradient factor for depth.
     */
    private fun calculateStep(currentStep : Step, previousStep : Step?, gradientFactor: Double)  {

        val pressureAmbient             = pressure.getAmbientPressure(currentStep.depth)
        val pressureInspiratoryHelium   = calculateInspiratoryPartialPressureGas(pressureAmbient, currentStep.breathingGas.helium)
        val pressureInspiratoryNitrogen = calculateInspiratoryPartialPressureGas(pressureAmbient, currentStep.breathingGas.nitrogen)
        val compartments : ArrayList<Compartment> = ArrayList()

        for(tissue in halftime.elements) {
            val curPresHelium    = if(previousStep is Step) previousStep.saturation.find { e -> e.id == tissue.id }!!.partialPressureInertHeliumInTissue else 0.0
            val curPresNitrogen  = if(previousStep is Step) previousStep.saturation.find { e -> e.id == tissue.id }!!.partialPressureInertNitrogenInTissue else defaultPartialPressureNitrogenInTissue
            val newPresHelium    = calculatePressureInertGasInTissue(curPresHelium, pressureInspiratoryHelium, tissue.helium, currentStep.duration)
            val newPresNitrogen  = calculatePressureInertGasInTissue(curPresNitrogen, pressureInspiratoryNitrogen, tissue.nitrogen, currentStep.duration)
            val partPresInertTot = newPresHelium + newPresNitrogen
            val presAmbTolTot    = calculatePressureAmbientTolerated(partPresInertTot, tissue.a(currentStep.breathingGas, gradientFactor), tissue.b(currentStep.breathingGas, gradientFactor))
            val ceiling          = pressure.getDepth(presAmbTolTot)
            compartments.add(Compartment(tissue.id, newPresHelium, newPresNitrogen, partPresInertTot, presAmbTolTot, ceiling))

        }

        currentStep.saturation = compartments
    }

    private fun calculateDecompression() {

        var stepLast             = dive.steps.last()
        val metresFirstDecoStopp = calculateNextDecompressionStop(stepLast.saturation)
        val slope                = (dive.gfHigh - dive.gfLow) / (0 - metresFirstDecoStopp)

        while(stepLast.depthEnd != 0.0) {
            val bestGas             = dive.breathingGasses.getBestDecompressionGas(stepLast.depthEnd, pressure)?: stepLast.breathingGas
            val isGasChangeStopp    = bestGas != stepLast.breathingGas
            val metresNextGasChange = dive.breathingGasses.getDepthForNextDecoGasChange(stepLast.depthEnd, pressure)
            var metresNextDecoStopp = calculateNextDecompressionStop(stepLast.saturation)
            val gfCurrent           = if(stepLast.depthEnd > metresFirstDecoStopp) dive.gfLow else slope * stepLast.depthEnd + dive.gfHigh

            // if diver was sent to this stop to change gas
            if(isGasChangeStopp) {
                val stepNew = Step(stepLast.depthEnd, stepLast.depthEnd, dive.minutesPerGasChange, bestGas)
                dive.steps.add(stepNew)
                calculateStep(stepNew, stepLast, gfCurrent)
                stepLast = stepNew
                continue
            }

            // if a diver reached a stop and needs to stay there for a certrain time
            if(metresNextDecoStopp == stepLast.depthEnd) {
                val intervalStep = 0.06
                var intervalCurrent = 0.0
                var stepSimulation : Step

                do {
                    intervalCurrent += intervalStep
                    stepSimulation  = Step(stepLast.depthEnd, stepLast.depthEnd, intervalCurrent, stepLast.breathingGas)
                    calculateStep(stepSimulation, stepLast, gfCurrent)
                    metresNextDecoStopp = calculateNextDecompressionStop(stepSimulation.saturation)
                } while(stepSimulation.depthEnd == metresNextDecoStopp)

                dive.steps.add(stepSimulation)
                stepLast = stepSimulation
                continue
            }

            // if a diver reached a stop and needs to ascent to the next stop
            val nextDepth   = if(metresNextDecoStopp < metresNextGasChange) metresNextGasChange else metresNextDecoStopp
            val duration    = (((stepLast.depthEnd - nextDepth) / dive.ascentSpeed) .toInt() + 1).toDouble()
            val stepNext    = Step(stepLast.depthEnd, nextDepth, duration, stepLast.breathingGas)
            calculateStep(stepNext, stepLast, gfCurrent)
            dive.steps.add(stepNext)
            stepLast = stepNext
            continue
        }
    }
    /**
     * Determines the next save decostopp based on the concept of "every 3 metres"
     *
     * @param saturation Current Saturation of leading tissue compartment.
     * @return Next decompression stop in metres.
     */
    private fun calculateNextDecompressionStop(saturation : ArrayList<Compartment>) : Double {
        var currentCeiling = 0.0
        var minimumCeiling : Int
        saturation.forEach { e -> if(e.ceiling > currentCeiling) currentCeiling = e.ceiling }
        return if(currentCeiling == 0.0) 0.0
        else {
            minimumCeiling = currentCeiling.toInt() + 1
            while(minimumCeiling % 3.0 != 0.0) minimumCeiling++
            minimumCeiling.toDouble()
        }
    }
    /**
     * Calculates pressure of inspirated inert gas.
     *
     * @param pressureAmbient Total ambient pressure in current depth.
     * @param partialPressureGas Partial pressure of inert gas on surface level (e.g. nitrogen 0.79 of 1 bar air).
     * @return Pressure of inspirated inert gas.
     */
    private fun calculateInspiratoryPartialPressureGas(pressureAmbient : Double, partialPressureGas : Double) = (pressureAmbient - psH2O) * partialPressureGas
    /**
     * Calculates inert gas partial pressure in tissue after exposure.
     *
     * @param curPresInertGasTissue Current inert gas partial pressure in tissue before the step.
     * @param presInspInertGas Pressure of inspirated inert gas.
     * @param halftimeTissue Haftime of gas in tissue.
     * @param duration Exposure time in minutes.
     * @return Inert gas partial pressure in tissue after exposure.
     */
    private fun calculatePressureInertGasInTissue(curPresInertGasTissue: Double, presInspInertGas: Double, halftimeTissue: Double, duration: Double): Double {
        return curPresInertGasTissue + (presInspInertGas - curPresInertGasTissue) * (1.0 - 2.0.pow((duration * -1) / halftimeTissue))
    }
    /**
     * Calculates maximum tolerated ambient pressure for tissue at current grade of saturation.
     *
     * @param pressureInertGasInTissue Total partial pressure of all inert gases in tissue.
     * @param a Factor A
     * @param b Factor B
     * @return Maximum tolerated ambient pressure for tissue at current grade of saturation.
     */
    private fun calculatePressureAmbientTolerated(pressureInertGasInTissue : Double, a : Double, b: Double) : Double {
        val result = (pressureInertGasInTissue - a) * b
        return if(result < 0.0) 0.0 else result
    }
}