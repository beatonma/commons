package org.beatonma.commons.core.extensions

import kotlin.math.roundToInt

fun Int.lerp(end: Int, progress: Float): Int = this + ((end - this) * progress).roundToInt()

/**
 * Map to a value between 0F..1F relative to the given limits.
 */
fun Int.normalizeIn(min: Int, max: Int): Float = toFloat().normalizeIn(min.toFloat(), max.toFloat())
