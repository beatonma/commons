package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.MemberProfileWithPartyConstituency

/**
 * [parliamentdotuk] may be used to look up the member profile, but it may be null if the server
 * was unable to confirm the ID via name lookup so we can't rely on it as a foreign key.
 */
@Entity(
    tableName = "bill_sponsors",
)
data class BillSponsor(
    @ColumnInfo(name = "sponsor_name") @PrimaryKey override val name: String,
    @ColumnInfo(name = "sponsor_bill_id") val billId: ParliamentID,
    @ColumnInfo(name = "sponsor_profile_id") val profile: MemberProfile?,
): Named

data class BillSponsorWithProfile(
    @Embedded val sponsor: BillSponsor,

    @Relation(
        parentColumn = "sponsor_profile_id",
        entityColumn = PARLIAMENTDOTUK,
        entity = MemberProfile::class
    )
    val profile: MemberProfileWithPartyConstituency?,
) : Named {
    override val name: String get() = sponsor.name
}
