package org.beatonma.commons.core.extensions

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

fun <T> List<T>.safeGet(position: Int): T? {
    return when {
        this.size > position -> this[position]
        else -> null
    }
}

fun <T> List<T>.chooseAny(population: Int = size / 2): List<T> {
    val available = (0 until size).toMutableSet()

    return List(population) {
        val index = available.random()
        val v = get(index)
        available.remove(index)
        v
    }
}
