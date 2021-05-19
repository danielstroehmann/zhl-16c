package de.stroehmi.diving.zhl16c.algorithm.zhl16c
/**
 * List of all compartments and its halftime values for nitrogen and helium as the two major inert gases.
 *
 * @property elements Compartments
 */
internal class CompartmentHalftimeList(val elements : Array<CompartmentHalftime>) {

    companion object Factory {

        private val halftimeNitrogenList:   DoubleArray = doubleArrayOf( 4.0, 8.0, 12.5, 18.5, 27.0, 38.3, 54.3, 77.0, 109.0, 146.0, 187.0, 239.0, 305.0, 390.0, 498.0, 635.0 )
        private val halftimeHeliumList:     DoubleArray = doubleArrayOf( 1.51, 3.02, 4.72, 6.99, 10.21, 14.48, 20.53, 29.11, 41.20, 55.19, 70.69, 90.34, 115.29, 147.42, 188.24, 240.03 )
        /**
         * Checks if halftime value lists are the same sizes, sort them by ascending.
         */
        init {
            require(halftimeHeliumList.size == halftimeNitrogenList.size)
            halftimeHeliumList.sort()
            halftimeNitrogenList.sort()
        }
        /**
         * Creates a single compartment with its halftime values for helium and nitrogen.
         *
         * @param id Compartment id ranging from 0..15.
         * @return Compartment and its halftime values.
         */
        private fun createCompartment(id: Int) : CompartmentHalftime = CompartmentHalftime(halftimeHeliumList[id], halftimeNitrogenList[id], id)
        /**
         * Creates a list of all compartments based on their halftime values for nitrogen and helium.
         *
         * @return List of compartments.
         */
        fun create() = CompartmentHalftimeList(Array(halftimeHeliumList.size) { compartmentNo -> createCompartment(compartmentNo) })
    }
}