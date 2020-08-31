package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.snommoc.Contract

private const val TAG = "ConstituencyBoundary"

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Constituency::class,
            parentColumns = ["constituency_$PARLIAMENTDOTUK"],
            childColumns = ["boundary_constituency_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "constituency_boundaries"
)
data class ConstituencyBoundary(
    @ColumnInfo(name = "boundary_constituency_id") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "boundary_kml") val kml: String,
    @ColumnInfo(name = "boundary_area") val area: String?,
    @ColumnInfo(name = "boundary_length") val boundaryLength: String?,
    @ColumnInfo(name = "boundary_center_lat") val centerLat: String?,
    @ColumnInfo(name = "boundary_center_long") val centerLong: String?,
): Parliamentdotuk {
//    fun center(): LatLng? {
//        val lat = centerLat?.toDoubleOrNull() ?: return null
//        val long = centerLong?.toDoubleOrNull() ?: return null
//        return LatLng(lat, long)
//    }
}


data class ApiConstituencyBoundary(
    @field:Json(name = Contract.KML) val kml: String,
    @field:Json(name = Contract.AREA) val area: String?,
    @field:Json(name = Contract.BOUNDARY_LENGTH) val boundaryLength: String?,
    @field:Json(name = Contract.CENTER_LATITUDE) val centerLat: String?,
    @field:Json(name = Contract.CENTER_LONGITUDE) val centerLong: String?,
) {
    fun toConstituencyBoundary(constituencyId: ParliamentID) = ConstituencyBoundary(
        parliamentdotuk = constituencyId,
        kml = kml,
        area = area,
        boundaryLength = boundaryLength,
        centerLat = centerLat,
        centerLong = centerLong
    )
}
