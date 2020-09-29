package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate


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

data class ApiMemberProfile(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.PARTY)
    val party: ApiParty,  // Use Party object for api response, serialized to id for storage
    @field:Json(name = Contract.CONSTITUENCY)
    val constituency: ApiConstituencyMinimal?,  // Use Constituency object for api response, serialized to id for storage
    @field:Json(name = Contract.ACTIVE) val active: Boolean? = null,
    @field:Json(name = Contract.IS_MP) val isMp: Boolean? = null,
    @field:Json(name = Contract.IS_LORD) val isLord: Boolean? = null,
    @field:Json(name = Contract.AGE) val age: Int? = null,
    @field:Json(name = Contract.DATE_OF_BIRTH) val dateOfBirth: LocalDate? = null,
    @field:Json(name = Contract.DATE_OF_DEATH) val dateOfDeath: LocalDate? = null,
    @field:Json(name = Contract.GENDER) val gender: String? = null,
    @field:Json(name = Contract.PLACE_OF_BIRTH) val placeOfBirth: ApiTown? = null,
    @field:Json(name = Contract.PORTRAIT) val portraitUrl: String? = null,
    @field:Json(name = Contract.CURRENT_POST) val currentPost: String? = null,
)

data class ApiHouseMembership(
    @field:Json(name = Contract.HOUSE) val house: House,
    @field:Json(name = Contract.START) val start: LocalDate,
    @field:Json(name = Contract.END) val end: LocalDate?,
)

data class ApiTown(
    @field:Json(name = Contract.TOWN) val town: String,
    @field:Json(name = Contract.COUNTRY) val country: String = "UK",
)

data class ApiAddresses(
    @field:Json(name = Contract.PHYSICAL) val physical: List<ApiPhysicalAddress>,
    @field:Json(name = Contract.WEB) val web: List<ApiWebAddress>,
)

data class ApiPhysicalAddress(
    @field:Json(name = Contract.ADDRESS) val address: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String,
    @field:Json(name = Contract.POSTCODE) val postcode: String?,
    @field:Json(name = Contract.PHONE) val phone: String?,
    @field:Json(name = Contract.FAX) val fax: String?,
    @field:Json(name = Contract.EMAIL) val email: String?,
)

data class ApiWebAddress(
    @field:Json(name = Contract.URL) val url: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String,
)


data class ApiPost(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.START) val start: LocalDate?,
    @field:Json(name = Contract.END) val end: LocalDate?
)


data class ApiPosts(
    @field:Json(name = Contract.GOVERNMENTAL) val governmental: List<ApiPost>,
    @field:Json(name = Contract.PARLIAMENTARY) val parliamentary: List<ApiPost>,
    @field:Json(name = Contract.OPPOSITION) val opposition: List<ApiPost>
)


data class ApiCommittee(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.START) val start: LocalDate?,
    @field:Json(name = Contract.END) val end: LocalDate?,
    @field:Json(name = Contract.CHAIR) val chairs: List<ApiCommitteeChairship>,
)

data class ApiCommitteeChairship(
    @field:Json(name = Contract.START) val start: LocalDate,
    @field:Json(name = Contract.END) val end: LocalDate?,
)

data class ApiPartyAssociation(
    @field:Json(name = Contract.PARTY) val party: ApiParty,
    @field:Json(name = Contract.START) val start: LocalDate,
    @field:Json(name = Contract.END) val end: LocalDate?
)

data class ApiFinancialInterest(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.CATEGORY) val category: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String,
    @field:Json(name = Contract.CREATED) val dateCreated: LocalDate?,
    @field:Json(name = Contract.AMENDED) val dateAmended: LocalDate?,
    @field:Json(name = Contract.DELETED) val dateDeleted: LocalDate?,
    @field:Json(name = Contract.REGISTERED_LATE) val registeredLate: Boolean
)

data class ApiExperience(
    @field:Json(name = Contract.CATEGORY) val category: String,
    @field:Json(name = Contract.ORGANISATION) val organisation: String?,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.START) val start: LocalDate?,
    @field:Json(name = Contract.END) val end: LocalDate?
)

data class ApiTopicOfInterest(
    @field:Json(name = Contract.CATEGORY) val category: String,
    @field:Json(name = Contract.SUBJECT) val topic: String  // Either a comma- or semicolon-separated list
)

data class ApiHistoricalConstituency(
    @field:Json(name = Contract.CONSTITUENCY) val constituency: ApiConstituencyMinimal,
    @field:Json(name = Contract.START) val start: LocalDate,
    @field:Json(name = Contract.END) val end: LocalDate?,
    @field:Json(name = Contract.ELECTION) val election: ApiElection
)
