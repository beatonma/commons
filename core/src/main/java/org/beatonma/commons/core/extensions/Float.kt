package org.beatonma.commons.core.extensions

import androidx.annotation.FloatRange
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.roundToInt

/**
 * Calculate position relative to toMin..toMax based on relative position in range fromMin..fromMax.
 */
fun Float.map(fromMin: Float, fromMax: Float, toMin: Float, toMax: Float): Float =
    normalizeIn(fromMin, fromMax)
        .mapTo(toMin, toMax)

/**
 * Same as [map], ensuring fromMin and fromMax are in the correct order.
 */
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
 * Map to a value between 0F..1F relative to the given limits.
 */
fun Int.normalizeIn(min: Int, max: Int): Float = toFloat().normalizeIn(min.toFloat(), max.toFloat())

/**
 * Receiver value is assumed to be between 0F..1F!
 */
fun Float.mapToByte(): Int = mapTo(0, 255)

fun Float.lerp(end: Float, progress: Float): Float =
    this + ((end - this) * progress)

fun Float.progressIn(min: Float = 0F, max: Float): Float = coerceIn(min, max).normalizeIn(min, max)

fun Int.lerp(end: Int, progress: Float): Int = this + ((end - this) * progress).roundToInt()

/**
 * Reverse direction of a value between 0..1, useful for animations.
 */
@FloatRange(from = 0.0, to = 1.0)
fun Float.reverse() = 1F - this
