package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import org.beatonma.commons.core.House
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import java.time.LocalDate


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
    @ColumnInfo(name = "house") val house: House,
    @ColumnInfo(name = "house_member_id", index = true) val memberId: Int,
    @ColumnInfo(name = "house_start") override val start: LocalDate,
    @ColumnInfo(name = "house_end") override val end: LocalDate?
): Named,
    Periodic {
    override val name get() = house.name
}
