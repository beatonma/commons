package org.beatonma.commons.core.extensions

import kotlin.math.roundToInt

fun Int.lerpTo(end: Int, progress: Float): Int = this + ((end - this) * progress).roundToInt()
fun Float.lerpBetween(start: Int, end: Int): Int = start + ((end - start) * this).roundToInt()

/**
 * Map to a value between 0F..1F relative to the given limits.
 */
fun Int.normalizeIn(min: Int, max: Int): Float = toFloat().normalizeIn(min.toFloat(), max.toFloat())


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
