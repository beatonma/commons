package org.beatonma.commons.data.core.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = ["parliamentdotuk"],
            childColumns = ["member_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FeaturedMember(
    @PrimaryKey val member_id: Int
)

data class FeaturedMemberProfile(
    @Embedded val profile: MemberProfileWithRelatedObjects,
    @Embedded val featured: FeaturedMember
)
