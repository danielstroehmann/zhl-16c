package de.stroehmi.diving.zhl16c.algorithm
/**
 * List of all CNS toxitity levels.
 */
internal class CnsList {

    /**
     * Key: partial pressure oxygen in depth
     * Pair.first: cns toxitity per minute in %
     * Pair.second: max. exposure time in min
     */
    @Suppress("PrivatePropertyName")
    private val NOAAtable = mapOf(
        0.50 to Pair(0.000, -1), 0.60 to Pair(0.140, 714), 0.64 to Pair(0.150, 667), 0.66 to Pair(0.160, 625), 0.68 to Pair(0.170, 588), 0.70 to Pair(0.180, 556), 0.74 to Pair(0.190, 526), 0.76 to Pair(0.200, 500), 0.78 to Pair(0.210, 476),
        0.80 to Pair(0.220, 455), 0.82 to Pair(0.230, 435), 0.84 to Pair(0.240, 417), 0.86 to Pair(0.250, 400), 0.88 to Pair(0.260, 385), 0.90 to Pair(0.280, 357), 0.92 to Pair(0.290, 345), 0.94 to Pair(0.300, 333), 0.96 to Pair(0.310, 323),
        0.98 to Pair(0.320, 313), 1.00 to Pair(0.330, 303), 1.02 to Pair(0.350, 286), 1.04 to Pair(0.360, 278), 1.06 to Pair(0.380, 263), 1.08 to Pair(0.400, 250), 1.10 to Pair(0.420, 238), 1.12 to Pair(0.430, 233), 1.14 to Pair(0.430, 233),
        1.16 to Pair(0.440, 227), 1.18 to Pair(0.460, 217), 1.20 to Pair(0.470, 213), 1.22 to Pair(0.480, 208), 1.24 to Pair(0.510, 196), 1.26 to Pair(0.520, 192), 1.28 to Pair(0.540, 185), 1.30 to Pair(0.560, 179), 1.32 to Pair(0.570, 175),
        1.34 to Pair(0.600, 167), 1.36 to Pair(0.620, 161), 1.38 to Pair(0.630, 159), 1.40 to Pair(0.650, 154), 1.42 to Pair(0.680, 147), 1.44 to Pair(0.710, 141), 1.46 to Pair(0.740, 135), 1.48 to Pair(0.780, 128), 1.50 to Pair(0.830, 120),
        1.52 to Pair(0.930, 108), 1.54 to Pair(1.040, 96), 1.56 to Pair(1.190, 84), 1.58 to Pair(1.470, 68), 1.60 to Pair(2.220, 45), 1.62 to Pair(5.000, 20), 1.65 to Pair(6.250, 16), 1.67 to Pair(7.690, 13), 1.70 to Pair(10.00, 10),
        1.72 to Pair(12.50, 8), 1.74 to Pair(20.00, 5), 1.77 to Pair(25.00, 4), 1.79 to Pair(31.25, 3), 1.80 to Pair(50.00, 2), 1.82 to Pair(100.0, 1)
    )
    /**
     * Finds best match (safe upper) CNS toxitity level based on partial inspiratory pressure of oxygen.
     *
     * @param partialInspiratoryPressureOxygen Partial inspiratory pressure of oxygen.
     */
    private fun find(partialInspiratoryPressureOxygen: Double) : Double {
        val key = NOAAtable.keys.find { elem -> elem > partialInspiratoryPressureOxygen } ?: NOAAtable.keys.last()
        return NOAAtable[key]!!.first
    }
    /**
     * Calculates CNS toxitiy percentage.
     *
     * @param partialInspiratoryPressureOxygen Partial inspiratory pressure of oxygen.
     * @param duration Exposure time in minutes.
     */
    fun calculateCnsPercentage(partialInspiratoryPressureOxygen : Double, duration: Double) = find(partialInspiratoryPressureOxygen) * duration
}