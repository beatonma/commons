package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.election.ApiConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import java.util.*

@Entity(
    tableName = "constituencies"
)
data class Constituency(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "constituency_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @field:Json(name = "name") @ColumnInfo(name = "constituency_name") val name: String,
    @ColumnInfo(name = "constituency_start") val start: Date? = null,
    @ColumnInfo(name = "constituency_end") val end: Date? = null,
)


data class ApiConstituency(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "start") val start: Date?,
    @field:Json(name = "end") val end: Date?,
    @field:Json(name = "boundary") val boundary: ConstituencyBoundary?,
    @field:Json(name = "results") val results: List<ApiConstituencyResult>,
) {
    fun toConstituency(): Constituency {
        return Constituency(
            parliamentdotuk = parliamentdotuk,
            name = name,
            start = start,
            end = end,
        )
    }
}


data class ConstituencyWithDetails(
    @Embedded
    val constituency: Constituency,

    @Relation(parentColumn = "constituency_$PARLIAMENTDOTUK", entityColumn = "boundary_constituency_id")
    val boundary: ConstituencyBoundary?,

    @Relation(parentColumn = "constituency_$PARLIAMENTDOTUK", entityColumn = "result_constituency_id", entity = ConstituencyResult::class)
    val electionResults: List<ConstituencyResultWithDetails>,
)
