package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID

private const val TAG = "BillPublication"


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Bill::class,
            parentColumns = ["bill_$PARLIAMENTDOTUK"],
            childColumns = ["bill_pub_bill_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "bill_publications"
)
data class BillPublication(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "bill_pub_parliamentdotuk") @PrimaryKey val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "bill_pub_bill_id") val billId: ParliamentID,
    @field:Json(name = "title") @ColumnInfo(name = "bill_pub_title") val title: String,
)
