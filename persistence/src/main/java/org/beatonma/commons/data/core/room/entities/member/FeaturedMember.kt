package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.MinimalMember

@Deprecated("Use zeitgeist")
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["featured_member_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    tableName = "featured_members"
)
data class FeaturedMember(
    @ColumnInfo(name = "featured_member_id") @PrimaryKey val memberId: ParliamentID,
    @ColumnInfo(name = "featured_about") val about: String? = null,
)

@Deprecated("Use zeitgeist")
data class FeaturedMemberProfile(
    @Embedded val featured: FeaturedMember,
    @Relation(
        parentColumn = "featured_member_id",
        entityColumn = "parliamentdotuk",
        entity = MemberProfile::class
    ) val profile: MinimalMember,
)

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
    @ColumnInfo(name = "zeitgeist_member_reason") val reason: String? = null,
)

data class ResolvedZeitgeistMember(
    @Embedded val zeitgeistMember: ZeitgeistMember,
    @Relation(
        parentColumn = "zeitgeist_member_id",
        entityColumn = PARLIAMENTDOTUK,
        entity = MemberProfile::class
    )
    val member: MinimalMember,
)
