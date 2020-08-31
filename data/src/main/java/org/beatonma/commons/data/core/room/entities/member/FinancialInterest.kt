package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["interest_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "interest_member_id",
        "interest_id"
    ],
    tableName = "financial_interests"
)
data class FinancialInterest(
    @ColumnInfo(name = "interest_member_id") val memberId: Int,
    @ColumnInfo(name = "interest_id") override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "interest_category") val category: String,
    @ColumnInfo(name = "interest_description") val description: String,
    @ColumnInfo(name = "interest_created") val dateCreated: LocalDate?,
    @ColumnInfo(name = "interest_amended") val dateAmended: LocalDate?,
    @ColumnInfo(name = "interest_deleted") val dateDeleted: LocalDate?,
    @ColumnInfo(name = "interest_registered_late") val registeredLate: Boolean
): Parliamentdotuk,
    Named,
    Dated {
    override val name: String
        get() = description
    override val date: LocalDate? get() = dateCreated
}


data class ApiFinancialInterest(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.CATEGORY) val category: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String,
    @field:Json(name = Contract.CREATED) val dateCreated: LocalDate?,
    @field:Json(name = Contract.AMENDED) val dateAmended: LocalDate?,
    @field:Json(name = Contract.DELETED) val dateDeleted: LocalDate?,
    @field:Json(name = Contract.REGISTERED_LATE) val registeredLate: Boolean
) {
    fun toFinancialInterest(memberId: ParliamentID) = FinancialInterest(
        memberId = memberId,
        parliamentdotuk = parliamentdotuk,
        category = category,
        description = description,
        dateCreated = dateCreated,
        dateAmended = dateAmended,
        dateDeleted = dateDeleted,
        registeredLate = registeredLate
    )
}
