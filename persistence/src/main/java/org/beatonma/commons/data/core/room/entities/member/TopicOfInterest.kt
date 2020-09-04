package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import org.beatonma.commons.core.PARLIAMENTDOTUK

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["topic_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "topic_member_id",
        "topic"
    ],
    tableName = "topics_of_interest"
)
data class TopicOfInterest(
    @ColumnInfo(name = "topic_member_id") val memberId: Int,
    @ColumnInfo(name = "topic_category") val category: String,
    @ColumnInfo(name = "topic") val topic: String  // Either a comma- or semicolon-separated list
)
