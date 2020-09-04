package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
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
