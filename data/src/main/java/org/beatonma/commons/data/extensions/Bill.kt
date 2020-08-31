package org.beatonma.commons.data.extensions

import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.member.House

fun Bill.startedIn(): House = when {
    this.title.contains("[HL]") -> House.lords
    else -> House.commons
}

object BillStageCanonicalNames {
    const val FIRST_READING = "1st reading"
    const val SECOND_READING = "2nd reading"
    const val COMMITTEE_STAGE = "committee stage"
    const val REPORT_STAGE = "report stage"
    const val THIRD_READING = "3rd reading"
    const val CONSIDERATION_OF_AMENDMENTS = "consideration of "
    const val ROYAL_ASSENT = "royal assent"
}
