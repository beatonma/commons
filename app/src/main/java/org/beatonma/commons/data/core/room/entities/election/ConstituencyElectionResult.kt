package org.beatonma.commons.data.core.room.entities.election

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.member.MemberProfile

@Entity(
    primaryKeys = [
        "result_member_id",
        "result_election_id",
    ],
    tableName = "constituency_results",
)
data class ConstituencyResult(
    @ColumnInfo(name = "result_member_id") val memberId: Int,
    @ColumnInfo(name = "result_election_id") val electionId: Int,
    @ColumnInfo(name = "result_constituency_id") val constituencyId: Int,
)


data class ApiConstituencyResult(
    @field:Json(name = "mp") val member: MemberProfile,
    @field:Json(name = "election") val election: Election,
) {
    fun toConstituencyResult(constituencyId: Int) = ConstituencyResult(
        memberId = member.parliamentdotuk,
        electionId = election.parliamentdotuk,
        constituencyId = constituencyId,
    )
}


data class ConstituencyResultWithDetails(
    @Embedded val result: ConstituencyResult,

    @Relation(parentColumn = "result_election_id", entityColumn = "election_$PARLIAMENTDOTUK")
    val election: Election,

    @Relation(parentColumn = "result_member_id", entityColumn = PARLIAMENTDOTUK)
    val profile: MemberProfile,
)
