package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import java.time.LocalDate

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
    @ColumnInfo(name = "partyacc_start") override val start: LocalDate,
    @ColumnInfo(name = "partyacc_end") override val end: LocalDate?
): Periodic


data class PartyAssociationWithParty(
    @Embedded val partyAssocation: PartyAssociation,

    @Relation(parentColumn = "partyacc_party_id", entityColumn = "party_$PARLIAMENTDOTUK")
    val party: Party
): Named,
    Periodic {
    override val name: String get() = party.name
    override val start: LocalDate get() = partyAssocation.start
    override val end: LocalDate? get() = partyAssocation.end
}
