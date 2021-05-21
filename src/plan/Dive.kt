package de.stroehmi.diving.zhl16c.plan

import de.stroehmi.diving.zhl16c.environment.PressureAir
import de.stroehmi.diving.zhl16c.environment.BreathingGas
import de.stroehmi.diving.zhl16c.environment.BreathingGasList
import de.stroehmi.diving.zhl16c.environment.PressureWater
/**
 * Represents the enviroment and steps of a dive and the behaviour of a diver.
 *
 * @property water Type of water.
 * @property ascentSpeed Speed in m/s for a diver to ascent.
 * @property gfLow Lower gradient factor.
 * @property gfHigh Higher gradient factor.
 * @property name Name of dive.
 * @constructor
 * Creates a new dive.
 *
 * @param altitude Altitude above sea level in metres for dive site.
 * @param defaultBreathingGas Breathing gas.
 */
class Dive(val water: PressureWater, altitude: Int, defaultBreathingGas : BreathingGas = BreathingGas.createAir(), val ascentSpeed : Double = 9.0, val minutesPerGasChange : Double = 2.0, val gfLow: Double, val gfHigh: Double, var name : String = "Dive") {

    internal val air        = PressureAir(altitude)
    val breathingGasses     = BreathingGasList(defaultBreathingGas)
    val steps               = mutableListOf<Step>()
    var cnsTotal : Double   = 0.0
    var otuTotal : Double   = 0.0

    init {
        require(gfLow in 0.0..1.0) { "Low GF must be between 0.0 and 1.0" }
        require(gfHigh in gfLow..1.0) { "High GF must be between $gfLow (current low GF) and 1.0" }
    }

    override fun toString(): String {
        var format =    "NAME: $name\n" +
                        "\n" +
                        "OTU: ${"%.2f".format(otuTotal)}\n" +
                        "CNS: ${"%.2f".format(cnsTotal)}%\n\n" +
                        "GASES:\n\n"

        for(gas in breathingGasses.elements) format += "${gas}\n"

        format +=   "\n" +
                    "STEPS:\n\n"
        for(step in steps) format += "${step}\n"

        return format
    }


}