package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import org.beatonma.commons.core.PARLIAMENTDOTUK
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
    @ColumnInfo(name = "memberfor_constituency_id") val constituencyId: Int,
    @ColumnInfo(name = "memberfor_start") override val start: LocalDate,
    @ColumnInfo(name = "memberfor_end") override val end: LocalDate?,
    @ColumnInfo(name = "memberfor_election_id") val electionId: Int
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


