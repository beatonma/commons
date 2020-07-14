package org.beatonma.commons.data.core.room.entities.division

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.network.retrofit.Contract

enum class VoteType {
    AyeVote,
    NoVote,
    Abstains,
    DidNotVote,
    SuspendedOrExpelledVote,
}

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Division::class,
            parentColumns = ["division_$PARLIAMENTDOTUK"],
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
data class Vote(
    @ColumnInfo(name = "dvote_division_id") val divisionId: ParliamentID,
    @ColumnInfo(name = "dvote_member_id") val memberId: ParliamentID,
    @ColumnInfo(name = "member_name") val memberName: String,
    @ColumnInfo(name = "vote") val voteType: VoteType,
    @ColumnInfo(name = "party_id") val partyId: ParliamentID?,
)

data class ApiVote(
    @field:Json(name = PARLIAMENTDOTUK) val memberId: ParliamentID,
    @field:Json(name = Contract.NAME) val memberName: String,
    @field:Json(name = Contract.VOTE) val voteType: VoteType,
    @field:Json(name = Contract.PARTY) val party: Party?,
) {
    fun toVote(divisionId: ParliamentID) = Vote(
            divisionId = divisionId,
            memberId = memberId,
            memberName = memberName,
            voteType = voteType,
            partyId = party?.parliamentdotuk
        )
}

data class VoteWithDivision(
    @Embedded val vote: Vote,
    @Relation(parentColumn = "dvote_division_id", entityColumn = "division_$PARLIAMENTDOTUK", entity = Division::class)
    val division: Division
)

data class ApiMemberVote(
    @field:Json(name = Contract.DIVISION) val division: Division,
    @field:Json(name = Contract.VOTE_TYPE) val voteType: VoteType,
) {
    fun toVote(memberId: ParliamentID) = Vote(
        memberId = memberId,
        divisionId = division.parliamentdotuk,
        voteType = voteType,
        memberName = "",
        partyId = 0
    )
}
