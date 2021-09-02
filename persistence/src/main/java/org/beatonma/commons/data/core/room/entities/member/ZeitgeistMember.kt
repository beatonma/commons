package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.MinimalMember
import org.beatonma.commons.data.core.room.entities.ZeitgeistContent

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["zeitgeist_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "zeitgeist_members",
)
data class ZeitgeistMember(
    @ColumnInfo(name = "zeitgeist_member_id") @PrimaryKey val memberId: ParliamentID,
    @ColumnInfo(name = "zeitgeist_member_reason") override val reason: String? = null,
    @ColumnInfo(name = "zeitgeist_member_priority") override val priority: Int = 50,
) : ZeitgeistContent

data class ResolvedZeitgeistMember(
    @Embedded val zeitgeistMember: ZeitgeistMember,
    @Relation(
        parentColumn = "zeitgeist_member_id",
        entityColumn = PARLIAMENTDOTUK,
        entity = MemberProfile::class
    )
    val member: MinimalMember,
)
