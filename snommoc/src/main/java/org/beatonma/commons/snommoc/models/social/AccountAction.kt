package org.beatonma.commons.snommoc.models.social

import com.squareup.moshi.Json
import org.beatonma.commons.core.SnommocToken
import org.beatonma.commons.snommoc.Contract

sealed class AccountAction(@field:Json(name = Contract.ACCOUNT_ACTION) val action: String)

data class RenameAccountRequest(
    @field:Json(name = Contract.USERNAME) val currentUsername: String,
    @field:Json(name = Contract.ACCOUNT_NEW_USERNAME) val newUsername: String,
    @field:Json(name = Contract.SNOMMOC_TOKEN) val token: SnommocToken,
): AccountAction(action= Contract.ACCOUNT_ACTION_CHANGE_USERNAME)

data class DeleteUserRequest(
    @field:Json(name = Contract.GOOGLE_TOKEN) val gtoken: String,
    @field:Json(name = Contract.SNOMMOC_TOKEN) val token: SnommocToken,
): AccountAction("delete")
