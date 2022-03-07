package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate
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
    @field:Json(name = Contract.HOUSE) val house: House,
    @field:Json(name = Contract.SESSION) val session: ApiSession,
    @field:Json(name = Contract.SITTINGS) val sittings: List<LocalDate>,
    @field:Json(name = Contract.SITTING_LATEST) val latestSitting: LocalDate,
)

data class ApiBillSponsor(
    @field:Json(name = Contract.ID) val id: Int,
    @field:Json(name = Contract.PROFILE) val member: ApiMemberProfile?,
    @field:Json(name = Contract.ORGANISATION) val organisation: ApiOrganisation?,
)

data class ApiBillPublication(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DATE) val date: LocalDate,
    @field:Json(name = Contract.LINKS) val links: List<ApiBillPublicationLink>,
    @field:Json(name = Contract.TYPE) val type: String,
)

data class ApiBillPublicationLink(
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.URL) val url: String,
    @field:Json(name = Contract.CONTENT_TYPE) val contentType: String,
)
