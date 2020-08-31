package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

/**
 * Names are lowercase so they can be used with Navigation Components deepLink
 * as part of the URL path.
 */
enum class House {
    commons,
    lords,
    ;

    fun otherPlace() = when (this) {
        lords -> commons
        commons -> lords
    }
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
    @ColumnInfo(name = "house") val house: House,
    @ColumnInfo(name = "house_member_id", index = true) val memberId: Int,
    @ColumnInfo(name = "house_start") override val start: LocalDate,
    @ColumnInfo(name = "house_end") override val end: LocalDate?
): Named,
    Periodic {
    override val name get() = house.name
}


data class ApiHouseMembership(
    @field:Json(name = Contract.HOUSE) val house: House,
    @field:Json(name = Contract.START) val start: LocalDate,
    @field:Json(name = Contract.END) val end: LocalDate?
) {
    fun toHouseMembership(memberId: ParliamentID) = HouseMembership(
        house = house,
        memberId = memberId,
        start = start,
        end = end
    )
}
