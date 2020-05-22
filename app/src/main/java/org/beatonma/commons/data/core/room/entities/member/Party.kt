package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.Named
import org.beatonma.commons.data.core.Parliamentdotuk

@Entity(tableName = "parties")
data class Party(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "party_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") @ColumnInfo(name = "party_name") override val name: String
): Parliamentdotuk, Named
