package org.beatonma.commons.data.core.interfaces

import java.time.LocalDate

interface Temporal {
    fun startOf(): LocalDate = when (this) {
        is Periodic -> this.start ?: throw Exception("No start set on Periodic item: $this")
        is Dated -> this.date ?: throw Exception("No date set on Dated item: $this")
        else -> throw Exception("Unhandled Temporal class: ${this.javaClass.canonicalName}")
    }

    /**
     * Return the end date for this object, or return [today] if it appears to be ongoing.
     */
    fun endOf(today: LocalDate): LocalDate = when (this) {
        is Periodic -> this.end ?: today
        is Dated -> this.date ?: throw Exception("No date set on Dated item: $this")
        else -> throw Exception("Unhandled Temporal class: ${this.javaClass.canonicalName}")
    }
}
