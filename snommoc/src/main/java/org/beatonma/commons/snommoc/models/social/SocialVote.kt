package org.beatonma.commons.snommoc.models.social

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import org.beatonma.commons.core.SnommocToken

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

@Parcelize
data class SocialVotes(
    @field:Json(name = JSON_NAME_AYE) val aye: Int = 0,
    @field:Json(name = JSON_NAME_NO) val no: Int = 0,
) : Parcelable {
    fun voteCount(): Int = (aye + no).coerceAtLeast(0)
}


data class DeletedVote(
    val token: SnommocToken
)
