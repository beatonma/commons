package org.beatonma.commons.data.core.social

import com.squareup.moshi.Json
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.network.retrofit.Contract
import java.time.LocalDateTime


data class SocialComment(
    @field:Json(name = Contract.USERNAME) val username: String,
    @field:Json(name = Contract.TEXT) val text: String,
    @field:Json(name = Contract.CREATED_ON) val created: LocalDateTime,
    @field:Json(name = Contract.MODIFIED_ON) val modified: LocalDateTime,
)

data class CreatedComment(
    val userToken: UserToken,
    val target: SocialTarget,
    val text: String,
)
