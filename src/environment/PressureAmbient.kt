package de.stroehmi.diving.zhl16c.environment
/**
 * Calculates ambient pressure based on altitude and water type.
 *
 * @property air Pressure of air in diving altitude.
 * @property water Water type.
 */
internal class PressureAmbient(private val air: PressureAir, private val water: PressureWater) {
    /**
     * Calculates a given pressure into metres below water line.
     *
     * @param totalAmbientPressure Total ambient pressure in depth.
     * @return Metres of depth for given ambient pressure.
     */
    fun getDepth(totalAmbientPressure: Double) : Double {
        val result = (totalAmbientPressure - air.pressure) * 10
        return if(result < 0.0) 0.0 else result
    }
    /**
     * Calculates the ambient pressure for altitude and water type of given depth.
     *
     * @param depth Depth below water line in metres.
     */
    fun getAmbientPressure(depth: Double) = air.pressure + water.pressure * (depth / 10)
}