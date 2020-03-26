package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

@Entity(tableName = "constituencies")
data class Constituency(
    @field:Json(name = PARLIAMENTDOTUK)
    @ColumnInfo(name = "constituency_parliamentdotuk")
    @PrimaryKey val parliamentdotuk: Int,

    @field:Json(name = "name") @ColumnInfo(name = "constituency_name") val name: String
//    @field:Json(name = "start") @ColumnInfo(name = "constituency_start") val start: Date? = null,
//    @field:Json(name = "end") @ColumnInfo(name = "constituency_end") val end: Date? = null
)
