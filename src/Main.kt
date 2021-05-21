package de.stroehmi.diving.zhl16c

import de.stroehmi.diving.zhl16c.algorithm.zhl16c.Buehlmann
import de.stroehmi.diving.zhl16c.environment.*
import de.stroehmi.diving.zhl16c.plan.Dive
import de.stroehmi.diving.zhl16c.plan.Step

internal fun main() {

    // Configure breathing gases
    val gasDefault = BreathingGas.createAir()
    val gasDecompression = BreathingGas.createNitrox(oxygenInPercent = 50, type = BreathingGasType.Decompression, pO2 = 1.6)

    // Define dive plan
    val step1 = Step(depthStart = 0.0, depthEnd = 40.0, duration = 6.0, breathingGas = gasDefault)
    val step2 = Step(depthStart = 40.0, depthEnd = 40.0, duration = 14.0, breathingGas = gasDefault)

    // Configure dive
    val dive = Dive(
        water = PressureWater.Freshwater,
        altitude = 0,
        defaultBreathingGas = gasDefault,
        ascentSpeed = 6.0,
        minutesPerGasChange = 2.0,
        gfLow = 0.3,
        gfHigh = 0.8,
        name = "Parkhaus Post")

    // add gases and steps to dive plan
    dive.steps.add(step1)
    dive.steps.add(step2)
    dive.breathingGasses.add(gasDefault)
    dive.breathingGasses.add(gasDecompression)

    // Hand your dive plan to decompression planer
    val algo = Buehlmann(dive)
    // calculate your decompression steps and adding them into the dive.steps - list
    algo.calc()
    // either explore the dive.steps or print your decompression plan
    println(dive)
}