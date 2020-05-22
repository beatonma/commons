package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import org.beatonma.commons.data.core.Named
import org.beatonma.commons.data.core.Periodic
import java.time.LocalDate

@Entity(
    primaryKeys = [
        "experience_member_id",
        "title"
    ],
    tableName = "experiences"
)
data class Experience(
    @ColumnInfo(name = "experience_member_id") val memberId: Int,
    @field:Json(name = "category") @ColumnInfo(name = "experience_category") val category: String,
    @field:Json(name = "organisation") @ColumnInfo(name = "organisation") val organisation: String?,
    @field:Json(name = "title") @ColumnInfo(name = "title") val title: String,
    @field:Json(name = "start") @ColumnInfo(name = "start") override val start: LocalDate?,
    @field:Json(name = "end") @ColumnInfo(name = "end") override val end: LocalDate?
): Named, Periodic {
    override val name: String get() = title
}
