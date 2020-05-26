package org.beatonma.commons.data.core

import java.time.LocalDate

interface Dated: Temporal {
    val date: LocalDate?
}
