package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.snommoc.Contract

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
    @ColumnInfo(name = "bill_pub_parliamentdotuk") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "bill_pub_bill_id") val billId: ParliamentID,
    @ColumnInfo(name = "bill_pub_title") val title: String,
) : Parliamentdotuk


data class ApiBillPublication(
    @field:Json(name = Contract.PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
) : Parliamentdotuk {
    fun toBillPublication(billId: ParliamentID) = BillPublication(
        parliamentdotuk = parliamentdotuk,
        title = title,
        billId = billId
    )
}
