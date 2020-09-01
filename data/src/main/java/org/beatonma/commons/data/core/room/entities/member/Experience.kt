package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
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
    @ColumnInfo(name = "experience_category") val category: String,
    @ColumnInfo(name = "organisation") val organisation: String?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "start") override val start: LocalDate?,
    @ColumnInfo(name = "end") override val end: LocalDate?
): Named,
    Periodic {
    override val name: String get() = title
}
