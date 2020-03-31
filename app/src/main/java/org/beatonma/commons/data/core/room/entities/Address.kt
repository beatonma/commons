package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

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
    @field:Json(name = "address") @ColumnInfo(name = "paddr_address") val address: String,
    @field:Json(name = "description") @ColumnInfo(name = "paddr_description") val description: String,
    @field:Json(name = "postcode") @ColumnInfo(name = "paddr_postcode") val postcode: String?,
    @field:Json(name = "phone") @ColumnInfo(name = "paddr_phone") val phone: String?,
    @field:Json(name = "fax") @ColumnInfo(name = "paddr_fax") val fax: String?,
    @field:Json(name = "email") @ColumnInfo(name = "paddr_email") val email: String?,
    @ColumnInfo(name = "paddr_member_id", index = true) val personId: Int
): Address()

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
    @field:Json(name = "url") @ColumnInfo(name = "waddr_url") val url: String,
    @field:Json(name = "description") @ColumnInfo(name = "waddr_description") val description: String,
    @ColumnInfo(name = "waddr_member_id", index = true) val personId: Int
)

/**
 * Used for deserializing api response
 */
data class ApiAddresses(
    @Embedded @field:Json(name = "physical") val physical: List<PhysicalAddress>,
    @Embedded @field:Json(name = "web") val web: List<WebAddress>
): Address()
