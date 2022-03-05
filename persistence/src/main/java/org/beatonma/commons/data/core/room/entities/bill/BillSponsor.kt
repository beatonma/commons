package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.member.MemberProfile

@Entity(tableName = "bill_sponsors")
data class BillSponsorData(
    @ColumnInfo(name = "billsponsor_id") @PrimaryKey val id: Int,
    @ColumnInfo(name = "billsponsor_bill_id") val billId: ParliamentID,
    @ColumnInfo(name = "billsponsor_member_id") val memberId: ParliamentID?,
    @Embedded val organisation: Organisation?,
)

data class BillSponsor(
    @ColumnInfo(name = "billsponsor_id") val id: Int,
    @Embedded val member: MemberProfile?,
    @Embedded val organisation: Organisation?,
)

data class Organisation(
    @ColumnInfo(name = "organisation_name") val name: String,
    @ColumnInfo(name = "organisation_url") val url: String,
)
