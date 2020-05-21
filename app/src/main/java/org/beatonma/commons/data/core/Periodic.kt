package org.beatonma.commons.data.core

import java.util.*

/**
 * Something with and start and/or end date
 */
interface Periodic {
    val start: Date?
    val end: Date?
}
