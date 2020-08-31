package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.snommoc.Contract

sealed class Address

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
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


data class ApiPhysicalAddress(
    @field:Json(name = Contract.ADDRESS) val address: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String,
    @field:Json(name = Contract.POSTCODE) val postcode: String?,
    @field:Json(name = Contract.PHONE) val phone: String?,
    @field:Json(name = Contract.FAX) val fax: String?,
    @field:Json(name = Contract.EMAIL) val email: String?,
) {
    fun toPhysicalAddress(memberId: ParliamentID) = PhysicalAddress(
        memberId = memberId,
        address = address,
        description = description,
        postcode = postcode,
        phone = phone,
        fax = fax,
        email = email
    )
}


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
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
)


data class ApiWebAddress(
    @field:Json(name = Contract.URL) val url: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String,
) {
    fun toWebAddress(memberId: ParliamentID) = WebAddress(
        url = url,
        description = description,
        memberId = memberId
    )
}


data class ApiAddresses(
    @field:Json(name = Contract.PHYSICAL) val physical: List<ApiPhysicalAddress>,
    @field:Json(name = Contract.WEB) val web: List<ApiWebAddress>
)
