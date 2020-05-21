package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.Named
import org.beatonma.commons.data.core.Periodic
import java.util.*

enum class House {
    Commons,
    Lords
}


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
    @field:Json(name = "start") @ColumnInfo(name = "house_start") override val start: Date,
    @field:Json(name = "end") @ColumnInfo(name = "house_end") override val end: Date?
): Named, Periodic {
    override val name get() = house.name
}
