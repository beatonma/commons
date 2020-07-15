package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.network.retrofit.Contract
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = BillStage::class,
            parentColumns = ["billstage_$PARLIAMENTDOTUK"],
            childColumns = ["billstagesitting_bill_stage_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "bill_stage_sittings"
)
data class BillStageSitting(
    @ColumnInfo(name = "billstagesitting_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "billstagesitting_bill_stage_id") val billStageId: ParliamentID,
    @ColumnInfo(name = "billstagesitting_date") override val date: LocalDate,
    @ColumnInfo(name = "billstagesitting_formal") val isFormal: Boolean,
    @ColumnInfo(name = "billstagesitting_provisional") val isProvisional: Boolean,
): Parliamentdotuk,
    Dated


data class ApiBillStageSitting(
    @field:Json(name = Contract.PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.DATE) override val date: LocalDate,
    @field:Json(name = Contract.FORMAL) val isFormal: Boolean,
    @field:Json(name = Contract.PROVISIONAL) val isProvisional: Boolean,
): Parliamentdotuk,
    Dated {
    fun toBillStageSitting(billStageId: ParliamentID) = BillStageSitting(
        parliamentdotuk = parliamentdotuk,
        billStageId = billStageId,
        date = date,
        isFormal = isFormal,
        isProvisional = isProvisional
    )
}
