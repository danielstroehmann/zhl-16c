package de.stroehmi.diving.zhl16c.algorithm
/**
 * Lookup list for all OTU entries.
 *
 */
internal class OtuList() {
    /**
     * OTU List key: partial pressure of oxygen in depth, value: otu points per minute.
     */
    private val otuPerMinuteByInspiratoryPressure = mapOf(
        0.00 to 0.000,
        0.50 to 0.000,
        0.60 to 0.285,
        0.70 to 0.490,
        0.80 to 0.658,
        0.90 to 0.881,
        1.00 to 1.000,
        1.10 to 1.180,
        1.20 to 1.320,
        1.30 to 1.470,
        1.40 to 1.620,
        1.50 to 1.770,
        1.60 to 1.920,
        1.70 to 2.010,
        1.80 to 2.200,
        1.90 to 2.340,
        2.00 to 2.480,
        2.10 to 2.610,
        2.20 to 2.740,
        2.30 to 2.880,
        2.40 to 3.000,
        2.50 to 3.140
    )

    /**
     * Find best match (safe upper) OTUs per minute based on partial inspiratory pressure of oxygen in depth.
     *
     * @param partialInspiratoryPressureOxygen Partial inspiratory pressure of oxygen.
     */
    private fun find(partialInspiratoryPressureOxygen : Double) : Double {
        val key = otuPerMinuteByInspiratoryPressure.keys.find { key -> key > partialInspiratoryPressureOxygen } ?: otuPerMinuteByInspiratoryPressure.keys.last()
        return otuPerMinuteByInspiratoryPressure[key]!!
    }
    /**
     * Calculates OTU based on exporsure time and partial inspiratory pressure of oxygen.
     *
     * @param partialInspiratoryPressureOxygen Partial inspiratory pressure of oxygen.
     * @param duration Exporsure time in minutes.
     */
    fun calculateTotalOxygenToxitityUnits(partialInspiratoryPressureOxygen : Double, duration: Double) = find(partialInspiratoryPressureOxygen) * duration
}