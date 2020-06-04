package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk

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
    @field:Json(name = "kml") @ColumnInfo(name = "boundary_kml") val kml: String,
    @field:Json(name = "area") @ColumnInfo(name = "boundary_area") val area: String?,
    @field:Json(name = "boundary_length") @ColumnInfo(name = "boundary_length") val boundaryLength: String?,
    @field:Json(name = "center_latitude") @ColumnInfo(name = "boundary_center_lat") val centerLat: String?,
    @field:Json(name = "center_longitude") @ColumnInfo(name = "boundary_center_long") val centerLong: String?,
): Parliamentdotuk {
    fun center(): LatLng? {
        val lat = centerLat?.toDoubleOrNull() ?: return null
        val long = centerLong?.toDoubleOrNull() ?: return null
        return LatLng(lat, long)
    }
}
