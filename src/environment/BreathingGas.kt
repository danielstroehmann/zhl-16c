package de.stroehmi.diving.zhl16c.environment

import kotlin.math.roundToInt

/**
 * Represents a gas mix for breathing.
 *
 * @property pO2 Maximum tolerated partial pressure oxygen (MOD).
 * @property name Name of mix.
 * @property type Type of usage (e.g. bottom mix or decompression).
 * @constructor Generates a new mix.
 *
 * @param oxygen Percent Oxygen e.g. 21 for air.
 * @param nitrogen Percent Nitrogen e.g. 79 for air.
 * @param helium Percent Helium e.g. 0 for air.
 */
@Suppress("EqualsOrHashCode")
class BreathingGas(oxygen: Int = 21, nitrogen: Int = 100 - oxygen, helium: Int = 100 - oxygen - nitrogen, val pO2: Double, val type: BreathingGasType = BreathingGasType.Bottom, val name : String) {

    val oxygen : Double
    val helium : Double
    val nitrogen : Double
    val inertGasFactorNitrogen : Double
    val inertGasFactorHelium : Double

    init {
        require(oxygen + nitrogen + helium == 100) { "Oxygen, nitrogen and helium must be a total of 100% instead of ${oxygen + nitrogen + helium}%"}
        this.oxygen                 = oxygen.toDouble() / 100.0
        this.helium                 = helium.toDouble() / 100.0
        this.nitrogen               = nitrogen.toDouble() / 100.0
        this.inertGasFactorHelium   = if(helium == 0) 0.0000000000000001 else this.helium / (this.helium + this.nitrogen)
        this.inertGasFactorNitrogen = if(nitrogen == 0) 0.0000000000000001 else this.nitrogen / (this.helium + this.nitrogen)
    }

    companion object Factory {
        /**
         * Creates air.
         *
         * @return Air.
         */
        fun createAir() : BreathingGas = BreathingGas(21, 79, 0, name = "AIR", type = BreathingGasType.Bottom, pO2 = 1.4)
        /**
         * Creates a Nitrox mix.
         *
         * @param oxygen Percent oxygen.
         * @return EANxx.
         */
        fun createNitrox(oxygen : Int, type: BreathingGasType, pO2 : Double) : BreathingGas {
            require(oxygen in 1..100) { "Oxygen content must be greater than 0% and maximum 100%" }
            return BreathingGas(oxygen, type = type, name = "EAN$oxygen", pO2 = pO2)
        }
        /**
         * Creates Trimix.
         *
         * @param oxygen Percent of oxygen.
         * @param helium Percent of helium.
         * @return Trimix xx/xx.
         */
        fun createTrimix(oxygen: Int, helium: Int, type: BreathingGasType, pO2: Double) : BreathingGas {
            require(oxygen in 1..100 && helium in 1..100 && (oxygen + helium) < 100) { "Sum of Oxygen and Nitrogen must be less than 100% and each must be greater than 0%" }
            return BreathingGas(oxygen = oxygen, helium = helium, nitrogen = 100 - helium - oxygen, type = type, name = "TX${oxygen}/${helium}", pO2 = pO2)
        }
    }
    /**
     * Gets maximum operation depth for gas mix.
     *
     * @param pressure Ambient pressure based on altitude and water type.
     * @return Maximum operational depth for mix.
     */
    internal fun getMOD(pressure: PressureAmbient) = pressure.getDepth(pO2 / oxygen).toInt().toDouble()

    override fun toString() = "${name.padEnd(5, ' ')} - Type: $type" // (o2: $oxygen, n2: $nitrogen, he: $helium)"

    override fun equals(other: Any?) = if (other is BreathingGas) other.name == this.name else false
}