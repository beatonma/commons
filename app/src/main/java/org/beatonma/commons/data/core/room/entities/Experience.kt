package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(
    primaryKeys = [
        "experience_member_id",
        "title"
    ],
    tableName = "experiences"
)
data class Experience(
    @ColumnInfo(name = "experience_member_id") val memberId: Int,
    @field:Json(name = "category") @ColumnInfo(name = "experience_category") val category: String,
    @field:Json(name = "organisation") @ColumnInfo(name = "organisation") val organisation: String?,
    @field:Json(name = "title") @ColumnInfo(name = "title") val title: String,
    @field:Json(name = "start") @ColumnInfo(name = "start") val start: String?,
    @field:Json(name = "end") @ColumnInfo(name = "end") val end: String?
)
