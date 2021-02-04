package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID

@Deprecated("Use zeitgeist")
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

@Deprecated("Use zeitgeist")
data class FeaturedBillWithBill(
    @Embedded val featured: FeaturedBill,
    @Relation(
        parentColumn = "featured_bill_id",
        entityColumn = "bill_$PARLIAMENTDOTUK",
        entity = Bill::class
    )
    val bill: MinimalBill,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Bill::class,
            parentColumns = ["bill_$PARLIAMENTDOTUK"],
            childColumns = ["zeitgeist_bill_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "zeitgeist_bills"
)
data class ZeitgeistBill(
    @ColumnInfo(name = "zeitgeist_bill_id") @PrimaryKey val billId: ParliamentID,
    @ColumnInfo(name = "zeitgeist_bill_reason") val reason: String? = null,
)

data class ResolvedZeitgeistBill(
    @Embedded val zeitgeistBill: ZeitgeistBill,
    @Relation(
        parentColumn = "zeitgeist_bill_id",
        entityColumn = "bill_$PARLIAMENTDOTUK",
        entity = Bill::class
    )
    val bill: Bill,
)
