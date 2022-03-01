package org.beatonma.commons.data.core.room.entities.election

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import java.time.LocalDate

@Entity(
    tableName = "elections"
)
data class Election(
    @ColumnInfo(name = "election_id") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "election_name") override val name: String,
    @ColumnInfo(name = "election_date") override val date: LocalDate,
    @ColumnInfo(name = "election_type") val electionType: String,
): Parliamentdotuk,
    Named,
    Dated
