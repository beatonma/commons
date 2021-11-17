package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

data class ApiConstituency(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.START) val start: LocalDate?,
    @field:Json(name = Contract.END) val end: LocalDate?,
    @field:Json(name = Contract.MP) val memberProfile: ApiMemberProfile?,
    @field:Json(name = Contract.BOUNDARY) val boundary: ApiConstituencyBoundary?,
    @field:Json(name = Contract.RESULTS) val results: List<ApiConstituencyResult>,
)

data class ApiConstituencyMinimal(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
)


data class ApiConstituencyBoundary(
    @field:Json(name = Contract.KML) val kml: String,
    @field:Json(name = Contract.AREA) val area: String?,
    @field:Json(name = Contract.BOUNDARY_LENGTH) val boundaryLength: String?,
    @field:Json(name = Contract.CENTER_LATITUDE) val centerLat: String?,
    @field:Json(name = Contract.CENTER_LONGITUDE) val centerLong: String?,
)

data class ApiConstituencyResult(
    @field:Json(name = Contract.MP) val member: ApiMemberProfile,
    @field:Json(name = Contract.ELECTION) val election: ApiElection,
)

data class ApiConstituencyElectionDetails(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.ELECTORATE) val electorate: Int,
    @field:Json(name = Contract.TURNOUT) val turnout: Int,
    @field:Json(name = Contract.TURNOUT_FRACTION) val turnoutFraction: String,
    @field:Json(name = Contract.RESULT) val result: String,
    @field:Json(name = Contract.MAJORITY) val majority: Int,
    @field:Json(name = Contract.CANDIDATES) val candidates: List<ApiConstituencyCandidate>,
    @field:Json(name = Contract.CONSTITUENCY) val constituency: ApiConstituencyMinimal,
    @field:Json(name = Contract.ELECTION) val election: ApiElection,
)

data class ApiConstituencyCandidate(
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.PROFILE) val profile: ApiMemberProfile?,
    @field:Json(name = Contract.PARTY_NAME) val partyName: String,
    @field:Json(name = Contract.PARTY) val party: ApiParty?,
    @field:Json(name = Contract.ORDER) val order: Int,
    @field:Json(name = Contract.VOTES) val votes: Int,
)
