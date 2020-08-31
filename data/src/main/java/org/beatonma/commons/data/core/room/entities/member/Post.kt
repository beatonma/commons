package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

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
    @ColumnInfo(name = "post_$PARLIAMENTDOTUK") override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "post_member_id", index = true) val memberId: ParliamentID,
    @ColumnInfo(name = "post_name") override val name: String,
    @ColumnInfo(name = "post_type") val postType: PostType,
    @ColumnInfo(name = "start") override val start: LocalDate?,
    @ColumnInfo(name = "end") override val end: LocalDate?
): Parliamentdotuk,
    Named,
    Periodic {
    enum class PostType {
        GOVERNMENTAL,
        PARLIAMENTARY,
        OPPOSITION
    }
}

data class ApiPost(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.START) val start: LocalDate?,
    @field:Json(name = Contract.END) val end: LocalDate?
) {
    fun toPost(memberId: ParliamentID, postType: Post.PostType) = Post(
        parliamentdotuk = parliamentdotuk,
        memberId = memberId,
        name = name,
        postType = postType,
        start = start,
        end = end
    )
}

data class ApiPosts(
    @field:Json(name = Contract.GOVERNMENTAL) val governmental: List<ApiPost>,
    @field:Json(name = Contract.PARLIAMENTARY) val parliamentary: List<ApiPost>,
    @field:Json(name = Contract.OPPOSITION) val opposition: List<ApiPost>
)
