package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.MinimalMember

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
    @ColumnInfo(name = "featured_member_id") @PrimaryKey val memberId: Int,
    @ColumnInfo(name = "featured_about") val about: String? = null
)

data class FeaturedMemberProfile(
    @Embedded val featured: FeaturedMember,
    @Relation(
        parentColumn = "featured_member_id",
        entityColumn = "parliamentdotuk",
        entity = MemberProfile::class
    ) val profile: MinimalMember
)
