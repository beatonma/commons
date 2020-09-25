package org.beatonma.commons.core.extensions

import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.roundToInt

fun Float.map(fromMin: Float, fromMax: Float, toMin: Float, toMax: Float): Float =
    normalizeIn(fromMin, fromMax)
        .mapTo(toMin, toMax)

fun Float.safeMap(fromLimit1: Float, fromLimit2: Float, toMin: Float, toMax: Float): Float = map(
    min(fromLimit1, fromLimit2),
    max(fromLimit1, fromLimit2),
    toMin, toMax
)

/**
 * Receiver value is assumed to be between 0F..1F!
 */
fun Float.mapTo(min: Int, max: Int, forcePositive: Boolean = true): Int {
    val range: Float = (max - min).toFloat()
    val self = if (forcePositive) this.coerceIn(0F, 1F) else this
    return (min + (self * range)).roundToInt()
        .coerceIn(min, max)
}

/**
 * Receiver value is assumed to be between 0F..1F!
 */
fun Float.mapTo(range: IntRange): Int = mapTo(range.first, range.last)

/**
 * Receiver value is assumed to be between 0F..1F!
 */
fun Float.mapTo(min: Float, max: Float): Float {
    val range: Float = max - min
    return (min + (this * range))
        .coerceIn(min, max)
}

/**
 * Convenience for [mapTo] where min == 0F
 */
fun Float.mapTo(max: Float): Float = mapTo(0F, max)

/**
 * Map to a value between 0F..1F relative to the given limits.
 */
fun Float.normalizeIn(min: Float, max: Float): Float {
    val range = max - min
    return ((this - min) / range)
        .coerceIn(0F, 1F)
}

/**
 * Convenience for [normalizeIn] where min == 0F
 */
fun Float.normalize(max: Float): Float = normalizeIn(0F, max)

/**
 * Receiver value is assumed to be between 0F..1F!
 */
fun Float.mapToByte(): Int = mapTo(0, 255)

fun Int.normalizeIn(min: Int, max: Int): Float = toFloat().normalizeIn(min.toFloat(), max.toFloat())
