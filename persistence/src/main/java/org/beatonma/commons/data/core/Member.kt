package org.beatonma.commons.data.core

import androidx.room.Embedded
import androidx.room.Relation
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.allNotNull
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.CommitteeMemberWithChairs
import org.beatonma.commons.data.core.room.entities.member.Experience
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituencyWithElection
import org.beatonma.commons.data.core.room.entities.member.HouseMembership
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.PartyAssociationWithParty
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.data.core.room.entities.member.TopicOfInterest
import org.beatonma.commons.data.core.room.entities.member.WebAddress

data class CompleteMember(
    val profile: MemberProfile,
    val party: Party,
    val constituency: Constituency,
    val committees: List<CommitteeMemberWithChairs>,
    val addresses: List<PhysicalAddress>,
    val weblinks: List<WebAddress>,
    val posts: List<Post>,
    val experiences: List<Experience>,
    val financialInterests: List<FinancialInterest>,
    val houses: List<HouseMembership>,
    val topicsOfInterest: List<TopicOfInterest>,
    val historicConstituencies: List<HistoricalConstituencyWithElection>,
    val parties: List<PartyAssociationWithParty>,
)

data class CompleteMemberBuilder(
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
) {
    val isComplete
        get() = allNotNull(
            profile, party, constituency, committees, addresses, weblinks, posts, experiences,
            financialInterests, houses, topicsOfInterest, historicConstituencies, parties,
        )

    fun toCompleteMember() = CompleteMember(
        profile!!,
        party!!,
        constituency!!,
        committees!!,
        addresses!!,
        weblinks!!,
        posts!!,
        experiences!!,
        financialInterests!!,
        houses!!,
        topicsOfInterest!!,
        historicConstituencies!!,
        parties!!,
    )
}

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
