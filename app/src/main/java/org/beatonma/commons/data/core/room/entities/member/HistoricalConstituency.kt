package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.election.Election
import java.time.LocalDate

@Entity(
    indices = [
        Index("memberfor_election_id", "memberfor_member_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["memberfor_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Election::class,
            parentColumns = ["election_$PARLIAMENTDOTUK"],
            childColumns = ["memberfor_election_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "memberfor_election_id",
        "memberfor_member_id"
    ],
    tableName = "historic_constituencies"
)
data class HistoricalConstituency(
    @ColumnInfo(name = "memberfor_member_id") val memberId: Int,
    @field:Json(name = "constituency") @ColumnInfo(name = "memberfor_constituency_id") val constituencyId: Int,
    @field:Json(name = "start") @ColumnInfo(name = "memberfor_start") override val start: LocalDate,
    @field:Json(name = "end") @ColumnInfo(name = "memberfor_end") override val end: LocalDate?,
    @field:Json(name = "election") @ColumnInfo(name = "memberfor_election_id") val electionId: Int
): Periodic

data class HistoricalConstituencyWithElection(
    @Embedded val historicalConstituency: HistoricalConstituency,

    @Relation(parentColumn = "memberfor_constituency_id", entityColumn = "constituency_$PARLIAMENTDOTUK")
    val constituency: Constituency,

    @Relation(parentColumn = "memberfor_election_id", entityColumn = "election_$PARLIAMENTDOTUK")
    val election: Election
): Named,
    Periodic {
    override val start: LocalDate? get() = historicalConstituency.start
    override val end: LocalDate? get() = historicalConstituency.end
    override val name: String get() = constituency.name
}


data class ApiHistoricalConstituency(
    @field:Json(name = "constituency") val constituency: Constituency,
    @field:Json(name = "start") override val start: LocalDate,
    @field:Json(name = "end") override val end: LocalDate?,
    @field:Json(name = "election") val election: Election
): Periodic {
    fun toHistoricalConstituency(member: Int) = HistoricalConstituency(
        memberId = member,
        constituencyId = constituency.parliamentdotuk,
        start = start,
        end = end,
        electionId = election.parliamentdotuk
    )
}
