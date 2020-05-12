package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID

/**
 * [parliamentdotuk] may be used to look up the member profile, but it may be null if the server
 * was unable to confirm the ID via name lookup so we can't rely on it as a foreign key.
 */
@Entity(
    tableName = "bill_sponsors"
)
data class BillSponsor(
    @field:Json(name = "name") @ColumnInfo(name = "sponsor_name") @PrimaryKey val name: String,
    @ColumnInfo(name = "sponsor_bill_id") val billId: ParliamentID,
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "sponsor_$PARLIAMENTDOTUK") val parliamentdotuk: ParliamentID?,
)
