package org.beatonma.commons.data.core.social

import com.squareup.moshi.Json
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.room.entities.user.UserToken

private const val JSON_NAME_AYE = "aye"
private const val JSON_NAME_NO = "no"
private const val JSON_NAME_NULL = "null"

enum class SocialVoteType(val apiName: String) {
    AYE(JSON_NAME_AYE),
    NO(JSON_NAME_NO),
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
