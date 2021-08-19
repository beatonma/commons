package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk

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
    @ColumnInfo(name = "bill_url") val url: String? = null,
    @ColumnInfo(name = "bill_content_type") val contentType: String? = null,
    @ColumnInfo(name = "bill_content_length") val contentLength: Int? = null,
) : Parliamentdotuk


data class BillPublicationBasic(
    @ColumnInfo(name = "bill_pub_parliamentdotuk") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "bill_pub_bill_id") val billId: ParliamentID,
    @ColumnInfo(name = "bill_pub_title") val title: String,
) : Parliamentdotuk

/**
 * Additional detail for a [BillPublication], currently retrieved separately from remote sources.
 */
@Entity
data class BillPublicationDetail(
    @ColumnInfo(name = "bill_pub_parliamentdotuk") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "bill_pub_bill_id") val billId: ParliamentID,
    @ColumnInfo(name = "bill_url") val url: String,
    @ColumnInfo(name = "bill_content_type") val contentType: String,
    @ColumnInfo(name = "bill_content_length") val contentLength: Int,
): Parliamentdotuk
