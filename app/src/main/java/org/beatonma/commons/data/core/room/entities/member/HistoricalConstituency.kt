package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK


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
    @field:Json(name = "start") @ColumnInfo(name = "memberfor_start") val start: String,
    @field:Json(name = "end") @ColumnInfo(name = "memberfor_end") val end: String?,
    @field:Json(name = "election") @ColumnInfo(name = "memberfor_election_id") val electionId: Int
)

data class HistoricalConstituencyWithElection(
    @Embedded val historicalConstituency: HistoricalConstituency,

    @Relation(parentColumn = "memberfor_constituency_id", entityColumn = "constituency_$PARLIAMENTDOTUK")
    val constituency: Constituency,

    @Relation(parentColumn = "memberfor_election_id", entityColumn = "election_$PARLIAMENTDOTUK")
    val election: Election
)


data class ApiHistoricalConstituency(
    @field:Json(name = "constituency") val constituency: Constituency,
    @field:Json(name = "start") val start: String,
    @field:Json(name = "end") val end: String?,
    @field:Json(name = "election") val election: Election
) {
    fun toHistoricalConstituency(member: Int) = HistoricalConstituency(
        memberId = member,
        constituencyId = constituency.parliamentdotuk,
        start = start,
        end = end,
        electionId = election.parliamentdotuk
    )
}
