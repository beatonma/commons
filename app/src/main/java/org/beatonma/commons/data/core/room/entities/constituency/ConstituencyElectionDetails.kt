package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.Party

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


data class ApiConstituencyElectionDetails(
    @field:Json(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = "electorate") val electorate: Int,
    @field:Json(name = "turnout") val turnout: Int,
    @field:Json(name = "turnout_fraction") val turnoutFraction: String,
    @field:Json(name = "result") val result: String,
    @field:Json(name = "majority") val majority: Int,
    @field:Json(name = "candidates") val candidates: List<ApiConstituencyCandidate>,
    @field:Json(name = "constituency") val constituency: Constituency,
    @field:Json(name = "election") val election: Election,
): Parliamentdotuk {
    fun toConstituencyElectionDetails() = ConstituencyElectionDetails(
        parliamentdotuk = parliamentdotuk,
        electorate = electorate,
        turnout = turnout,
        turnoutFraction = turnoutFraction,
        result = result,
        majority = majority,
        constituencyId = constituency.parliamentdotuk,
        electionId = election.parliamentdotuk,
    )
}



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


data class ApiConstituencyCandidate(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "party_name") val partyName: String,
    @field:Json(name = "order") val order: Int,
    @field:Json(name = "votes") val votes: Int,
) {
    fun toConstituencyCandidate(resultsId: Int) = ConstituencyCandidate(
        resultsId = resultsId,
        name = name,
        partyName = partyName,
        order = order,
        votes = votes
    )
}


data class ConstituencyElectionDetailsWithCandidates(
    @Embedded val details: ConstituencyElectionDetails,

    @Relation(parentColumn = "c_e_r_$PARLIAMENTDOTUK", entityColumn = "c_e_r_id")
    val candidates: List<ConstituencyCandidate>,
)


data class ConstituencyElectionDetailsWithExtras(
    val details: ConstituencyElectionDetails? = null,
    val candidates: List<ConstituencyCandidate>? = null,
    val election: Election? = null,
    val constituency: Constituency? = null,
)


data class CandidateWithParty(
    val candidate: ConstituencyCandidate,
    val party: Party?
)
