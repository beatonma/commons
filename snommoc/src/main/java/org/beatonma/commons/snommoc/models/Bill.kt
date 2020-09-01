package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

data class ApiBill(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String?,
    @field:Json(name = Contract.ACT_NAME) val actName: String?,
    @field:Json(name = Contract.LABEL) val label: String?,
    @field:Json(name = Contract.HOMEPAGE) val homepage: String?,
    @field:Json(name = Contract.DATE) val date: LocalDate,
    @field:Json(name = Contract.BALLOT_NUMBER) val ballotNumber: Int?,
    @field:Json(name = Contract.BILL_CHAPTER) val billChapter: String?,
    @field:Json(name = Contract.IS_PRIVATE) val isPrivate: Boolean = false,
    @field:Json(name = Contract.IS_MONEY_BILL) val isMoneyBill: Boolean = false,
    @field:Json(name = Contract.PUBLIC_INVOLVEMENT_ALLOWED) val publicInvolvementAllowed: Boolean = false,
    @field:Json(name = Contract.PUBLICATIONS) val publications: List<ApiBillPublication> = listOf(),
    @field:Json(name = Contract.SESSION) val session: ApiParliamentarySession?,
    @field:Json(name = Contract.TYPE) val type: ApiBillType?,
    @field:Json(name = Contract.SPONSORS) val sponsors: List<ApiBillSponsor> = listOf(),
    @field:Json(name = Contract.STAGES) val stages: List<ApiBillStage> = listOf(),
)

data class ApiBillStage(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TYPE) val type: String,
    @field:Json(name = Contract.SITTINGS) val sittings: List<ApiBillStageSitting>
)

data class ApiBillSponsor(
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID?,
    @field:Json(name = Contract.PARTY) val party: ApiParty?
)

data class ApiBillType(
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String?,
)

data class ApiBillPublication(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
)

data class ApiBillStageSitting(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.DATE) val date: LocalDate,
    @field:Json(name = Contract.FORMAL) val isFormal: Boolean,
    @field:Json(name = Contract.PROVISIONAL) val isProvisional: Boolean,
)
