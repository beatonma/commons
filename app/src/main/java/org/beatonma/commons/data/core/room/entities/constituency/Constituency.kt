package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.member.MemberProfile

@Entity(
    tableName = "constituencies"
)
data class Constituency(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "constituency_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @field:Json(name = "name") @ColumnInfo(name = "constituency_name") val name: String,
    @ColumnInfo(name = "constituency_start") val start: String? = null,
    @ColumnInfo(name = "constituency_end") val end: String? = null,
)


data class ApiConstituency(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "start") val start: String?,
    @field:Json(name = "end") val end: String?,
    @field:Json(name = "boundary") val boundary: ConstituencyBoundary?,
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


data class ConstituencyWithBoundary(
    @Embedded
    val constituency: Constituency,

    @Relation(parentColumn = "constituency_$PARLIAMENTDOTUK", entityColumn = "boundary_constituency_id")
    val boundary: ConstituencyBoundary?,
)



data class ConstituencyDetails(
    @Embedded val mp: MemberProfile,

    @Relation(parentColumn = "constituency_id", entityColumn = "constituency_$PARLIAMENTDOTUK")
    val constituency: Constituency,

    @Relation(parentColumn = "constituency_id", entityColumn = "boundary_constituency_id")
    val boundary: ConstituencyBoundary?,
)
