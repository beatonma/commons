package org.beatonma.commons.core.extensions

/**
 * Iterates through a [List] using the index and calls [action] for each item.
 * This does not allocate an iterator like [Iterable.forEach].
 *
 * From androidx.compose.runtime.snapshots.ListUtils
 */
inline fun <T> List<T>.fastForEach(action: (T) -> Unit) {
    for (index in indices) {
        val item = get(index)
        action(item)
    }
}

inline fun <T> Array<T>.fastForEach(action: (T) -> Unit) {
    for (index in indices) {
        val item = get(index)
        action(item)
    }
}

/**
 * Return false if any of the given values are null
 */
fun allNotNull(vararg values: Any?): Boolean {
    values.fastForEach { value ->
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

/**
 * Roll over to beginning of list if requested position is too large.
 */
fun <T> List<T>.modGet(position: Int): T = this[position % this.size]

fun <T> List<T>.chooseAny(population: Int = size / 2): List<T> {
    val available = (0 until size).toMutableSet()

    return List(population) {
        val index = available.random()
        val v = get(index)
        available.remove(index)
        v
    }
}
