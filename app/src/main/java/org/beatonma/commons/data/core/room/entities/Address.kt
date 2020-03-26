package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

sealed class Address

@Entity(
    primaryKeys = [
        "paddr_person_id",
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
    @ColumnInfo(name = "paddr_person_id") val personId: Int
): Address()

@Entity(tableName = "weblinks")
data class WebAddress(
    @field:Json(name = "url") @ColumnInfo(name = "waddr_url") @PrimaryKey val url: String,
    @field:Json(name = "description") @ColumnInfo(name = "waddr_description") val description: String,
    @ColumnInfo(name = "waddr_person_id") val personId: Int
)

/**
 * Used for deserializing api response
 */
data class Addresses(
    @Embedded @field:Json(name = "physical") val physical: List<PhysicalAddress>? = null,
    @Embedded @field:Json(name = "web") val web: List<WebAddress>? = null
): Address()
