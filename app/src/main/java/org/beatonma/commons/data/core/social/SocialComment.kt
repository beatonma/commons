package org.beatonma.commons.data.core.social

import com.squareup.moshi.Json
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.room.entities.user.UserToken
import java.time.LocalDateTime


data class SocialComment(
    @field:Json(name = "username") val username: String,
    @field:Json(name = "text") val text: String,
    @field:Json(name = "created_on") val created: LocalDateTime,
    @field:Json(name = "modified_on") val modified: LocalDateTime,
)

data class CreatedComment(
    val userToken: UserToken,
    val target: SocialTarget,
    val text: String,
)
