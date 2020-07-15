package org.beatonma.commons.data.core

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.squareup.moshi.Json
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.network.retrofit.Contract

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
): Parliamentdotuk {
    override val parliamentdotuk: ParliamentID get() = profile.parliamentdotuk
}

/**
 * Class for deserializing complete /member/profile/ api response.
 */
data class ApiCompleteMember(
    val profile: MemberProfile,
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
