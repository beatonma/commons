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
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "interest_id") val interestId: Int,
    @field:Json(name = "category") @ColumnInfo(name = "interest_category") val category: String,
    @field:Json(name = "description") @ColumnInfo(name = "interest_description") val description: String,
    @field:Json(name = "created") @ColumnInfo(name = "interest_created") val dateCreated: String?,
    @field:Json(name = "amended") @ColumnInfo(name = "interest_amended") val dateAmended: String?,
    @field:Json(name = "deleted") @ColumnInfo(name = "interest_deleted") val dateDeleted: String?,
    @field:Json(name = "registered_late") @ColumnInfo(name = "interest_registered_late") val registeredLate: Boolean
)
