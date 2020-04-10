package org.beatonma.commons.data.core.room.entities.division

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK

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
    @ColumnInfo(name = "dvote_division_id") val divisionId: Int,
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "dvote_member_id") val memberId: Int,
    @field:Json(name = "name") @ColumnInfo(name = "member_name") val memberName: String,
    @field:Json(name = "vote") @ColumnInfo(name = "vote") val voteType: VoteType,
)

data class ApiVote(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = "dvote_member_id") val memberId: Int,
    @field:Json(name = "name") @ColumnInfo(name = "member_name") val memberName: String,
    @field:Json(name = "vote") @ColumnInfo(name = "vote") val voteType: VoteType,
)

data class VoteWithDivision(
    @Embedded val vote: Vote,
    @Relation(parentColumn = "dvote_division_id", entityColumn = "division_$PARLIAMENTDOTUK", entity = Division::class)
    val division: Division
)

data class ApiMemberVote(
    @field:Json(name = "division") val division: Division,
    @field:Json(name = "vote_type") val voteType: VoteType,
)
