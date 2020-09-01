package org.beatonma.commons.snommoc.models.social

import com.squareup.moshi.Json
import org.beatonma.commons.snommoc.Contract

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
    @field:Json(name = Contract.TITLE) val title: String?,
    @field:Json(name = Contract.COMMENTS) val comments: List<SocialComment>,
    @field:Json(name = Contract.VOTES) val votes: SocialVotes,
    @field:Json(name = Contract.VOTE) val userVote: SocialVoteType?,
) {
    fun commentCount(): Int = comments.size
    fun voteCount(): Int = votes.voteCount()

    fun ayeVotes(): Int = votes.aye
    fun noVotes(): Int = votes.no
}
