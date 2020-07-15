package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.network.retrofit.Contract

/**
 * [parliamentdotuk] may be used to look up the member profile, but it may be null if the server
 * was unable to confirm the ID via name lookup so we can't rely on it as a foreign key.
 */
@Entity(
    tableName = "bill_sponsors"
)
data class BillSponsor(
    @ColumnInfo(name = "sponsor_name") @PrimaryKey override val name: String,
    @ColumnInfo(name = "sponsor_bill_id") val billId: ParliamentID,
    @ColumnInfo(name = "sponsor_$PARLIAMENTDOTUK") val parliamentdotuk: ParliamentID?,
    @ColumnInfo(name = "sponsor_party_id") val partyId: ParliamentID?,
): Named


data class ApiBillSponsor(
    @field:Json(name = Contract.NAME) override val name: String,
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID?,
    @field:Json(name = Contract.PARTY) val party: Party?
): Named {
    fun toBillSponsor(billId: ParliamentID) = BillSponsor(
        name = name,
        billId = billId,
        parliamentdotuk = parliamentdotuk,
        partyId = party?.parliamentdotuk
    )
}
