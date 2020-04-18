package org.beatonma.commons.data.core.room.entities.division

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.member.House

@Entity(
    tableName = "divisions",
)
data class Division(
    @ColumnInfo(name = "division_$PARLIAMENTDOTUK") @PrimaryKey val parliamentdotuk: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "house") val house: House,
    @ColumnInfo(name = "passed") val passed: Boolean,
    @ColumnInfo(name = "ayes") val ayes: Int,
    @ColumnInfo(name = "noes") val noes: Int,
    @ColumnInfo(name = "abstentions") val abstentions: Int,
    @ColumnInfo(name = "did_not_vote") val didNotVote: Int,
    @ColumnInfo(name = "non_eligible") val nonEligible: Int,
    @ColumnInfo(name = "suspended_or_expelled") val suspendedOrExpelled: Int,
    @ColumnInfo(name = "errors") val errors: Int,
    @ColumnInfo(name = "deferred_vote") val deferredVote: Boolean,
    @ColumnInfo(name = "whipped_vote") val whippedVote: Boolean?,  // L
)

data class ApiDivision(
    @field:Json(name = PARLIAMENTDOTUK) val parliamentdotuk: Int,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "house") val house: House,
    @field:Json(name = "passed") val passed: Boolean,
    @field:Json(name = "ayes") val ayes: Int,
    @field:Json(name = "noes") val noes: Int,
    @field:Json(name = "abstentions") val abstentions: Int,
    @field:Json(name = "did_not_vote") val didNotVote: Int,
    @field:Json(name = "non_eligible") val nonEligible: Int,
    @field:Json(name = "suspended_or_expelled") val suspendedOrExpelled: Int,
    @field:Json(name = "errors") val errors: Int,
    @field:Json(name = "deferred_vote") val deferredVote: Boolean,
    @field:Json(name = "whipped_vote") val whippedVote: Boolean?,
    @field:Json(name = "votes") val votes: List<ApiVote>,
) {
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
    val votes: List<Vote>
)
