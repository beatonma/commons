package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.data.core.room.entities.election.ApiConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.core.room.entities.member.ApiMemberProfile
import org.beatonma.commons.data.core.room.entities.member.BasicProfileWithParty
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

@Entity(
    tableName = "constituencies"
)
data class Constituency(
    @ColumnInfo(name = "constituency_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "constituency_name") override val name: String,
    @ColumnInfo(name = "constituency_start") override val start: LocalDate? = null,
    @ColumnInfo(name = "constituency_end") override val end: LocalDate? = null,
): Parliamentdotuk,
    Named,
    Periodic


data class ApiConstituency(
    @field:Json(name = Contract.PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) override val name: String,
    @field:Json(name = Contract.START) override val start: LocalDate?,
    @field:Json(name = Contract.END) override val end: LocalDate?,
    @field:Json(name = Contract.MP) val memberProfile: ApiMemberProfile?,
    @field:Json(name = Contract.BOUNDARY) val boundary: ApiConstituencyBoundary?,
    @field:Json(name = Contract.RESULTS) val results: List<ApiConstituencyResult>,
): Parliamentdotuk,
    Named,
    Periodic {
    fun toConstituency(): Constituency {
        return Constituency(
            parliamentdotuk = parliamentdotuk,
            name = name,
            start = start,
            end = end,
        )
    }
}

data class ApiConstituencyMinimal(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
) {
    fun toConstituency(): Constituency = Constituency(
        parliamentdotuk = parliamentdotuk,
        name = name
    )
}


data class ConstituencyWithBoundary(
    @Embedded
    val constituency: Constituency,

    @Relation(parentColumn = "constituency_$PARLIAMENTDOTUK", entityColumn = "boundary_constituency_id")
    val boundary: ConstituencyBoundary?,
)


data class CompleteConstituency(
    var constituency: Constituency? = null,
    var member: BasicProfileWithParty? = null,
    var electionResults: List<ConstituencyResultWithDetails>? = null,
    var boundary: ConstituencyBoundary? = null,
)
