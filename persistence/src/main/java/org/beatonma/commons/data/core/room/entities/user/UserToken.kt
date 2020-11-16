package org.beatonma.commons.data.core.room.entities.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.beatonma.commons.core.SnommocToken

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
