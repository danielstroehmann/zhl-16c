package de.stroehmi.diving.zhl16c.environment
import kotlin.math.pow

/**
 * Represents air pressure in certain altitude above zero.
 *
 * @constructor Sets hydrostatic pressure using intl. altitude formula.
 *
 * @param altitude Altitude above zero in metres.
 */
internal class PressureAir(altitude: Int) {
    // international altitude formula
    val pressure = 1013.25 * (1 - (6.5 * altitude) / 288150).pow(5.255) / 1000
}