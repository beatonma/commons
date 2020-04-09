package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK


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
    @ColumnInfo(name = "billstage_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @ColumnInfo(name = "billstage_bill_$PARLIAMENTDOTUK") val billId: Int = 0,
    @ColumnInfo(name = "billstage_type") val type: String,
)


data class ApiBillStage(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: Int,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "sittings") val sittings: List<BillStageSitting>
) {
    fun toBillStage(billId: Int) = BillStage(
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
