package org.beatonma.commons.kotlin.extensions

fun Float.map(fromMin: Float, fromMax: Float, toMin: Float, toMax: Float): Float {
    return normalizeIn(fromMin, fromMax)
        .mapTo(toMin, toMax)
}

/**
 * Receiver value is assumed to be between 0F..1F!
 */
fun Float.mapTo(min: Int, max: Int, forcePositive: Boolean = true): Int {
    val range: Float = (max - min).toFloat()
    val self = if (forcePositive) this.coerceIn(0F, 1F) else this
    return (min + (self * range)).toInt()
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
 * Map to a value between 0F..1F relative to the given limits.
 */
fun Float.normalizeIn(min: Float, max: Float): Float {
    val range = max - min
    return ((this - min) / range)
        .coerceIn(0F, 1F)
}

/**
 * Receiver value is assumed to be between 0F..1F!
 */
fun Float.mapToByte(): Int = mapTo(0, 255)
