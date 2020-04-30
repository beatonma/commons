package org.beatonma.commons.data.core.room.entities.election

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import java.util.*

@Entity(
    tableName = "elections"
)
data class Election(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "election_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @field:Json(name = "name") @ColumnInfo(name = "election_name") val name: String,
    @field:Json(name = "date") @ColumnInfo(name = "election_date") val date: Date,
    @field:Json(name = "election_type") @ColumnInfo(name = "election_type") val electionType: String
)
