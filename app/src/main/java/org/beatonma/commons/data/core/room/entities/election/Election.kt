package org.beatonma.commons.data.core.room.entities.election

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.Dated
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.Parliamentdotuk
import org.beatonma.commons.data.core.Named
import java.util.*

@Entity(
    tableName = "elections"
)
data class Election(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "election_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") @ColumnInfo(name = "election_name") override val name: String,
    @field:Json(name = "date") @ColumnInfo(name = "election_date") override val date: Date,
    @field:Json(name = "election_type") @ColumnInfo(name = "election_type") val electionType: String
): Parliamentdotuk, Named, Dated
