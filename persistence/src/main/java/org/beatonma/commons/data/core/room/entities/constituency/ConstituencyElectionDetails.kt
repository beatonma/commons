package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.election.Election

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Constituency::class,
            parentColumns = ["constituency_$PARLIAMENTDOTUK"],
            childColumns = ["c_e_r_constituency_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Election::class,
            parentColumns = ["election_$PARLIAMENTDOTUK"],
            childColumns = ["c_e_r_election_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ],
    tableName = "constituency_election_results"
)
data class ConstituencyElectionDetails(
    @PrimaryKey @ColumnInfo(name = "c_e_r_$PARLIAMENTDOTUK") override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "c_e_r_constituency_id") val constituencyId: ParliamentID,
    @ColumnInfo(name = "c_e_r_election_id") val electionId: ParliamentID,
    @ColumnInfo(name = "c_e_r_electorate") val electorate: Int,
    @ColumnInfo(name = "c_e_r_turnout") val turnout: Int,
    @ColumnInfo(name = "c_e_r_turnout_fraction") val turnoutFraction: String,
    @ColumnInfo(name = "c_e_r_result") val result: String,
    @ColumnInfo(name = "c_e_r_majority") val majority: Int,
): Parliamentdotuk


/* Candidates */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ConstituencyElectionDetails::class,
            parentColumns = ["c_e_r_$PARLIAMENTDOTUK"],
            childColumns = ["c_e_r_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "c_e_r_id",
        "candidate_name",
    ],
    tableName = "constituency_candidates"
)
data class ConstituencyCandidate(
    @ColumnInfo(name = "c_e_r_id") val resultsId: Int,
    @ColumnInfo(name = "candidate_name") val name: String,
    @ColumnInfo(name = "candidate_party_name") val partyName: String,
    @ColumnInfo(name = "candidate_order") val order: Int,
    @ColumnInfo(name = "candidate_votes") val votes: Int,
)


data class ConstituencyElectionDetailsWithCandidates(
    @Embedded val details: ConstituencyElectionDetails,

    @Relation(parentColumn = "c_e_r_$PARLIAMENTDOTUK", entityColumn = "c_e_r_id")
    val candidates: List<ConstituencyCandidate>,
)


data class ConstituencyElectionDetailsWithExtras(
    var details: ConstituencyElectionDetails? = null,
    var candidates: List<ConstituencyCandidate>? = null,
    var election: Election? = null,
    var constituency: Constituency? = null,
)
