package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

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
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "billstagesitting_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @ColumnInfo(name = "billstagesitting_bill_stage_id") var billStageId: Int,
    @field:Json(name = "date") @ColumnInfo(name = "billstagesitting_date") val date: String,
    @field:Json(name = "formal") @ColumnInfo(name = "billstagesitting_formal") val isFormal: Boolean,
    @field:Json(name = "provisional") @ColumnInfo(name = "billstagesitting_provisional") val isProvisional: Boolean,
)
