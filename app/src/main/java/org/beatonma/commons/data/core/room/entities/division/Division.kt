package org.beatonma.commons.data.core.room.entities.division

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Commentable
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Votable
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.social.SocialTargetType
import org.beatonma.commons.network.retrofit.Contract
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
    Votable {

    override fun getSocialContentTarget(): SocialTargetType = when(house) {
        House.commons -> SocialTargetType.division_commons
        House.lords -> SocialTargetType.division_lords
    }
}

data class ApiDivision(
    @field:Json(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String?,
    @field:Json(name = Contract.DATE) override val date: LocalDate,
    @field:Json(name = Contract.HOUSE) val house: House,
    @field:Json(name = Contract.PASSED) val passed: Boolean,
    @field:Json(name = Contract.AYES) val ayes: Int,
    @field:Json(name = Contract.NOES) val noes: Int,
    @field:Json(name = Contract.ABSTENTIONS) val abstentions: Int?,
    @field:Json(name = Contract.DID_NOT_VOTE) val didNotVote: Int?,
    @field:Json(name = Contract.NON_ELIGIBLE) val nonEligible: Int?,
    @field:Json(name = Contract.SUSPENDED_OR_EXPELLED) val suspendedOrExpelled: Int?,
    @field:Json(name = Contract.ERRORS) val errors: Int?,
    @field:Json(name = Contract.DEFERRED_VOTE) val deferredVote: Boolean,
    @field:Json(name = Contract.WHIPPED_VOTE) val whippedVote: Boolean?,
    @field:Json(name = Contract.VOTES) val votes: List<ApiVote>,
): Parliamentdotuk,
    Dated {
    fun toDivision() = Division(
        parliamentdotuk = parliamentdotuk,
        title = title,
        description = description,
        date = date,
        passed = passed,
        ayes = ayes,
        noes = noes,
        house = house,
        abstentions = abstentions,
        didNotVote = didNotVote,
        nonEligible = nonEligible,
        suspendedOrExpelled = suspendedOrExpelled,
        deferredVote = deferredVote,
        errors = errors,
        whippedVote = whippedVote,
    )
}

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
