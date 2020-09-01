package org.beatonma.commons.data.core.room.entities.bill

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk

@Entity(
    tableName = "parliamentary_sessions"
)
data class ParliamentarySession(
    @ColumnInfo(name = "session_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "session_name") override val name: String,
): Parliamentdotuk,
    Named
