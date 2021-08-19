package org.beatonma.commons.snommoc.models.social

import com.squareup.moshi.Json
import org.beatonma.commons.core.SnommocToken
import org.beatonma.commons.snommoc.Contract

data class ApiUserToken(
    @field:Json(name = Contract.SNOMMOC_TOKEN) val snommocToken: SnommocToken,
    @field:Json(name = Contract.GOOGLE_TOKEN) val googleTokenStub: String,
    @field:Json(name = Contract.USERNAME) val username: String,
)

data class ApiUserName(
    @field:Json(name = Contract.USERNAME) val username: String,
)
