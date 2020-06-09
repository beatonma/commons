package org.beatonma.commons.data.core.social

import com.squareup.moshi.Json
import org.beatonma.commons.data.SnommocToken
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.room.entities.user.UserToken

private const val JSON_NAME_AYE = "aye"
private const val JSON_NAME_NO = "no"
private const val JSON_NAME_NULL = "null"

/**
 * Names are lowercase so they can be mapped directly to API response.
 */
enum class SocialVoteType(val apiName: String) {
    aye(JSON_NAME_AYE),
    no(JSON_NAME_NO),
    NULL(JSON_NAME_NULL),  // Used to undo/reset a user vote - never received from API.
    ;
}

data class SocialVotes(
    @field:Json(name = JSON_NAME_AYE) val aye: Int = 0,
    @field:Json(name = JSON_NAME_NO) val no: Int = 0,
) {
    fun voteCount(): Int = aye + no
}

data class CreatedVote(
    val userToken: UserToken,
    val target: SocialTarget,
    val voteType: SocialVoteType,
)

data class DeletedVote(
    val token: SnommocToken
)
