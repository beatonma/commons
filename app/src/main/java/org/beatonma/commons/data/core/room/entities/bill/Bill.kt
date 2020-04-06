package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

/**
 * Basic bill without related.
 */
@Entity(
    tableName = "bills"
)
data class Bill(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "bill_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @field:Json(name = "title") @ColumnInfo(name = "bill_title") val title: String,
    @field:Json(name = "description") @ColumnInfo(name = "bill_description") val description: String?,
    @field:Json(name = "act_name") @ColumnInfo(name = "bill_act_name") val actName: String?,
    @field:Json(name = "label") @ColumnInfo(name = "bill_label") val label: String?,
    @field:Json(name = "homepage") @ColumnInfo(name = "bill_homepage") val homepage: String?,
    @field:Json(name = "date") @ColumnInfo(name = "bill_date") val date: String,
    @field:Json(name = "ballot_number") @ColumnInfo(name = "bill_ballot_number") val ballotNumber: Int?,
    @field:Json(name = "bill_chapter") @ColumnInfo(name = "bill_bill_chapter") val billChapter: String?,
    @field:Json(name = "is_private") @ColumnInfo(name = "bill_private") val isPrivate: Boolean,
    @field:Json(name = "is_money_bill") @ColumnInfo(name = "bill_money_bill") val isMoneyBill: Boolean,
    @field:Json(name = "public_involvement_allowed") @ColumnInfo(name = "bill_public_involvement_allowed") val publicInvolvementAllowed: Boolean,
    @field:Json(name = "session") @ColumnInfo(name = "bill_session_id") val sessionId: Int,
    @field:Json(name = "type") @ColumnInfo(name = "bill_type_id") val typeId: String,
)


data class ApiBill(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "bill_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @field:Json(name = "title") @ColumnInfo(name = "bill_title") val title: String,
    @field:Json(name = "description") @ColumnInfo(name = "bill_description") val description: String?,
    @field:Json(name = "act_name") @ColumnInfo(name = "bill_act_name") val actName: String?,
    @field:Json(name = "label") @ColumnInfo(name = "bill_label") val label: String?,
    @field:Json(name = "homepage") @ColumnInfo(name = "bill_homepage") val homepage: String?,
    @field:Json(name = "date") @ColumnInfo(name = "bill_date") val date: String,
    @field:Json(name = "ballot_number") @ColumnInfo(name = "bill_ballot_number") val ballotNumber: Int?,
    @field:Json(name = "bill_chapter") @ColumnInfo(name = "bill_bill_chapter") val billChapter: String?,
    @field:Json(name = "is_private") @ColumnInfo(name = "bill_private") val isPrivate: Boolean,
    @field:Json(name = "is_money_bill") @ColumnInfo(name = "bill_money_bill") val isMoneyBill: Boolean,
    @field:Json(name = "public_involvement_allowed") @ColumnInfo(name = "bill_public_involvement_allowed") val publicInvolvementAllowed: Boolean,
    @field:Json(name = "publications") @ColumnInfo(name = "bill_publications") val publications: List<BillPublication>,
    @field:Json(name = "session") @ColumnInfo(name = "bill_session") val session: ParliamentarySession,
    @field:Json(name = "type") @ColumnInfo(name = "bill_type") val type: BillType,
//    @field:Json(name = "sponsors") @ColumnInfo(name = "sponsors") val sponsors: List<BillSponsor>,
//    @field:Json(name = "stages") @ColumnInfo(name = "stages") val stages: List<BillStage>,
    @field:Json(name = "sponsors") @Ignore val sponsors: List<BillSponsor>,
    @field:Json(name = "stages") @Ignore val stages: List<ApiBillStage>,
)


data class MinimalBill(
    @ColumnInfo(name = "bill_$PARLIAMENTDOTUK") val parliamentdotuk: Int,
    @ColumnInfo(name = "bill_title") val title: String,
    @ColumnInfo(name = "bill_description") val description: String?,
    @ColumnInfo(name = "bill_date") val date: String?,
)


data class BillWithSessionAndType(
    @Embedded val bill: Bill,
    @Relation(parentColumn = "bill_session_id", entityColumn = "session_$PARLIAMENTDOTUK")
    val session: ParliamentarySession,

    @Relation(parentColumn = "bill_type_id", entityColumn = "billtype_name")
    val type: BillType
)
