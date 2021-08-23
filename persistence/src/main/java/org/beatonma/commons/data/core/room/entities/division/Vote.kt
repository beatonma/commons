package org.beatonma.commons.data.core.room.entities.division

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.VoteType

//enum class VoteType {
//    AyeVote,
//    NoVote,
//    Abstains,
//    DidNotVote,
//    SuspendedOrExpelledVote,
//}

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


data class VoteWithDivision(
    @Embedded val vote: Vote,
    @Relation(parentColumn = "dvote_division_id", entityColumn = "division_$PARLIAMENTDOTUK", entity = Division::class)
    val division: Division
)
