package org.beatonma.commons.data.core.room.entities.division

import org.beatonma.commons.core.DivisionVoteType
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.member.Party

sealed interface HouseDivision<T : DivisionVoteType>

sealed interface HouseVoteData<T : DivisionVoteType> {
    val divisionId: ParliamentID
    val memberId: ParliamentID
    val memberName: String
    val vote: T
    val partyId: ParliamentID
}

sealed interface ResolvedHouseVote<T : DivisionVoteType> {
    val data: HouseVoteData<T>
    val party: Party
}
