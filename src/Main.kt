package de.stroehmi.diving.zhl16c

import de.stroehmi.diving.zhl16c.algorithm.zhl16c.Buehlmann
import de.stroehmi.diving.zhl16c.environment.*
import de.stroehmi.diving.zhl16c.plan.Dive
import de.stroehmi.diving.zhl16c.plan.Step

internal fun main() {

    // Configure breathing gases
    val gasDefault = BreathingGas.createAir()
    val gasDecompression = BreathingGas.createNitrox(50, BreathingGasType.Decompression, pO2 = 1.6)

    // Define dive plan
    val step1 = Step(0.0, 40.0, 4.0, gasDefault)
    val step2 = Step(40.0,40.0, 20.0, gasDefault)

    // Configure dive
    val dive = Dive(PressureWater.Freshwater, 0, gasDefault, 6.0, 2.0, 0.3, 0.8, name = "Parkhaus Post")

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