package org.beatonma.commons.data.core

import androidx.room.Embedded
import androidx.room.Relation
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.*

data class Member(
    @Embedded val profile: MemberProfile
)

data class CompleteMember(
    @Embedded val profile: MemberProfile,

    @Relation(parentColumn = "party_id", entityColumn = "party_parliamentdotuk")
    val party: Party,

    @Relation(parentColumn = "constituency_id", entityColumn = "constituency_parliamentdotuk")
    val constituency: Constituency?,

    @Relation(parentColumn = PARLIAMENTDOTUK, entityColumn = "committee_member_id", entity = CommitteeMembership::class)
    val committees: List<CommitteeMemberWithChairs>,

    @Relation(parentColumn = PARLIAMENTDOTUK, entityColumn = "paddr_member_id")
    val addresses: List<PhysicalAddress>,
    
    @Relation(parentColumn = PARLIAMENTDOTUK, entityColumn = "waddr_member_id")
    val weblinks: List<WebAddress>,

    @Relation(parentColumn = PARLIAMENTDOTUK, entityColumn = "post_member_id")
    val posts: List<Post>,

    @Relation(parentColumn = PARLIAMENTDOTUK, entityColumn = "experience_member_id")
    val experiences: List<Experience>,

    @Relation(parentColumn = PARLIAMENTDOTUK, entityColumn = "interest_member_id")
    val financialInterests: List<FinancialInterest>,

    @Relation(parentColumn = PARLIAMENTDOTUK, entityColumn= "house_member_id")
    val houses: List<HouseMembership>,

    @Relation(parentColumn = PARLIAMENTDOTUK, entityColumn="topic_member_id")
    val topicsOfInterest: List<TopicOfInterest>
)


data class MinimalMember(
    @Embedded val profile: MemberProfile,

    @Relation(parentColumn = "party_id", entityColumn = "party_parliamentdotuk", entity=Party::class) val party: Party,
    @Relation(parentColumn = "constituency_id", entityColumn = "constituency_parliamentdotuk", entity=Constituency::class)
    val constituency: Constituency?
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
    @field:Json(name = "subjects") val topicsOfInterest: List<TopicOfInterest>

    /**
     * API fields pending implementation:
     *  - constituencies (historical associations)
     *  - speeches (maiden speech(es))
     *  - parties (historical associations)
     */
)
