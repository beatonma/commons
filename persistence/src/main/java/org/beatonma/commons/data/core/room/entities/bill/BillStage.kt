package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
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
    @ColumnInfo(name = "billstage_bill_$PARLIAMENTDOTUK") val billId: ParliamentID,
    @ColumnInfo(name = "billstage_type") val type: String,
): Parliamentdotuk



data class BillStageWithSittings(
    @Embedded val stage: BillStage,
    @Relation(parentColumn = "billstage_$PARLIAMENTDOTUK", entityColumn = "billstagesitting_bill_stage_id")
    val sittings: List<BillStageSitting>
)
