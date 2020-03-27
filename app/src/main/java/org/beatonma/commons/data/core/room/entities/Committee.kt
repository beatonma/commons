package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

@Entity(
    primaryKeys = [
        "committee_$PARLIAMENTDOTUK",
        "committee_member_id"
    ],
    tableName = "committee_memberships"
)
data class CommitteeMembership(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "committee_$PARLIAMENTDOTUK") val parliamentdotuk: Int,
    @ColumnInfo(name = "committee_member_id") val memberId: Int,
    @field:Json(name = "name") @ColumnInfo(name = "committee_name") val name: String,
    @field:Json(name = "start") @ColumnInfo(name = "committee_start") val start: String?,
    @field:Json(name = "end") @ColumnInfo(name = "committee_end") val end: String?
)

// TODO handle committee chairship
