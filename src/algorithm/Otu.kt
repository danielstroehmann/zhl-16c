package de.stroehmi.diving.zhl16c.algorithm
/**
 * Oxygen toxitity units.
 *
 * @property partialInspiratoryPressureOxygen Partial inspiratory pressure of oxygen.
 * @property oxygenToxitityUnitsPerMinute Units per minute for given oxygen inspiratory pressure.
 */
internal data class Otu(val partialInspiratoryPressureOxygen : Double, val oxygenToxitityUnitsPerMinute : Double)