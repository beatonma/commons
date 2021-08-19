package org.beatonma.commons.snommoc.models.search

import android.net.Uri
import androidx.core.net.toUri
import com.squareup.moshi.Json
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.snommoc.CommonsService
import org.beatonma.commons.snommoc.Contract

data class MemberSearchResult(
    @field:Json(name = Contract.PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) override val name: String,
    @field:Json(name = Contract.PORTRAIT) val portraitUrl: String?,
    @field:Json(name = Contract.PARTY) val party: PartySearchResult?,
    @field:Json(name = Contract.CONSTITUENCY) val constituency: ConstituencySearchResult?,
    @field:Json(name = Contract.CURRENT_POST) val currentPost: String?,
): SearchResult {
    override fun toUri() = CommonsService.getMemberUrl(parliamentdotuk).toUri()
}

data class PartySearchResult(
    @field:Json(name = Contract.PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) override val name: String,
) : SearchResult {
    override fun toUri(): Uri {
        TODO("Not yet implemented")
    }
}

data class ConstituencySearchResult(
    @field:Json(name = Contract.PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) override val name: String,
) : SearchResult {
    override fun toUri(): Uri {
        TODO("Not yet implemented")
    }
}


interface SearchResult {
    val parliamentdotuk: ParliamentID
    val name: String

    fun toUri(): Uri
}
