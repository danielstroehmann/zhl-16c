package de.stroehmi.diving.zhl16c.algorithm
/**
 * CNS toxitity based on partial inspiratory pressure of oxygen.
 *
 * @property partialPressureOxygen Oxygen insporatory pressure.
 * @property cnsPercentPerMinute CNS toxitity increase in percent by minute.
 * @property maximumDurationInMinutes Maximum exposure time in minutes.
 */
internal data class Cns(val partialPressureOxygen : Double, val cnsPercentPerMinute: Double, val maximumDurationInMinutes: Int)
