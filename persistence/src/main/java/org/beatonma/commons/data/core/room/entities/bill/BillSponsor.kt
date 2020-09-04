package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.*
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.room.entities.member.Party

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


data class BillSponsorWithParty(
    @Embedded val sponsor: BillSponsor,

    @Relation(parentColumn = "sponsor_party_id", entityColumn = "party_$PARLIAMENTDOTUK")
    val party: Party?
)
