package org.beatonma.commons.data.core.social

import com.squareup.moshi.Json

/**
 * Names are lowercase so they can be used with Navigation Components deepLink
 * as part of the URL path.
 */
enum class SocialTargetType {
    member,
    bill,
    division_commons,
    division_lords,
    ;
}

data class SocialContent(
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "comments") val comments: List<SocialComment>,
    @field:Json(name = "votes") val votes: SocialVotes,
) {
    fun commentCount(): Int = comments.size
    fun voteCount(): Int = votes.voteCount()

    fun ayeVotes(): Int = votes.aye
    fun noVotes(): Int = votes.no
}
