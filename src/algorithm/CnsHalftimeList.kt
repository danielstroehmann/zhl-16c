package de.stroehmi.diving.zhl16c.algorithm
/**
 * CNS toxitity reduction factor list.
 */
internal class CnsHalftimeList() {
    // key: surface interval in minutes, value: factor to reduce previous CNS
    private val lookupTable = mapOf( 30 to 0.8, 60 to 0.63, 90 to 0.5, 120 to 0.4, 150 to 0.31, 180 to 0.25, 210 to 0.2, 240 to 0.16, 270 to 0.13, 300 to 0.1, 360 to 0.06, 570 to 0.0 )

    /**
     * Calculates remaining CNS toxitity in percent before a subsequent dive.
     *
     * @param surfaceIntervalInMinute Surface interval in minutes.
     * @param cnsPercentageAfterPreviousDive CNS toxitity in percent at the end of previous dive.
     */
    fun getRemainingCnsPercentageAfterSurfaceInterval(surfaceIntervalInMinute : Int, cnsPercentageAfterPreviousDive: Double) : Double {
        val key = lookupTable.keys.find { key -> key <= surfaceIntervalInMinute } ?: lookupTable.keys.first()
        return lookupTable[key]!! * cnsPercentageAfterPreviousDive
    }
}