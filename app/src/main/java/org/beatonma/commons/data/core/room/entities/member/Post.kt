package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["post_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "post_$PARLIAMENTDOTUK",
        "post_member_id"
    ],
    tableName = "posts"
)
data class Post(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "post_$PARLIAMENTDOTUK") val parliamentdotuk: Int,
    @field:Json(name = "post_member_id") @ColumnInfo(name = "post_member_id", index = true) val memberId: Int,
    @field:Json(name = "name") @ColumnInfo(name = "post_name") val name: String,
    @ColumnInfo(name = "post_type") val postType: PostType,
    @field:Json(name = "start") @ColumnInfo(name = "start") val start: Date?,
    @field:Json(name = "end") @ColumnInfo(name = "end") val end: Date?
) {
    enum class PostType {
        GOVERNMENTAL,
        PARLIAMENTARY,
        OPPOSITION
    }
}

data class ApiPosts(
    @Embedded @field:Json(name = "governmental") val governmental: List<Post>,
    @Embedded @field:Json(name = "parliamentary") val parliamentary: List<Post>,
    @Embedded @field:Json(name = "opposition") val opposition: List<Post>
)
