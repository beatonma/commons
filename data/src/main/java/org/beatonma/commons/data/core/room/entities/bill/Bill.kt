package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Commentable
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Votable
import org.beatonma.commons.data.core.social.SocialTargetType
import org.beatonma.commons.snommoc.Contract
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
    Votable {
    override fun getSocialContentTarget(): SocialTargetType = SocialTargetType.bill
}

data class ApiBill(
    @field:Json(name = Contract.PARLIAMENTDOTUK) @PrimaryKey override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String?,
    @field:Json(name = Contract.ACT_NAME) val actName: String?,
    @field:Json(name = Contract.LABEL) val label: String?,
    @field:Json(name = Contract.HOMEPAGE) val homepage: String?,
    @field:Json(name = Contract.DATE) override val date: LocalDate,
    @field:Json(name = Contract.BALLOT_NUMBER) val ballotNumber: Int?,
    @field:Json(name = Contract.BILL_CHAPTER) val billChapter: String?,
    @field:Json(name = Contract.IS_PRIVATE) val isPrivate: Boolean = false,
    @field:Json(name = Contract.IS_MONEY_BILL) val isMoneyBill: Boolean = false,
    @field:Json(name = Contract.PUBLIC_INVOLVEMENT_ALLOWED) val publicInvolvementAllowed: Boolean = false,
    @field:Json(name = Contract.PUBLICATIONS) val publications: List<ApiBillPublication> = listOf(),
    @field:Json(name = Contract.SESSION) val session: ParliamentarySession?,
    @field:Json(name = Contract.TYPE) val type: BillType?,
    @field:Json(name = Contract.SPONSORS) @Ignore val sponsors: List<ApiBillSponsor> = listOf(),
    @field:Json(name = Contract.STAGES) @Ignore val stages: List<ApiBillStage> = listOf(),
): Parliamentdotuk,
    Dated {
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
    @Embedded var bill: Bill? = null,
    @Ignore var session: ParliamentarySession? = null,
    @Ignore var type: BillType? = null,
    @Ignore var publications: List<BillPublication>? = null,
    @Ignore var sponsors: List<BillSponsorWithParty>? = null,
    @Ignore var stages: List<BillStageWithSittings>? = null,
)
