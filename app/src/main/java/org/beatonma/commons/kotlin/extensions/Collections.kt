package org.beatonma.commons.kotlin.extensions

/**
 * Return false if any of the given values are null
 */
fun allNotNull(vararg values: Any?): Boolean {
    values.forEach { value ->
        if (value == null) return false
    }
    return true
}

fun Collection<Any?>.allNotNull(): Boolean {
    forEach { value ->
        if (value == null) return false
    }
    return true
}


fun <T> firstNotNull(vararg args: T?): T? = args.firstOrNull { it != null }
fun <T> Collection<T?>.firstNotNull() = firstOrNull { it != null }
