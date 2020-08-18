package org.beatonma.commons.data.core.room.entities.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import org.beatonma.commons.data.core.repository.UserAccount
import org.beatonma.commons.network.retrofit.Contract

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
    @ColumnInfo(name = "snommoc_token") val snommocToken: String,
    @ColumnInfo(name = "google_id") val googleId: String,
    @ColumnInfo(name = "username") val username: String,
)

data class ApiUserToken(
    @field:Json(name = Contract.SNOMMOC_TOKEN) val snommocToken: String,
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
