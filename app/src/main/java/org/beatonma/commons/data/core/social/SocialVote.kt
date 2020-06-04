package org.beatonma.commons.data.core.social

import com.squareup.moshi.Json
import org.beatonma.commons.data.core.interfaces.Votable
import org.beatonma.commons.data.core.room.entities.user.UserToken

private const val JSON_NAME_AYE = "aye"
private const val JSON_NAME_NO = "no"

enum class SocialVoteType(val apiName: String) {
    AYE(JSON_NAME_AYE),
    NO(JSON_NAME_NO),
    ;
}

data class SocialVotes(
    @field:Json(name = JSON_NAME_AYE) val aye: Int = 0,
    @field:Json(name = JSON_NAME_NO) val no: Int = 0,
)

data class CreatedVote<T: Votable>(
    val userToken: UserToken,
    val target: T,
    val voteType: SocialVoteType,
)
