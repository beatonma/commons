package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

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
    @field:Json(name = "category") @ColumnInfo(name = "topic_category") val category: String,
    @field:Json(name = "subject") @ColumnInfo(name = "topic") val topic: String  // Either a comma- or semicolon-separated list
)
