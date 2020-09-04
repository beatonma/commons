package org.beatonma.commons.data.core.room.entities.election

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import org.beatonma.commons.data.core.room.entities.member.BasicProfile
import org.beatonma.commons.data.core.room.entities.member.Party

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


data class ConstituencyResultWithDetails(
    @Embedded val election: Election,
    @Embedded val profile: BasicProfile,
    @Embedded val party: Party,
)
