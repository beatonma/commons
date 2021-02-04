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
 * Receiver value is assumed to be between 0F..1F!
 */
fun Float.mapToByte(): Int = mapTo(0, 255)

fun Float.lerpTo(end: Float, progress: Float): Float =
    this + ((end - this) * progress)

fun Float.lerpBetween(start: Float, end: Float): Float = start.lerpTo(end, progress = this)

/**
 * Needs a better name
 * lerp from 0..target and back again.
 */
fun Float.triangle(progress: Float, inflectAt: Float = 0.5F): Float =
    this * (progress.progressIn(0F, inflectAt) - progress.progressIn(inflectAt, 1F))

@FloatRange(from = 0.0, to = 1.0)
fun Float.progressIn(min: Float = 0F, max: Float): Float = coerceIn(min, max).normalizeIn(min, max)

/**
 * Reverse direction of a value between 0..1, useful for animations.
 */
fun Float.reversed() = 1F - this

/**
 * Flip the sign (+-) of the receiver.
 */
fun Float.inverted() = this * -1F

typealias Easing = (fraction: Float) -> Float

fun Float.withEasing(easing: Easing): Float = easing(this)
