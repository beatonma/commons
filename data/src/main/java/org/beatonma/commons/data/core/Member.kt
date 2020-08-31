package org.beatonma.commons.data.core

import androidx.room.Embedded
import androidx.room.Relation
import com.squareup.moshi.Json
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.snommoc.Contract

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

/**
 * Class for deserializing complete /member/profile/ api response.
 */
data class ApiCompleteMember(
    val profile: ApiMemberProfile,
    @field:Json(name = Contract.ADDRESS) val addresses: ApiAddresses,
    @field:Json(name = Contract.POSTS) val posts: ApiPosts,
    @field:Json(name = Contract.COMMITTEES) val committees: List<ApiCommittee>,
    @field:Json(name = Contract.HOUSES) val houses: List<ApiHouseMembership>,
    @field:Json(name = Contract.INTERESTS) val financialInterests: List<ApiFinancialInterest>,
    @field:Json(name = Contract.EXPERIENCES) val experiences: List<ApiExperience>,
    @field:Json(name = Contract.SUBJECTS) val topicsOfInterest: List<ApiTopicOfInterest>,
    @field:Json(name = Contract.CONSTITUENCIES) val constituencies: List<ApiHistoricalConstituency>,
    @field:Json(name = Contract.PARTIES) val parties: List<ApiPartyAssociation>,

    /**
     * API fields pending implementation:
     *  - speeches (maiden speech(es))
     */
)
