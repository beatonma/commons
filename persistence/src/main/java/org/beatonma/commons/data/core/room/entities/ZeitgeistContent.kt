package org.beatonma.commons.data.core.room.entities

import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.ZeitgeistReason

interface ZeitgeistContent {
    val id: ParliamentID
    val reason: ZeitgeistReason
    val priority: Int
}
