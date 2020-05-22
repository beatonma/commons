package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.Dated
import org.beatonma.commons.data.core.Parliamentdotuk
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
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "billstagesitting_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "billstagesitting_bill_stage_id") val billStageId: ParliamentID,
    @field:Json(name = "date") @ColumnInfo(name = "billstagesitting_date") override val date: LocalDate,
    @field:Json(name = "formal") @ColumnInfo(name = "billstagesitting_formal") val isFormal: Boolean,
    @field:Json(name = "provisional") @ColumnInfo(name = "billstagesitting_provisional") val isProvisional: Boolean,
): Parliamentdotuk,
    Dated
