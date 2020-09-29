package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

data class ApiParliamentarySession(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
)

data class ApiParty(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
)

data class ApiElection(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.DATE) val date: LocalDate,
    @field:Json(name = Contract.ELECTION_TYPE) val electionType: String,
)
