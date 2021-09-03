package org.beatonma.commons.data.core.room.entities

import org.beatonma.commons.core.ParliamentID

interface ZeitgeistContent {
    val id: ParliamentID
    val reason: String?
    val priority: Int
}

interface ResolvedZeitgeistContent<T : ZeitgeistContent> {
    val zeitgeistContent: T
}
