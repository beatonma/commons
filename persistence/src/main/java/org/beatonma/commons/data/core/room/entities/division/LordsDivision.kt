package org.beatonma.commons.data.core.room.entities.division

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.House
import org.beatonma.commons.core.LordsVoteType
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Votable
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import java.time.LocalDate

@Entity(
    tableName = "lords_divisions",
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = ["member_id"],
            childColumns = ["ldivision_sponsor_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class LordsDivisionData(
    @ColumnInfo(name = "ldivision_id") @PrimaryKey override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "ldivision_title") val title: String,
    @ColumnInfo(name = "ldivision_date") override val date: LocalDate,
    @ColumnInfo(name = "ldivision_description") val description: String,
    @ColumnInfo(name = "ldivision_house") val house: House,
    @ColumnInfo(name = "ldivision_sponsor_id") val sponsorId: ParliamentID?,
    @ColumnInfo(name = "ldivision_passed") val passed: Boolean,
    @ColumnInfo(name = "ldivision_whipped_vote") val whippedVote: Boolean,
    @ColumnInfo(name = "ldivision_ayes") val ayes: Int,
    @ColumnInfo(name = "ldivision_noes") val noes: Int,
) : Parliamentdotuk, Dated, Votable

@Entity(
    tableName = "lords_division_votes",
    primaryKeys = ["ldivision_vote_division_id", "ldivision_vote_member_id"],
    foreignKeys = [
        ForeignKey(
            entity = LordsDivisionData::class,
            parentColumns = ["ldivision_id"],
            childColumns = ["ldivision_vote_division_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class LordsDivisionVoteData(
    @ColumnInfo(name = "ldivision_vote_division_id") override val divisionId: ParliamentID,
    @ColumnInfo(name = "ldivision_vote_member_id") override val memberId: ParliamentID,
    @ColumnInfo(name = "ldivision_vote_member_name") override val memberName: String,
    @ColumnInfo(name = "ldivision_vote_party_id") override val partyId: ParliamentID,
    @ColumnInfo(name = "ldivision_vote_type") override val vote: LordsVoteType,
) : HouseVoteData<LordsVoteType>

data class LordsDivisionVote(
    @Embedded
    override val data: LordsDivisionVoteData,

    @Relation(parentColumn = "ldivision_vote_party_id", entityColumn = "party_id")
    override val party: Party,
) : ResolvedHouseVote<LordsVoteType>

data class LordsDivision(
    @Embedded
    val data: LordsDivisionData,

    @Relation(
        parentColumn = "ldivision_id",
        entityColumn = "ldivision_vote_division_id",
        entity = LordsDivisionVoteData::class,
    )
    val votes: List<LordsDivisionVote>,
) : HouseDivision<LordsVoteType>
