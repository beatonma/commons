package org.beatonma.commons.core.extensions

import androidx.annotation.FloatRange
import kotlin.math.roundToInt

/**
 * Linear interpolation between [start] and [end], using the receiver Float as current progress.
 */
fun Float.lerpBetween(start: Int, end: Int): Int = start + ((end - start) * this).roundToInt()

/**
 * Map to a value between 0F..1F relative to the given limits.
 */
fun Int.normalizeIn(min: Int, max: Int): Float = toFloat().normalizeIn(min.toFloat(), max.toFloat())

/**
 * Return the normalized value of the receiver in the given [min]..[max] range.
 */
@FloatRange(from = 0.0, to = 1.0)
fun Int.progressIn(min: Int, max: Int): Float = coerceIn(min, max).normalizeIn(min, max)

/**
 * Get the closest multiple of [nearest] AFTER the receiver.
 */
fun Int.roundUp(nearest: Int): Int {
    val mod = this % nearest
    return if (mod == 0) this
    else {
        if (this < 0) this - mod
        else this + nearest - mod
    }
}

/**
 * Get the closest multiple of [nearest] BEFORE the receiver.
 */
fun Int.roundDown(nearest: Int): Int {
    val mod = this % nearest
    return if (mod == 0) this
    else {
        if (this < 0) this - nearest - mod
        else this - mod
    }
}
