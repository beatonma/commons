package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
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
