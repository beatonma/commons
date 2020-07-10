package org.beatonma.commons.data.core.room.entities.member

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.squareup.moshi.Json
import org.beatonma.commons.R
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.kotlin.extensions.stringCompat
import java.time.LocalDate

/**
 * Names are lowercase so they can be used with Navigation Components deepLink
 * as part of the URL path.
 */
enum class House {
    commons,
    lords,
    ;
    fun description(context: Context) = when(this) {
        commons -> context.stringCompat(R.string.house_of_commons)
        lords -> context.stringCompat(R.string.house_of_lords)
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
    @field:Json(name = "house") @ColumnInfo(name = "house") val house: House,
    @ColumnInfo(name = "house_member_id", index = true) val memberId: Int,
    @field:Json(name = "start") @ColumnInfo(name = "house_start") override val start: LocalDate,
    @field:Json(name = "end") @ColumnInfo(name = "house_end") override val end: LocalDate?
): Named,
    Periodic {
    override val name get() = house.name
}
