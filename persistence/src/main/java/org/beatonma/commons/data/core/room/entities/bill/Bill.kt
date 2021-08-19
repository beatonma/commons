package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.allNotNull
import org.beatonma.commons.data.core.interfaces.Commentable
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Votable
import java.time.LocalDate

/**
 * Basic bill without related.
 */
@Entity(
    tableName = "bills"
)
data class Bill(
    @ColumnInfo(name = "bill_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "bill_title") val title: String,
    @ColumnInfo(name = "bill_description") val description: String?,
    @ColumnInfo(name = "bill_act_name") val actName: String?,
    @ColumnInfo(name = "bill_label") val label: String?,
    @ColumnInfo(name = "bill_homepage") val homepage: String?,
    @ColumnInfo(name = "bill_date") override val date: LocalDate,
    @ColumnInfo(name = "bill_ballot_number") val ballotNumber: Int?,
    @ColumnInfo(name = "bill_bill_chapter") val billChapter: String?,
    @ColumnInfo(name = "bill_private") val isPrivate: Boolean,
    @ColumnInfo(name = "bill_money_bill") val isMoneyBill: Boolean,
    @ColumnInfo(name = "bill_public_involvement_allowed") val publicInvolvementAllowed: Boolean,
    @ColumnInfo(name = "bill_session_id") val sessionId: ParliamentID?,
    @ColumnInfo(name = "bill_type_id") val typeId: String?,
): Parliamentdotuk,
    Dated,
    Commentable,
    Votable


data class MinimalBill(
    @ColumnInfo(name = "bill_$PARLIAMENTDOTUK") override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "bill_title") val title: String,
    @ColumnInfo(name = "bill_description") val description: String?,
    @ColumnInfo(name = "bill_date") override val date: LocalDate?,
): Dated, Parliamentdotuk


data class BillWithSessionAndType(
    @Embedded val bill: Bill,
    @Relation(parentColumn = "bill_session_id", entityColumn = "session_$PARLIAMENTDOTUK")
    val session: ParliamentarySession,

    @Relation(parentColumn = "bill_type_id", entityColumn = "billtype_name")
    val type: BillType
)


data class CompleteBill(
    @Embedded val bill: Bill,
    @Ignore val session: ParliamentarySession,
    @Ignore val type: BillType,
    @Ignore val publications: List<BillPublication>,
    @Ignore val sponsors: List<BillSponsorWithParty>,
    @Ignore val stages: List<BillStageWithSittings>,
)

data class CompleteBillBuilder(
    var bill: Bill? = null,
    var session: ParliamentarySession? = null,
    var type: BillType? = null,
    var publications: List<BillPublication>? = null,
    var sponsors: List<BillSponsorWithParty>? = null,
    var stages: List<BillStageWithSittings>? = null,
) {
    val isComplete: Boolean
        get() = allNotNull(bill, session, type, publications, sponsors, stages)

    fun toCompleteBill() = CompleteBill(
        bill!!,
        session!!,
        type!!,
        publications!!,
        sponsors!!,
        stages!!,
    )
}
