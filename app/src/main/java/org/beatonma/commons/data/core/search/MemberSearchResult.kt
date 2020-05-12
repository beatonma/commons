package org.beatonma.commons.data.core.search

import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID

data class MemberSearchResult(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "portrait") val portraitUrl: String?,
    @field:Json(name = "party") val party: PartySearchResult?,
    @field:Json(name = "constituency") val constituency: ConstituencySearchResult?,
    @field:Json(name = "current_post") val currentPost: String?,
)

data class PartySearchResult(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") val name: String,
)

data class ConstituencySearchResult(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") val name: String,
)
