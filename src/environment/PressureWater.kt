package de.stroehmi.diving.zhl16c.environment
/**
 * Types of water
 *
 * @property pressure
 */
enum class PressureWater(val pressure: Double) {
    Freshwater(1.00),
    Saltwater(1.03)
}