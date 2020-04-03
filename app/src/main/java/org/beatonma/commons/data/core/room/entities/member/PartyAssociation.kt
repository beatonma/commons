package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MemberProfile::class,
            parentColumns = [PARLIAMENTDOTUK],
            childColumns = ["partyacc_member_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Party::class,
            parentColumns = ["party_$PARLIAMENTDOTUK"],
            childColumns = ["partyacc_party_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "partyacc_member_id",
        "partyacc_party_id",
        "partyacc_start"
    ],
    tableName = "party_associations"
)
data class PartyAssociation(
    @ColumnInfo(name = "partyacc_member_id") val memberId: Int,
    @ColumnInfo(name = "partyacc_party_id") val partyId: Int,
    @ColumnInfo(name = "partyacc_start") val start: String,
    @ColumnInfo(name = "partyacc_end") val end: String?
)

data class PartyAssociationWithParty(
    @Embedded val partyAssocation: PartyAssociation,

    @Relation(parentColumn = "partyacc_party_id", entityColumn = "party_$PARLIAMENTDOTUK")
    val party: Party
)


data class ApiPartyAssociation(
    @field:Json(name = "party") val party: Party,
    @field:Json(name = "start") val start: String,
    @field:Json(name = "end") val end: String?
)