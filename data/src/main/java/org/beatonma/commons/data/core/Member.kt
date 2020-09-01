package org.beatonma.commons.data.core

import androidx.room.Embedded
import androidx.room.Relation
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.*

data class CompleteMember(
    var profile: MemberProfile? = null,
    var party: Party? = null,
    var constituency: Constituency? = null,
    var committees: List<CommitteeMemberWithChairs>? = null,
    var addresses: List<PhysicalAddress>? = null,
    var weblinks: List<WebAddress>? = null,
    var posts: List<Post>? = null,
    var experiences: List<Experience>? = null,
    var financialInterests: List<FinancialInterest>? = null,
    var houses: List<HouseMembership>? = null,
    var topicsOfInterest: List<TopicOfInterest>? = null,
    var historicConstituencies: List<HistoricalConstituencyWithElection>? = null,
    var parties: List<PartyAssociationWithParty>? = null,
)

data class MinimalMember(
    @Embedded val profile: MemberProfile,

    @Relation(
        parentColumn = "party_id",
        entityColumn = "party_parliamentdotuk",
        entity = Party::class)
    val party: Party,

    @Relation(
        parentColumn = "constituency_id",
        entityColumn = "constituency_parliamentdotuk",
        entity = Constituency::class)
    val constituency: Constituency?,
): Parliamentdotuk {
    override val parliamentdotuk: ParliamentID get() = profile.parliamentdotuk
}
