package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(
    primaryKeys = ["town_name", "country_name"],
    tableName = "birthplace"
)
data class Birthplace(
    @field:Json(name = "town") @ColumnInfo(name = "town_name") val town: String,
    @field:Json(name = "country") @ColumnInfo(name = "country_name") val country: String = "UK"
)
