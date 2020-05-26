package org.beatonma.commons.data.core

import java.time.LocalDate
import java.time.Period

/**
 * Something with and start and/or end date
 */
interface Periodic: Temporal {
    val start: LocalDate?
    val end: LocalDate?
}


suspend fun <T: Periodic> compressConsecutiveItems(
    periodic: List<T>?,
    areItemsTheSame: (T, T) -> Boolean,
    ifMonthsLessThan: Int = 2,  // If a gap between 2 same items < this value they should be combined into a single item
    combineFunc: (T, T) -> T,
): List<T>? {
    periodic ?: return null

    /**
     * Returns true if the items can be squished into one.
     */
    fun shouldCombine(a: T, b: T): Boolean {
        if (!areItemsTheSame(a, b)) return false

        val firstEnd = a.end ?: return false
        val secondStart = b.start ?: return false

        val difference = Period.between(firstEnd, secondStart)
        return difference.toTotalMonths() < ifMonthsLessThan
    }

    val compressed = mutableListOf<T>()

    val sorted = periodic.sortedBy { it.start }
    var current: T = sorted.firstOrNull() ?: return compressed
    sorted.forEach { item ->
        if (current === item) {
            return@forEach
        }
        current = if (shouldCombine(current, item)) {
            combineFunc(current, item)
        }
        else {
            compressed.add(current)
            item
        }
    }

    compressed.add(current)

    return compressed
}
