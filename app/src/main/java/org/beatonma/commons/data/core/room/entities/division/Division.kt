package org.beatonma.commons.data.core.room.entities.division

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.member.House

@Entity(
    tableName = "divisions",
)
data class Division(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "division_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @field:Json(name = "title") @ColumnInfo(name = "title") val title: String,
    @field:Json(name = "date") @ColumnInfo(name = "date") val date: String,
    @field:Json(name = "passed") @ColumnInfo(name = "passed") val passed: Boolean,
    @field:Json(name = "ayes") @ColumnInfo(name = "ayes") val ayes: Int,
    @field:Json(name = "noes") @ColumnInfo(name = "noes") val noes: Int,
    @field:Json(name = "house") @ColumnInfo(name = "house") val house: House,
)

data class ApiDivision(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "division_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @field:Json(name = "title") @ColumnInfo(name = "title") val title: String,
    @field:Json(name = "date") @ColumnInfo(name = "date") val date: String,
    @field:Json(name = "passed") @ColumnInfo(name = "passed") val passed: Boolean,
    @field:Json(name = "ayes") @ColumnInfo(name = "ayes") val ayes: Int,
    @field:Json(name = "noes") @ColumnInfo(name = "noes") val noes: Int,
    @field:Json(name = "house") @ColumnInfo(name = "house") val house: House,
    @field:Json(name = "votes") @Ignore val votes: List<ApiVote>,
)

data class DivisionWithVotes(
    @Embedded val division: Division,
    @Relation(parentColumn = "division_$PARLIAMENTDOTUK", entityColumn = "dvote_division_id", entity = Vote::class)
    val votes: List<Vote>
)
