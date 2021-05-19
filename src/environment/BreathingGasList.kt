package de.stroehmi.diving.zhl16c.environment
/**
 * List with breathing gases for a dive.
 *
 * @constructor
 * Creates a list for breathing gases.
 *
 * @param defaultBreathingGas Gas to start with.
 */
class BreathingGasList(defaultBreathingGas : BreathingGas) {
    /// List of breathing gases.
    val elements = ArrayList<BreathingGas>()

    init {
        elements.add(defaultBreathingGas)
    }
    /**
     * Add new gas for dive.
     *
     * @param breathingGas New gas.
     */
    fun add(breathingGas : BreathingGas) {
        if(elements.find { e-> e == breathingGas } == null) elements.add(breathingGas)
    }
    /**
     * Remove gas from current breathing gases carried by diver.
     *
     * @param breathingGas
     */
    fun remove(breathingGas: BreathingGas) {
        elements.remove(breathingGas)
    }
    /**
     * Selects the gas with the highest oxygen content for current depth and is marked as decompression gas.
     * Highest oxygen count always wins. If gas contains the same amount of oxygen but also helium this gas will be selected.
     *
     * @param currentDepth Current depth of diver below water line.
     * @param ambientPressure Ambient pressure during dive determined by altitude and water type.
     * @return Best gas for current depth.
     */
    internal fun getBestDecompressionGas(currentDepth : Double, ambientPressure : PressureAmbient) : BreathingGas? {
        // Get all gases that are available for decompression based on MOD
        val modGas  = elements.filter { gas -> gas.getMOD(ambientPressure) >= currentDepth && gas.type == BreathingGasType.Decompression}
        return if(modGas.isEmpty()) null
        else {
            var bestMix = modGas[0]
            for (gas in modGas) if ((gas.oxygen > bestMix.oxygen) || (gas.oxygen == bestMix.oxygen && gas.helium > bestMix.helium)) bestMix = gas
            bestMix
        }
    }
    /**
     * Calculates the next stop for gas change based on gas carried by diver and marked for decompression.
     *
     * @param currentDepth Current depth of diver.
     * @param pressureAmbient Ambient pressure.
     * @return
     */
    internal fun getDepthForNextDecoGasChange(currentDepth: Double, pressureAmbient: PressureAmbient) : Double {
        val stopps = ArrayList<Double>()
        val decoGases = elements.filter { gas -> gas.getMOD(pressureAmbient) < currentDepth && gas.type == BreathingGasType.Decompression }
        return if(decoGases.isNotEmpty()) {
            for(gas in decoGases) stopps.add(gas.getMOD(pressureAmbient))
            stopps.sort()
            stopps.last()
        } else return -1.0
    }
}