package org.beatonma.commons.data.core.room.entities.division

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.House
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Commentable
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Votable
import org.beatonma.commons.data.core.room.entities.member.Party
import java.time.LocalDate

@Entity(
    tableName = "divisions",
)
data class Division(
    @ColumnInfo(name = "division_$PARLIAMENTDOTUK") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?, // Lords only
    @ColumnInfo(name = "date") override val date: LocalDate,
    @ColumnInfo(name = "house") val house: House,
    @ColumnInfo(name = "passed") val passed: Boolean,
    @ColumnInfo(name = "ayes") val ayes: Int,
    @ColumnInfo(name = "noes") val noes: Int,
    @ColumnInfo(name = "abstentions") val abstentions: Int?,  // Commons only
    @ColumnInfo(name = "did_not_vote") val didNotVote: Int?,  // Commons only
    @ColumnInfo(name = "non_eligible") val nonEligible: Int?,  // Commons only
    @ColumnInfo(name = "suspended_or_expelled") val suspendedOrExpelled: Int?,  // Commons only
    @ColumnInfo(name = "errors") val errors: Int?,  // Commons only
    @ColumnInfo(name = "deferred_vote") val deferredVote: Boolean,
    @ColumnInfo(name = "whipped_vote") val whippedVote: Boolean?,  // Lords only
): Parliamentdotuk,
    Dated,
    Commentable,
    Votable


data class DivisionWithVotes(
    @Embedded val division: Division,
    @Relation(parentColumn = "division_$PARLIAMENTDOTUK", entityColumn = "dvote_division_id", entity = Vote::class)
    val votes: List<VoteWithParty>
)


data class VoteWithParty(
    @Embedded val vote: Vote,
    @Relation(parentColumn = "party_id", entityColumn = "party_$PARLIAMENTDOTUK")
    val party: Party?
)
