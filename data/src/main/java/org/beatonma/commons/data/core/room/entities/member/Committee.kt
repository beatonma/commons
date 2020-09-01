package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Periodic
import java.time.LocalDate

@Entity(
    primaryKeys = [
        "committee_$PARLIAMENTDOTUK",
        "committee_member_id"
    ],
    tableName = "committee_memberships"
)
data class CommitteeMembership(
    @ColumnInfo(name = "committee_$PARLIAMENTDOTUK") override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "committee_member_id") val memberId: ParliamentID,
    @ColumnInfo(name = "committee_name") override val name: String,
    @ColumnInfo(name = "committee_start") override val start: LocalDate?,
    @ColumnInfo(name = "committee_end") override val end: LocalDate?,
): Parliamentdotuk,
    Named,
    Periodic


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
    @ColumnInfo(name = "committee_id") val committeeId: ParliamentID,
    @ColumnInfo(name = "chair_member_id") val memberId: ParliamentID,
    @ColumnInfo(name = "chair_start") override val start: LocalDate,
    @ColumnInfo(name = "chair_end") override val end: LocalDate?,
): Periodic



data class CommitteeMemberWithChairs(
    @Embedded val membership: CommitteeMembership,

    @Relation(
        parentColumn = "committee_parliamentdotuk",
        entityColumn = "committee_id"
    )
    val chairs: List<CommitteeChairship>,
): Named,
    Periodic {
    override val name: String
        get() = membership.name
    override val start: LocalDate?
        get() = membership.start
    override val end: LocalDate?
        get() = membership.end
}
