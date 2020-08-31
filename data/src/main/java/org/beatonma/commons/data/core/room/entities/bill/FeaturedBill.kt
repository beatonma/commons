package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.*
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Bill::class,
            parentColumns = ["bill_$PARLIAMENTDOTUK"],
            childColumns = ["featured_bill_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "featured_bills"
)
data class FeaturedBill(
    @ColumnInfo(name = "featured_bill_id") @PrimaryKey val billId: ParliamentID,
    @ColumnInfo(name = "featured_about") val about: String? = null
)


data class FeaturedBillWithBill(
    @Embedded val featured: FeaturedBill,
    @Relation(
        parentColumn = "featured_bill_id",
        entityColumn = "bill_$PARLIAMENTDOTUK",
        entity = Bill::class
    )
    val bill: MinimalBill
)
