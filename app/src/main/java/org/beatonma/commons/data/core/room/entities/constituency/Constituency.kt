package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.Named
import org.beatonma.commons.data.core.Parliamentdotuk
import org.beatonma.commons.data.core.Periodic
import org.beatonma.commons.data.core.room.entities.election.ApiConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.core.room.entities.member.BasicProfileWithParty
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import java.time.LocalDate

@Entity(
    tableName = "constituencies"
)
data class Constituency(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "constituency_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") @ColumnInfo(name = "constituency_name") override val name: String,
    @ColumnInfo(name = "constituency_start") override val start: LocalDate? = null,
    @ColumnInfo(name = "constituency_end") override val end: LocalDate? = null,
): Parliamentdotuk, Named, Periodic


data class ApiConstituency(
    @field:Json(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") override val name: String,
    @field:Json(name = "start") override val start: LocalDate?,
    @field:Json(name = "end") override val end: LocalDate?,
    @field:Json(name = "mp") val memberProfile: MemberProfile?,
    @field:Json(name = "boundary") val boundary: ConstituencyBoundary?,
    @field:Json(name = "results") val results: List<ApiConstituencyResult>,
): Parliamentdotuk, Named, Periodic {
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


data class CompleteConstituency(
    val constituency: Constituency? = null,
    val member: BasicProfileWithParty? = null,
    val electionResults: List<ConstituencyResultWithDetails>? = null,
    val boundary: ConstituencyBoundary? = null,
)
