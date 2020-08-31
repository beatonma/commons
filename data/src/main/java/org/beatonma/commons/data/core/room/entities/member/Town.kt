package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import org.beatonma.commons.snommoc.Contract

@Entity(
    primaryKeys = ["town_name", "country_name"],
    tableName = "towns"
)
data class Town(
    @field:Json(name = Contract.TOWN) @ColumnInfo(name = "town_name") val town: String,
    @field:Json(name = Contract.COUNTRY) @ColumnInfo(name = "country_name") val country: String = "UK"
)
