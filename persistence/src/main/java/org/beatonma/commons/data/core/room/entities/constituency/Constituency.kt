package org.beatonma.commons.data.core.room.entities.constituency

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.core.room.entities.member.BasicProfileWithParty
import java.time.LocalDate

@Entity(
    tableName = "constituencies"
)
data class Constituency(
    @ColumnInfo(name = "constituency_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "constituency_name") override val name: String,
    @ColumnInfo(name = "constituency_start") override val start: LocalDate? = null,
    @ColumnInfo(name = "constituency_end") override val end: LocalDate? = null,
): Parliamentdotuk,
    Named,
    Periodic

data class ConstituencyWithBoundary(
    @Embedded
    val constituency: Constituency,

    @Relation(parentColumn = "constituency_$PARLIAMENTDOTUK",
        entityColumn = "boundary_constituency_id")
    val boundary: ConstituencyBoundary?,
)

data class CompleteConstituency(
    var constituency: Constituency? = null,
    var member: BasicProfileWithParty? = null,
    var electionResults: List<ConstituencyResultWithDetails>? = null,
    var boundary: ConstituencyBoundary? = null,
)

val NoConstituency = Constituency(-1, "")
