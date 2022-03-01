package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDateTime

data class ApiBill(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.LAST_UPDATE) val lastUpdate: LocalDateTime,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String?,
    @field:Json(name = Contract.IS_ACT) val isAct: Boolean,
    @field:Json(name = Contract.IS_DEFEATED) val isDefeated: Boolean,
    @field:Json(name = Contract.WITHDRAWN_AT) val withdrawnAt: LocalDateTime?,
    @field:Json(name = Contract.TYPE) val type: ApiBillType,
    @field:Json(name = Contract.SESSION_INTRODUCED) val sessionIntroduced: ApiSession,
    @field:Json(name = Contract.SESSIONS) val sessions: List<ApiSession>,
    @field:Json(name = Contract.CURRENT_STAGE) val currentStage: ApiBillStage,
    @field:Json(name = Contract.STAGES) val stages: List<ApiBillStage>,
    @field:Json(name = Contract.SPONSORS) val sponsors: List<ApiBillSponsor>,
    @field:Json(name = Contract.PUBLICATIONS) val publications: List<ApiBillPublication>,
)

data class ApiBillType(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String,
    @field:Json(name = Contract.CATEGORY) val category: String,
)

data class ApiSession(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
)

data class ApiOrganisation(
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.URL) val url: String,
)

data class ApiBillStage(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.DESCRIPTION) val description: String,
    @field:Json(name = Contract.HOUSE) val house: String,
    @field:Json(name = Contract.SESSION) val session: ApiSession,
    @field:Json(name = Contract.SITTINGS) val sittings: List<LocalDateTime>,
)

data class ApiBillSponsor(
    @field:Json(name = Contract.PROFILE) val member: ApiMemberProfile?,
    @field:Json(name = Contract.ORGANISATION) val organisation: ApiOrganisation?,
)

data class ApiBillPublication(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DATE) val date: LocalDateTime,
    @field:Json(name = Contract.LINKS) val links: List<ApiBillPublicationLink>,
    @field:Json(name = Contract.TYPE) val type: String,
)

data class ApiBillPublicationLink(
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.URL) val url: String,
    @field:Json(name = Contract.CONTENT_TYPE) val contentType: String,
)


//data class ApiBill(
//    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
//    @field:Json(name = Contract.TITLE) val title: String,
//    @field:Json(name = Contract.DESCRIPTION) val description: String?,
//    @field:Json(name = Contract.ACT_NAME) val actName: String?,
//    @field:Json(name = Contract.LABEL) val label: String?,
//    @field:Json(name = Contract.HOMEPAGE) val homepage: String?,
//    @field:Json(name = Contract.DATE) val date: LocalDate,
//    @field:Json(name = Contract.BALLOT_NUMBER) val ballotNumber: Int?,
//    @field:Json(name = Contract.BILL_CHAPTER) val billChapter: String?,
//    @field:Json(name = Contract.IS_PRIVATE) val isPrivate: Boolean = false,
//    @field:Json(name = Contract.IS_MONEY_BILL) val isMoneyBill: Boolean = false,
//    @field:Json(name = Contract.PUBLIC_INVOLVEMENT_ALLOWED) val publicInvolvementAllowed: Boolean = false,
//    @field:Json(name = Contract.PUBLICATIONS) val publications: List<ApiBillPublication> = listOf(),
//    @field:Json(name = Contract.SESSION) val session: ApiParliamentarySession?,
//    @field:Json(name = Contract.TYPE) val type: ApiBillType?,
//    @field:Json(name = Contract.SPONSORS) val sponsors: List<ApiBillSponsor> = listOf(),
//    @field:Json(name = Contract.STAGES) val stages: List<ApiBillStage> = listOf(),
//)
//
//data class ApiBillStage(
//    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
//    @field:Json(name = Contract.TYPE) val type: String,
//    @field:Json(name = Contract.SITTINGS) val sittings: List<ApiBillStageSitting>
//)
//
//data class ApiBillSponsor(
//    @field:Json(name = Contract.NAME) val name: String,
//    @field:Json(name = Contract.PROFILE) val profile: ApiMemberProfile?,
//)
//
//data class ApiBillType(
//    @field:Json(name = Contract.NAME) val name: String,
//    @field:Json(name = Contract.DESCRIPTION) val description: String?,
//)
//
//data class ApiBillPublication(
//    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
//    @field:Json(name = Contract.TITLE) val title: String,
//)
//
//data class ApiBillStageSitting(
//    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
//    @field:Json(name = Contract.DATE) val date: LocalDate,
//    @field:Json(name = Contract.FORMAL) val isFormal: Boolean,
//    @field:Json(name = Contract.PROVISIONAL) val isProvisional: Boolean,
//)
