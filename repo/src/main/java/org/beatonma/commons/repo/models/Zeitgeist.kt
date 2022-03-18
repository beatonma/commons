package org.beatonma.commons.repo.models

import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill
import org.beatonma.commons.data.core.room.entities.division.ZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.member.ZeitgeistMember

data class Zeitgeist(
    var members: List<ZeitgeistMember> = listOf(),
    var divisions: List<ZeitgeistDivision> = listOf(),
    var bills: List<ZeitgeistBill> = listOf(),
)
