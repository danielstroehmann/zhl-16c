package de.stroehmi.diving.zhl16c.algorithm
/**
 * Lookup list for all OTU entries.
 *
 * @property elements OTUs.
 */
internal class OtuList(private val elements : ArrayList<Otu> = ArrayList()) {
    /**
     * Find best match (safe upper) OTUs per minute based on partial inspiratory pressure of oxygen in depth.
     *
     * @param partialInspiratoryPressureOxygen Partial inspiratory pressure of oxygen.
     */
    private fun find(partialInspiratoryPressureOxygen : Double) = elements.find { elem -> elem.partialInspiratoryPressureOxygen > partialInspiratoryPressureOxygen } ?: elements[elements.lastIndex]
    /**
     * Calculates OTU based on exporsure time and partial inspiratory pressure of oxygen.
     *
     * @param partialInspiratoryPressureOxygen Partial inspiratory pressure of oxygen.
     * @param duration Exporsure time in minutes.
     */
    fun calculateTotalOxygenToxitityUnits(partialInspiratoryPressureOxygen : Double, duration: Double) = find(partialInspiratoryPressureOxygen = partialInspiratoryPressureOxygen).oxygenToxitityUnitsPerMinute * duration

    companion object Factory {
        private val partialPressureOxygen: ArrayList<Double> = arrayListOf(
            2.50,
            2.40,
            2.30,
            2.20,
            2.10,
            2.00,
            1.90,
            1.80,
            1.70,
            1.60,
            1.50,
            1.40,
            1.30,
            1.20,
            1.10,
            1.00,
            0.90,
            0.80,
            0.70,
            0.60,
            0.50,
            0.00
        )
        private val toxitityUnitsPerMinute: ArrayList<Double> = arrayListOf(
            3.140,
            3.000,
            2.880,
            2.740,
            2.610,
            2.480,
            2.340,
            2.200,
            2.010,
            1.920,
            1.770,
            1.620,
            1.470,
            1.320,
            1.180,
            1.000,
            0.881,
            0.658,
            0.490,
            0.285,
            0.000,
            0.000
        )
        /**
         * Creates a lookup table for all OTUs.
         *
         * @return List of all OTU entries.
         */
        fun create() : OtuList {
            toxitityUnitsPerMinute.sort()
            partialPressureOxygen.sort()
            val list : ArrayList<Otu> = ArrayList()
            for(i in 0..partialPressureOxygen.lastIndex) list.add(Otu(partialInspiratoryPressureOxygen = partialPressureOxygen[i], oxygenToxitityUnitsPerMinute = toxitityUnitsPerMinute[i]))
            return OtuList(list)
        }
    }
}