package org.beatonma.commons.data.core.room.entities.election

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.squareup.moshi.Json
import org.beatonma.commons.data.core.room.entities.member.ApiMemberProfile
import org.beatonma.commons.data.core.room.entities.member.BasicProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.snommoc.Contract

@Entity(
    primaryKeys = [
        "result_member_id",
        "result_election_id",
        "result_constituency_id",
    ],
    tableName = "constituency_results",
)
data class ConstituencyResult(
    @ColumnInfo(name = "result_member_id") val memberId: Int,
    @ColumnInfo(name = "result_election_id") val electionId: Int,
    @ColumnInfo(name = "result_constituency_id") val constituencyId: Int,
)


data class ApiConstituencyResult(
    @field:Json(name = Contract.MP) val member: ApiMemberProfile,
    @field:Json(name = Contract.ELECTION) val election: ApiElection,
) {
    fun toConstituencyResult(constituencyId: Int) = ConstituencyResult(
        memberId = member.parliamentdotuk,
        electionId = election.parliamentdotuk,
        constituencyId = constituencyId,
    )
}


data class ConstituencyResultWithDetails(
    @Embedded val election: Election,
    @Embedded val profile: BasicProfile,
    @Embedded val party: Party,
)
