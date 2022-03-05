package org.beatonma.commons.repo.models

import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill
import org.beatonma.commons.data.core.room.entities.division.ResolvedZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.member.ResolvedZeitgeistMember

data class Zeitgeist(
    var members: List<ResolvedZeitgeistMember> = listOf(),
    var divisions: List<ResolvedZeitgeistDivision> = listOf(),
    var bills: List<ZeitgeistBill> = listOf(),
)
