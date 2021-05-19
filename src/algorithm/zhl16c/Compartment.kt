package de.stroehmi.diving.zhl16c.algorithm.zhl16c
/**
 * Representations the partial pressure of inert gases in one of the 16 tissue compartments.
 *
 * @property id Compartment range from 0..15.
 * @property partialPressureInertHeliumInTissue Partial pressure of helium in given compartment tissue.
 * @property partialPressureInertNitrogenInTissue Partial pressure of nitrogen in given compartment tissue.
 * @property partialPressureInertTotalInTissue Total partial pressure in tissue of all inert gases.
 * @property pressureAmbientToleratedTotal Maximum tolerated ambient pressure of the compartment tissue.
 * @property ceiling Maximum tolerated ambient pressure translated into ceiling level based on water type and altitude.
 */
data class Compartment(
    val id: Int, val partialPressureInertHeliumInTissue : Double,
    val partialPressureInertNitrogenInTissue : Double,
    val partialPressureInertTotalInTissue : Double,
    val pressureAmbientToleratedTotal : Double,
    val ceiling : Double) {

    override fun toString(): String {
        return  "No. ${id.toString().padStart(2, '0')} -\t" +
                "ppHe: ${"%.2f".format(partialPressureInertHeliumInTissue)}\t" +
                "ppN2: ${"%.2f".format(partialPressureInertNitrogenInTissue)}\t" +
                "pTot: ${"%.2f".format(partialPressureInertTotalInTissue)}\t" +
                "pAmbTolTot: ${"%.2f".format(pressureAmbientToleratedTotal)}\t" +
                "ceiling: ${"%.2f".format(ceiling)}"
    }
}