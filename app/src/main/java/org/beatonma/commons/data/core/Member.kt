package org.beatonma.commons.data.core

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.squareup.moshi.Json
import org.beatonma.commons.data.core.room.entities.member.*

data class CompleteMember(
    @Embedded var profile: MemberProfile? = null,
    @Ignore val party: Party? = null,
    @Ignore val constituency: Constituency? = null,
    @Ignore val committees: List<CommitteeMemberWithChairs>? = null,
    @Ignore val addresses: List<PhysicalAddress>? = null,
    @Ignore val weblinks: List<WebAddress>? = null,
    @Ignore val posts: List<Post>? = null,
    @Ignore val experiences: List<Experience>? = null,
    @Ignore val financialInterests: List<FinancialInterest>? = null,
    @Ignore val houses: List<HouseMembership>? = null,
    @Ignore val topicsOfInterest: List<TopicOfInterest>? = null,
    @Ignore val historicConstituencies: List<HistoricalConstituencyWithElection>? = null,
    @Ignore val parties: List<PartyAssociationWithParty>? = null,
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
)

/**
 * Class for deserializing complete /member/profile/ api response.
 */
data class ApiCompleteMember(
    val profile: MemberProfile,
    @field:Json(name = "address") val addresses: ApiAddresses,
    @field:Json(name = "posts") val posts: ApiPosts,
    @field:Json(name = "committees") val committees: List<ApiCommittee>,
    @field:Json(name = "houses") val houses: List<HouseMembership>,
    @field:Json(name = "interests") val financialInterests: List<FinancialInterest>,
    @field:Json(name = "experiences") val experiences: List<Experience>,
    @field:Json(name = "subjects") val topicsOfInterest: List<TopicOfInterest>,
    @field:Json(name = "constituencies") val constituencies: List<ApiHistoricalConstituency>,
    @field:Json(name = "parties") val parties: List<ApiPartyAssociation>,

    /**
     * API fields pending implementation:
     *  - speeches (maiden speech(es))
     */
)
