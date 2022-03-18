package org.beatonma.commons.data.core.room.entities.division

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.data.core.interfaces.Commentable
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Votable
import org.beatonma.commons.data.core.room.entities.member.Party
import java.time.LocalDate

@Entity(
    tableName = "commons_divisions",
)
data class CommonsDivisionData(
    @ColumnInfo(name = "division_id") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date") override val date: LocalDate,
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
) : Parliamentdotuk,
    Dated,
    Commentable,
    Votable

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CommonsDivisionData::class,
            parentColumns = ["division_id"],
            childColumns = ["dvote_division_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "dvote_division_id",
        "dvote_member_id",
    ],
    tableName = "division_votes",
)
data class CommonsDivisionVoteData(
    @ColumnInfo(name = "dvote_division_id") override val divisionId: ParliamentID,
    @ColumnInfo(name = "dvote_member_id") override val memberId: ParliamentID,
    @ColumnInfo(name = "dvote_member_name") override val memberName: String,
    @ColumnInfo(name = "dvote_vote") override val vote: VoteType,
    @ColumnInfo(name = "dvote_party_id") override val partyId: ParliamentID,
) : HouseVoteData<VoteType>


data class CommonsDivisionVote(
    @Embedded override val data: CommonsDivisionVoteData,

    @Relation(parentColumn = "dvote_party_id", entityColumn = "party_id")
    override val party: Party,
) : ResolvedHouseVote<VoteType>


data class CommonsDivision(
    @Embedded val data: CommonsDivisionData,
    @Relation(parentColumn = "division_id",
        entityColumn = "dvote_division_id",
        entity = CommonsDivisionVoteData::class)
    val votes: List<CommonsDivisionVote>,
) : HouseDivision<VoteType>
