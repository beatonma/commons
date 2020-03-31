package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["house_member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "house",
        "house_start",
        "house_member_id"
    ],
    tableName = "house_memberships"
)
data class HouseMembership(
    @field:Json(name = "house") @ColumnInfo(name = "house") val house: House,
    @ColumnInfo(name = "house_member_id", index = true) val memberId: Int,
    @field:Json(name = "start") @ColumnInfo(name = "house_start") val start: String,
    @field:Json(name = "end") @ColumnInfo(name = "house_end") val end: String?
) {
    enum class House {
        Commons,
        Lords
    }
}
