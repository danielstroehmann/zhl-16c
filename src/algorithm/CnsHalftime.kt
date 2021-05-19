package de.stroehmi.diving.zhl16c.algorithm
/**
 * CNS toxitity reduction factor based on surface interval between dives in minutes.
 *
 * @property surfaceIntervalInMinutes Surface interval in minutes.
 * @property reductionFactor Reduction factor.
 */
internal data class CnsHalftime(val surfaceIntervalInMinutes: Int, val reductionFactor: Double)