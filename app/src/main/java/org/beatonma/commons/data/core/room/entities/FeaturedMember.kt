package org.beatonma.commons.data.core.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.beatonma.commons.data.PARLIAMENTDOTUK


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["member_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FeaturedMember(
    @PrimaryKey val member_id: Int,
    val about: String? = null
)

data class FeaturedMemberProfile(
    @Embedded val profile: MemberProfileWithRelatedObjects,
    @Embedded val featured: FeaturedMember
)
