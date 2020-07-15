package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.network.retrofit.Contract

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


data class ApiTopicOfInterest(
    @field:Json(name = Contract.CATEGORY) val category: String,
    @field:Json(name = Contract.SUBJECT) val topic: String  // Either a comma- or semicolon-separated list
) {
    fun toTopicOfInterest(memberId: ParliamentID) = TopicOfInterest(
        memberId = memberId,
        category = category,
        topic = topic
    )
}
