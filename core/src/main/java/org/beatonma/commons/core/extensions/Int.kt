package org.beatonma.commons.core.extensions

import kotlin.math.roundToInt

fun Int.lerpTo(end: Int, progress: Float): Int = this + ((end - this) * progress).roundToInt()
fun Float.lerpBetween(start: Int, end: Int): Int = start + ((end - start) * this).roundToInt()

/**
 * Map to a value between 0F..1F relative to the given limits.
 */
fun Int.normalizeIn(min: Int, max: Int): Float = toFloat().normalizeIn(min.toFloat(), max.toFloat())
