package org.beatonma.commons.data.core.room.entities.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import org.beatonma.commons.data.SnommocToken
import org.beatonma.commons.data.core.repository.UserAccount
import org.beatonma.commons.snommoc.Contract

@Entity(
    primaryKeys = [
        "google_id",
        "snommoc_token"
    ],
    tableName = "user_tokens",
)
data class UserToken(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "photo_url") val photoUrl: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "snommoc_token") val snommocToken: SnommocToken,
    @ColumnInfo(name = "google_id") val googleId: String,
    @ColumnInfo(name = "username") val username: String,
)

data class ApiUserToken(
    @field:Json(name = Contract.SNOMMOC_TOKEN) val snommocToken: SnommocToken,
    @field:Json(name = Contract.GOOGLE_TOKEN) val googleTokenStub: String,
    @field:Json(name = Contract.USERNAME) val username: String,
) {
    fun composeToUserToken(account: UserAccount) = UserToken(
        name = account.name,
        photoUrl = account.photoUrl,
        email = account.email,
        snommocToken = this.snommocToken,
        googleId = account.googleId,
        username = this.username,
    )
}


sealed class AccountAction(@field:Json(name = Contract.ACCOUNT_ACTION) val action: String)
data class RenameAccountRequest(
    @field:Json(name = Contract.USERNAME) val currentUsername: String,
    @field:Json(name = Contract.ACCOUNT_NEW_USERNAME) val newUsername: String,
    @field:Json(name = Contract.SNOMMOC_TOKEN) val token: SnommocToken,
): AccountAction(action=Contract.ACCOUNT_ACTION_CHANGE_USERNAME)

data class DeleteUserRequest(
    @field:Json(name = Contract.GOOGLE_TOKEN) val gtoken: String,
    @field:Json(name = Contract.SNOMMOC_TOKEN) val token: SnommocToken,
): AccountAction("delete")
