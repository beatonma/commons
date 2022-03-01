package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk

private const val TAG = "ConstituencyBoundary"

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Constituency::class,
            parentColumns = ["constituency_id"],
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
): Parliamentdotuk
