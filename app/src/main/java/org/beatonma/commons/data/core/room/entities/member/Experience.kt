package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.network.retrofit.Contract
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


data class ApiExperience(
    @field:Json(name = Contract.CATEGORY) val category: String,
    @field:Json(name = Contract.ORGANISATION) val organisation: String?,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.START) val start: LocalDate?,
    @field:Json(name = Contract.END) val end: LocalDate?
) {
    fun toExperience(memberId: ParliamentID) = Experience(
        memberId = memberId,
        category = category,
        organisation = organisation,
        title = title,
        start = start,
        end = end
    )
}
