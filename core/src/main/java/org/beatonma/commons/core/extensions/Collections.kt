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

inline fun <T> List<T>.fastForEachIndexed(action: (Int, T) -> Unit) {
    for (index in indices) {
        val item = get(index)
        action(index, item)
    }
}

inline fun <T> Array<T>.fastForEach(action: (T) -> Unit) {
    for (index in indices) {
        val item = get(index)
        action(item)
    }
}

inline fun <T> Array<T>.fastForEachIndexed(action: (Int, T) -> Unit) {
    for (index in indices) {
        val item = get(index)
        action(index, item)
    }
}

/**
 * Return false if any of the given values are null
 */
fun allNotNull(vararg values: Any?, verbose: Boolean = false): Boolean {
    if (verbose) {
        values.fastForEachIndexed { index, value ->
            if (value == null) {
                println("allNotNull: value at position $index is null")
                return false
            }
        }
    }
    else {
        values.fastForEach { value ->
            if (value == null) {
                return false
            }
        }
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

/**
 * Return true if all provided [values] are equal, or no [values] are provided.
 */
fun <T> allEqual(vararg values: T?): Boolean =
    values.toSet().size <= 1

/**
 * If [orNull] is true, returns true if all items in the list are null or equal to [expected].
 * If [orNull] is false (default), returns true only if all items in the list are equal to [expected].
 */
fun <T> Collection<T?>.allEqualTo(expected: T?, orNull: Boolean = false): Boolean {
    if (isEmpty()) return false
    if (orNull) return indexOfFirst { it != null && it != expected } == -1
    return indexOfFirst { it != expected } == -1
}

/**
 * Return the item at [position] in the receiving [List], or null if the position
 * does not exist.
 */
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

inline fun <K, V, R> Map<out K, V>.mapIndexed(transform: (index: Int, Map.Entry<K, V>) -> R): List<R> {
    var index = 0
    return map { m -> transform(index++, m) }
}

/**
 * Call [block] on each non-overlapping pair of items in the receiving [List].
 */
inline fun <T> List<T>.pairwise(block: (T, T) -> Unit) {
    check(this.size % 2 == 0) { "pairwise operations require a list with an even number of items." }

    for (i in 0 until size step 2) {
        block(get(i), get(i + 1))
    }
}


/**
 * Returns a list containing the results of applying the given [transform] to each
 * non-overlapping pair of items in the receiving [List].
 */
inline fun <T, R> List<T>.pairwiseMap(transform: (T, T) -> R): List<R> {
    check(this.size % 2 == 0) { "pairwise operations require a list with an even number of items." }

    val results = mutableListOf<R>()
    for (i in 0 until size step 2) {
        results.add(transform(get(i), get(i + 1)))
    }

    return results
}

/**
 * Returns a list of [Pair]s containing each non-overlapping pair of items in the receiving [List].
 */
fun <T> List<T>.pairs(): List<Pair<T, T>> {
    check(this.size % 2 == 0) { "pairwise operations require a list with an even number of items." }

    val results = mutableListOf<Pair<T, T>>()
    for (i in 0 until size step 2) {
        results.add(Pair(get(i), get(i + 1)))
    }

    return results
}
