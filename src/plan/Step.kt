package de.stroehmi.diving.zhl16c.plan
import de.stroehmi.diving.zhl16c.algorithm.zhl16c.Compartment
import de.stroehmi.diving.zhl16c.environment.BreathingGas

/**
 * Parameter of a dive plan.
 * @param depthStart Depth at start (is end of previous step or 0 at the beginning).
 * @param depthEnd Depth at the end of the step.
 * @param duration travel time for this step needed to calculate exposure.
 * @param breathingGas Gas used during the step.
 */
class Step(val depthStart : Double, val depthEnd : Double, val duration: Double, val breathingGas: BreathingGas) {

    // if direction is up the average depth is used for calculating the saturation. if down or level than max depth is used as safety measure
    private val direction : Direction
    // average depth used for calculation of saturation limits
    val depth : Double
    // saturation levels for tissue compartments
    var saturation : ArrayList<Compartment> = ArrayList()

    init {

        require(depthStart >= 0 && depthEnd >= 0)

        direction = when {
            depthStart < depthEnd -> Direction.Down
            depthStart > depthEnd -> Direction.Up
            else -> Direction.Level
        }

        depth = when (direction) {
            Direction.Up -> depthStart - (depthStart - depthEnd) / 2.0
            else -> depthEnd
        }

    }

    override fun toString(): String {

        val pattern = ".*\\.0$".toRegex()
        val formatDuration = if(pattern.matches(duration.toString())) duration.toString() else (duration.toInt() + 1).toDouble().toString()

        return "Starting at " +
                "${depthStart.toString().padStart(4, ' ')}m to " +
                "${depthEnd.toString().padStart(4, ' ')}m " +
                // "(avg. ${depth.toString().padStart(5, ' ')}m) for " +
                "for ${formatDuration.padStart(4, ' ')}min, direction: $direction\n" +
                "Gas: ${breathingGas.toString().padStart(6, ' ')}\n"
    }
}