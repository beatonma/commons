package org.beatonma.commons.data.core.search

import android.net.Uri
import androidx.core.net.toUri
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.network.retrofit.CommonsService

data class MemberSearchResult(
    @field:Json(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") override val name: String,
    @field:Json(name = "portrait") val portraitUrl: String?,
    @field:Json(name = "party") val party: PartySearchResult?,
    @field:Json(name = "constituency") val constituency: ConstituencySearchResult?,
    @field:Json(name = "current_post") val currentPost: String?,
): SearchResult {
    override fun toUri() = CommonsService.getMemberUrl(parliamentdotuk).toUri()
}

data class PartySearchResult(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") val name: String,
)

data class ConstituencySearchResult(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") val name: String,
)


interface SearchResult {
    val parliamentdotuk: ParliamentID
    val name: String

    fun toUri(): Uri
}
