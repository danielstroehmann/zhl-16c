package de.stroehmi.diving.zhl16c.algorithm
/**
 * CNS toxitity reduction factor list.
 *
 * @property elements Reduction factors by minutes on surface after dive.
 */
internal class CnsHalftimeList(private val elements : Array<CnsHalftime>) {
    /**
     * Calculates remaining CNS toxitity in percent before a subsequent dive.
     *
     * @param surfaceIntervalInMinute Surface interval in minutes.
     * @param cnsPercentageAfterPreviousDive CNS toxitity in percent at the end of previous dive.
     */
    fun getRemainingCnsPercentageAfterSurfaceInterval(surfaceIntervalInMinute : Int, cnsPercentageAfterPreviousDive: Double) =
        (elements.find { elem -> elem.surfaceIntervalInMinutes <= surfaceIntervalInMinute }?: elements[0]).reductionFactor * cnsPercentageAfterPreviousDive

    companion object Factory {
        private val surfaceIntervalInMinutes = arrayListOf<Int>(
            30,
            60,
            90,
            120,
            150,
            180,
            210,
            240,
            270,
            300,
            360,
            570
        )
        private val reductionFactor = arrayListOf<Double>(
            0.8,
            0.63,
            0.5,
            0.4,
            0.31,
            0.25,
            0.2,
            0.16,
            0.13,
            0.1,
            0.06,
            0.0
        )

        init {
            require(surfaceIntervalInMinutes.size == reductionFactor.size)
            reductionFactor.sort()
            surfaceIntervalInMinutes.sort()
        }
        /**
         * Creates a list of all CNS toxitity reduction factors.
         *
         */
        fun create() = CnsHalftimeList(Array(surfaceIntervalInMinutes.size) { no -> CnsHalftime(surfaceIntervalInMinutes[no], reductionFactor[no]) })
    }
}