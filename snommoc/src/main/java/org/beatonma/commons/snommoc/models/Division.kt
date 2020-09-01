package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

data class ApiDivision(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String?,
    @field:Json(name = Contract.DATE) val date: LocalDate,
    @field:Json(name = Contract.HOUSE) val house: House,
    @field:Json(name = Contract.PASSED) val passed: Boolean,
    @field:Json(name = Contract.AYES) val ayes: Int,
    @field:Json(name = Contract.NOES) val noes: Int,
    @field:Json(name = Contract.ABSTENTIONS) val abstentions: Int?,
    @field:Json(name = Contract.DID_NOT_VOTE) val didNotVote: Int?,
    @field:Json(name = Contract.NON_ELIGIBLE) val nonEligible: Int?,
    @field:Json(name = Contract.SUSPENDED_OR_EXPELLED) val suspendedOrExpelled: Int?,
    @field:Json(name = Contract.ERRORS) val errors: Int?,
    @field:Json(name = Contract.DEFERRED_VOTE) val deferredVote: Boolean,
    @field:Json(name = Contract.WHIPPED_VOTE) val whippedVote: Boolean?,
    @field:Json(name = Contract.VOTES) val votes: List<ApiVote>,
)


data class ApiMemberVote(
    @field:Json(name = Contract.DIVISION) val division: ApiDivision,
    @field:Json(name = Contract.VOTE_TYPE) val voteType: VoteType,
)


data class ApiVote(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val memberId: ParliamentID,
    @field:Json(name = Contract.NAME) val memberName: String,
    @field:Json(name = Contract.VOTE) val voteType: VoteType,
    @field:Json(name = Contract.PARTY) val party: ApiParty?,
)
