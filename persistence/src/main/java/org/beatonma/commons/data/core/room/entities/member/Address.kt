package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

sealed class Address

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = ["member_id"],
            childColumns = ["paddr_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "paddr_member_id",
        "paddr_address"
    ],
    tableName = "physical_addresses"
)
data class PhysicalAddress(
    @ColumnInfo(name = "paddr_address") val address: String,
    @ColumnInfo(name = "paddr_description") val description: String,
    @ColumnInfo(name = "paddr_postcode") val postcode: String?,
    @ColumnInfo(name = "paddr_phone") val phone: String?,
    @ColumnInfo(name = "paddr_fax") val fax: String?,
    @ColumnInfo(name = "paddr_email") val email: String?,
    @ColumnInfo(name = "paddr_member_id", index = true) val memberId: Int,
): Address()



@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = ["member_id"],
            childColumns = ["waddr_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "waddr_url",
        "waddr_member_id"
    ],
    tableName = "weblinks"
)
data class WebAddress(
    @ColumnInfo(name = "waddr_url") val url: String,
    @ColumnInfo(name = "waddr_description") val description: String,
    @ColumnInfo(name = "waddr_member_id", index = true) val memberId: Int
): Address()
