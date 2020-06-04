package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Bill::class,
            parentColumns = ["bill_$PARLIAMENTDOTUK"],
            childColumns = ["billstage_bill_$PARLIAMENTDOTUK"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "bill_stages"
)
data class BillStage(
    @ColumnInfo(name = "billstage_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "billstage_bill_$PARLIAMENTDOTUK") val billId: ParliamentID = 0,
    @ColumnInfo(name = "billstage_type") val type: String,
): Parliamentdotuk


data class ApiBillStage(
    @field:Json(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "sittings") val sittings: List<BillStageSitting>
): Parliamentdotuk {
    fun toBillStage(billId: ParliamentID) = BillStage(
        parliamentdotuk = parliamentdotuk,
        billId = billId,
        type = type,
    )
}


data class BillStageWithSittings(
    @Embedded val stage: BillStage,
    @Relation(parentColumn = "billstage_$PARLIAMENTDOTUK", entityColumn = "billstagesitting_bill_stage_id")
    val sittings: List<BillStageSitting>
)
