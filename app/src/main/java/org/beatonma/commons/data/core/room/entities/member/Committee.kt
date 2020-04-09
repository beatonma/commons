package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

@Entity(
    primaryKeys = [
        "committee_$PARLIAMENTDOTUK",
        "committee_member_id"
    ],
    tableName = "committee_memberships"
)
data class CommitteeMembership(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "committee_$PARLIAMENTDOTUK")
    val parliamentdotuk: Int,
    @ColumnInfo(name = "committee_member_id") val memberId: Int,
    @field:Json(name = "name") @ColumnInfo(name = "committee_name") val name: String,
    @field:Json(name = "start") @ColumnInfo(name = "committee_start") val start: String?,
    @field:Json(name = "end") @ColumnInfo(name = "committee_end") val end: String?,
)

@Entity(
    indices = [
        Index("committee_id", "chair_member_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = CommitteeMembership::class,
            parentColumns = ["committee_$PARLIAMENTDOTUK", "committee_member_id"],
            childColumns = ["committee_id", "chair_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "chair_start",
        "committee_id",
        "chair_member_id"
    ],
    tableName = "committee_chairships"
)
data class CommitteeChairship(
    @ColumnInfo(name = "committee_id") val committeeId: Int,
    @ColumnInfo(name = "chair_member_id") val memberId: Int,
    @field:Json(name = "start") @ColumnInfo(name = "chair_start") val start: String,
    @field:Json(name = "end") @ColumnInfo(name = "chair_end") val end: String?,
)

data class CommitteeMemberWithChairs(
    @Embedded val membership: CommitteeMembership,

    @Relation(
        parentColumn = "committee_parliamentdotuk",
        entityColumn = "committee_id"
    )
    val chairs: List<CommitteeChairship>,
)

data class ApiCommittee(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: Int,
    val memberId: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "start") val start: String?,
    @field:Json(name = "end") val end: String?,
    @field:Json(name = "chair") val chairs: List<CommitteeChairship>,
) {

    fun toCommitteeMembership(member: Int) = CommitteeMembership(
        parliamentdotuk = parliamentdotuk,
        memberId = member,
        name = name,
        start = start,
        end = end
    )
}
