package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Periodic
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
