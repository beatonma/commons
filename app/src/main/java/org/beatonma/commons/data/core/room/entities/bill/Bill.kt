package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import java.util.*

/**
 * Basic bill without related.
 */
@Entity(
    tableName = "bills"
)
data class Bill(
    @ColumnInfo(name = "bill_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "bill_title") val title: String,
    @ColumnInfo(name = "bill_description") val description: String?,
    @ColumnInfo(name = "bill_act_name") val actName: String?,
    @ColumnInfo(name = "bill_label") val label: String?,
    @ColumnInfo(name = "bill_homepage") val homepage: String?,
    @ColumnInfo(name = "bill_date") val date: Date,
    @ColumnInfo(name = "bill_ballot_number") val ballotNumber: Int?,
    @ColumnInfo(name = "bill_bill_chapter") val billChapter: String?,
    @ColumnInfo(name = "bill_private") val isPrivate: Boolean,
    @ColumnInfo(name = "bill_money_bill") val isMoneyBill: Boolean,
    @ColumnInfo(name = "bill_public_involvement_allowed") val publicInvolvementAllowed: Boolean,
    @ColumnInfo(name = "bill_session_id") val sessionId: ParliamentID?,
    @ColumnInfo(name = "bill_type_id") val typeId: String?,
)


data class ApiBill(
    @field:Json(name = PARLIAMENTDOTUK) @PrimaryKey val parliamentdotuk: ParliamentID,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "act_name") val actName: String?,
    @field:Json(name = "label") val label: String?,
    @field:Json(name = "homepage") val homepage: String?,
    @field:Json(name = "date") val date: Date,
    @field:Json(name = "ballot_number") val ballotNumber: Int?,
    @field:Json(name = "bill_chapter") val billChapter: String?,
    @field:Json(name = "is_private") val isPrivate: Boolean = false,
    @field:Json(name = "is_money_bill") val isMoneyBill: Boolean = false,
    @field:Json(name = "public_involvement_allowed") val publicInvolvementAllowed: Boolean = false,
    @field:Json(name = "publications") val publications: List<BillPublication> = listOf(),
    @field:Json(name = "session") val session: ParliamentarySession?,
    @field:Json(name = "type") val type: BillType?,
    @field:Json(name = "sponsors") @Ignore val sponsors: List<BillSponsor> = listOf(),
    @field:Json(name = "stages") @Ignore val stages: List<ApiBillStage> = listOf(),
) {
    fun toBill() = Bill(
        parliamentdotuk = parliamentdotuk,
        title = title,
        description = description,
        actName = actName,
        label = label,
        homepage = homepage,
        date = date,
        ballotNumber = ballotNumber,
        billChapter = billChapter,
        isPrivate = isPrivate,
        isMoneyBill = isMoneyBill,
        publicInvolvementAllowed = publicInvolvementAllowed,
        sessionId = session?.parliamentdotuk,
        typeId = type?.name
    )
}


data class MinimalBill(
    @ColumnInfo(name = "bill_$PARLIAMENTDOTUK") val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "bill_title") val title: String,
    @ColumnInfo(name = "bill_description") val description: String?,
    @ColumnInfo(name = "bill_date") val date: Date?,
)


data class BillWithSessionAndType(
    @Embedded val bill: Bill,
    @Relation(parentColumn = "bill_session_id", entityColumn = "session_$PARLIAMENTDOTUK")
    val session: ParliamentarySession,

    @Relation(parentColumn = "bill_type_id", entityColumn = "billtype_name")
    val type: BillType
)


data class CompleteBill(
    @Embedded val bill: Bill? = null,
    @Ignore val session: ParliamentarySession? = null,
    @Ignore val type: BillType? = null,
    @Ignore val publications: List<BillPublication>? = null,
    @Ignore val sponsors: List<BillSponsor>? = null,
    @Ignore val stages: List<BillStageWithSittings>? = null,
)
