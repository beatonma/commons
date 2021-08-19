package org.beatonma.commons.data.core.room.entities.user

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import org.beatonma.commons.core.SnommocToken

@Entity(
    primaryKeys = [
        "google_id",
        "snommoc_token"
    ],
    tableName = "user_tokens",
)
@Parcelize
data class UserToken(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "photo_url") val photoUrl: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "snommoc_token") val snommocToken: SnommocToken,
    @ColumnInfo(name = "google_id") val googleId: String,
    @ColumnInfo(name = "username") val username: String,
) : Parcelable
