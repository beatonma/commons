package org.beatonma.commons.snommoc.models.social

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
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

@Parcelize
data class SocialContent(
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.COMMENTS) val comments: List<SocialComment>,
    @field:Json(name = Contract.VOTES) val votes: SocialVotes,
    @field:Json(name = Contract.VOTE) val userVote: SocialVoteType?,
) : Parcelable {
    val commentCount: Int get() = comments.size
    val voteCount: Int get() = votes.voteCount()

    val ayeVotes: Int get() = votes.aye
    val noVotes: Int get() = votes.no
}

val EmptySocialContent = SocialContent("", listOf(), SocialVotes(-1, -1), null)
