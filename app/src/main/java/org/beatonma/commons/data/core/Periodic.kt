package org.beatonma.commons.data.core

import java.time.LocalDate

/**
 * Something with and start and/or end date
 */
interface Periodic {
    val start: LocalDate?
    val end: LocalDate?
}
