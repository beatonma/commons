package org.beatonma.commons.data.core.room.entities

import androidx.room.*
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.MinimalMember

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["member_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    tableName = "featured_members"
)
data class FeaturedMember(
    @PrimaryKey val member_id: Int,
    val about: String? = null
)

data class FeaturedMemberProfile(
    @Embedded val featured: FeaturedMember,
    @Relation(
        parentColumn = "member_id",
        entityColumn = "parliamentdotuk",
        entity = MemberProfile::class
    ) val profile: MinimalMember
)
