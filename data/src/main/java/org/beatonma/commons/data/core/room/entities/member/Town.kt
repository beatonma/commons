package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["town_name", "country_name"],
    tableName = "towns"
)
data class Town(
    @ColumnInfo(name = "town_name") val town: String,
    @ColumnInfo(name = "country_name") val country: String = "UK"
)
