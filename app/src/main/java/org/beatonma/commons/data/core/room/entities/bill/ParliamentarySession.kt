package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.Parliamentdotuk
import org.beatonma.commons.data.core.Named

@Entity(
    tableName = "parliamentary_sessions"
)
data class ParliamentarySession(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "session_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") @ColumnInfo(name = "session_name") override val name: String,
): Parliamentdotuk, Named
