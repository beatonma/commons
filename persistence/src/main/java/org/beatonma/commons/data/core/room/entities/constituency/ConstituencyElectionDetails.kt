package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.allNotNull
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.MemberProfileWithPartyConstituency
import org.beatonma.commons.data.core.room.entities.member.Party

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Constituency::class,
            parentColumns = ["constituency_id"],
            childColumns = ["c_e_r_constituency_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Election::class,
            parentColumns = ["election_id"],
            childColumns = ["c_e_r_election_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ],
    tableName = "constituency_election_results"
)
data class ConstituencyElectionDetails(
    @PrimaryKey @ColumnInfo(name = "c_e_r_id") override val parliamentdotuk: ParliamentID,
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
            parentColumns = ["c_e_r_id"],
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
    @ColumnInfo(name = "candidate_profile_id") val profile: MemberProfile?,
    @ColumnInfo(name = "candidate_party_name") val partyName: String,
    @ColumnInfo(name = "candidate_party_id") val party: Party?,
    @ColumnInfo(name = "candidate_order") val order: Int,
    @ColumnInfo(name = "candidate_votes") val votes: Int,
)

data class ConstituencyCandidateWithParty(
    @Embedded val candidate: ConstituencyCandidate,

    @Relation(
        parentColumn = "candidate_profile_id",
        entityColumn = "member_id",
        entity = MemberProfile::class
    )
    val person: MemberProfileWithPartyConstituency?,

    @Relation(parentColumn = "candidate_party_id", entityColumn = "party_id")
    val party: Party?,
)


data class ConstituencyElectionDetailsWithCandidates(
    @Embedded val details: ConstituencyElectionDetails,

    @Relation(
        parentColumn = "c_e_r_id",
        entityColumn = "c_e_r_id",
        entity = ConstituencyCandidate::class
    )
    val candidates: List<ConstituencyCandidateWithParty>,
)


data class ConstituencyElectionDetailsWithExtras(
    val details: ConstituencyElectionDetails,
    val candidates: List<ConstituencyCandidateWithParty>,
    val election: Election,
    val constituency: Constituency,
)

data class ConstituencyElectionDetailsBuilder(
    var details: ConstituencyElectionDetails? = null,
    var candidates: List<ConstituencyCandidateWithParty>? = null,
    var election: Election? = null,
    var constituency: Constituency? = null,
) {
    val isComplete: Boolean
        get() = allNotNull(
            details,
            candidates,
            election,
            constituency,
        )

    fun toConstituencyElectionDetailsWithExtras() = ConstituencyElectionDetailsWithExtras(
        details!!,
        candidates!!,
        election!!,
        constituency!!,
    )
}
