package org.beatonma.commons.snommoc.models.social

import com.squareup.moshi.Json
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDateTime


data class SocialComment(
    @field:Json(name = Contract.USERNAME) val username: String,
    @field:Json(name = Contract.TEXT) val text: String,
    @field:Json(name = Contract.CREATED_ON) val created: LocalDateTime,
    @field:Json(name = Contract.MODIFIED_ON) val modified: LocalDateTime,
)
