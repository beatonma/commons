package org.beatonma.commons.data.core.interfaces

import java.time.LocalDate

interface Dated: Temporal {
    val date: LocalDate?
}
