package org.beatonma.commons.app.memberprofile

import androidx.annotation.VisibleForTesting
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.Snippet
import org.beatonma.commons.app.ui.SnippetGeneratorAndroidViewModel
import org.beatonma.commons.app.ui.data.WeblinkData
import org.beatonma.commons.app.ui.toSnippets
import org.beatonma.commons.data.LiveDataIoResultList
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.core.interfaces.compressConsecutiveItems
import org.beatonma.commons.data.core.repository.MemberRepository
import org.beatonma.commons.data.core.room.entities.division.VoteWithDivision
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.kotlin.extensions.dateRange
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.kotlin.extensions.openUrl
import org.beatonma.commons.kotlin.extensions.stringCompat
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "MemberProfViewModel"

class MemberProfileViewModel
@Inject constructor(
    private val repository: MemberRepository,
    application: CommonsApplication,
) : SnippetGeneratorAndroidViewModel<CompleteMember>(application) {

    lateinit var memberVoteLiveData: LiveDataIoResultList<VoteWithDivision>


    fun forMember(parliamentdotuk: ParliamentID) {
        liveData = repository.observeMember(parliamentdotuk)
        memberVoteLiveData = repository.observeCommonsVotesForMember(parliamentdotuk)
        observe()
    }

    override suspend fun generateSnippets(data: CompleteMember): List<Snippet> = listOfNotNull(
        *profileSnippets(data.profile),
        *webAddressSnippets(data.weblinks),
        *postSnippets(data.posts),
        *committeeMembershipSnippets(data.committees),
        *houseMembershipSnippets(data.houses),
        *financialInterestSnippets(data.financialInterests),
        *experienceSnippets(data.experiences),
        *topicOfInterestSnippets(data.topicsOfInterest)
    ).shuffled()

    private fun profileSnippets(profile: MemberProfile?): Array<Snippet> {
        profile ?: return arrayOf()
        return listOfNotNull(
            birthSnippet(profile)
        ).toTypedArray()
    }

    private fun birthSnippet(profile: MemberProfile): Snippet? {
        val dob: String? = profile.dateOfBirth?.formatted()
        val place: Town? = profile.placeOfBirth
        val age: Int? = profile.age

        return when {
            dob != null && place != null && age != null -> Snippet(
                stringCompat(R.string.snippet_birth_date, dob),
                stringCompat(R.string.snippet_birth_place, place.town, place.country),
                subtitle = stringCompat(R.string.snippet_birth_age, age))

            dob != null && age != null -> Snippet(
                stringCompat(R.string.snippet_birth_date, dob),
                stringCompat(R.string.snippet_birth_age, age)
            )

            else -> null
        }
    }

    private fun webAddressSnippets(addresses: List<WebAddress>?): Array<Snippet> =
        addresses.toSnippets { address ->
            Snippet(
                address.description,
                address.url,
                clickActionText = stringCompat(R.string.action_open_url, ),
                onclick = { context ->
                    context.openUrl(address.url)
                }
            )
        }

    private fun postSnippets(posts: List<Post>?): Array<Snippet> = posts.toSnippets { post ->
        val postType: String = stringCompat(when (post.postType) {
            Post.PostType.GOVERNMENTAL -> R.string.snippet_post_type_governmental
            Post.PostType.PARLIAMENTARY -> R.string.snippet_post_type_parliamentary
            Post.PostType.OPPOSITION -> R.string.snippet_post_type_opposition
        })

        val dateRange = dateRange(post.start, post.end)

        Snippet(
            post.name,
            postType,
            subcontent = dateRange
        )
    }

    private fun committeeMembershipSnippets(memberships: List<CommitteeMemberWithChairs>?): Array<Snippet> =
        memberships.toSnippets { membership ->
            Snippet(
                stringCompat(R.string.snippet_member_of, membership.membership.name),
                "chairs: [${membership.chairs.firstOrNull()}]",
                dateRange(membership.membership.start, membership.membership.end) ?: ""
            )
        }

    private fun houseMembershipSnippets(memberships: List<HouseMembership>?): Array<Snippet> =
        memberships.toSnippets { membership ->
            Snippet(
                stringCompat(R.string.snippet_member_of_house, membership.house),
                dateRange(membership.start, membership.end) ?: ""
            )
        }

    private fun financialInterestSnippets(interests: List<FinancialInterest>?): Array<Snippet> =
        interests.toSnippets { interest ->
            Snippet(
                stringCompat(R.string.snippet_financial_interest, interest.category),
                interest.description
            )
        }

    private fun experienceSnippets(experiences: List<Experience>?): Array<Snippet> =
        experiences.toSnippets { experience ->
            val title = if (experience.organisation == null) {
                experience.title
            }
            else {
                stringCompat(R.string.snippet_experience, experience.title, experience.organisation)
            }

            Snippet(
                title,
                experience.category,
                subcontent = dateRange(experience.start, experience.end)
            )
        }

    private fun topicOfInterestSnippets(topics: List<TopicOfInterest>?): Array<Snippet> =
        topics.toSnippets { topic ->
            Snippet(
                topic.category,
                topic.topic
            )
        }

    suspend fun toProfileData(member: CompleteMember): List<ProfileData<*>> {
        return listOf<ProfileData<*>>(
            ProfileData.Weblinks(weblinksOf(member)),
            ProfileData.Current(currentPositionsOf(member)),
            ProfileData.History(constructHistoryOf(member)),
            ProfileData.Addresses(member.addresses ?: listOf()),
            ProfileData.FinancialInterests(financialInterestsOf(member)),
        )
    }

    private suspend fun weblinksOf(member: CompleteMember): List<WeblinkData> =
        member.weblinks?.map { WeblinkData(it.url) }
            ?.sortedBy { it.displayText }
            ?: listOf()

    private suspend fun currentPositionsOf(member: CompleteMember): List<Any> {
        val profile = member.profile ?: return listOf()
        return listOfNotNull(
            profile.currentPost,
            if (profile.active == true) {
                when {
                    profile.isLord == true -> stringCompat(R.string.member_house_of_lords)
                    profile.isMp == true -> member.constituency
                    else -> null
                }
            } else {
                stringCompat(R.string.member_inactive)
            }
        )
    }

    private suspend fun financialInterestsOf(member:CompleteMember): List<FinancialInterest> =
        member.financialInterests?.sortedBy { it.dateCreated }
            ?: listOf()
}


private suspend fun constructHistoryOf(member: CompleteMember): List<HistoryItem<*>> {
    fun <T : Temporal> MutableList<HistoryItem<*>>.addEvents(items: List<T>?) {
        if (items != null) addAll(items.map { HistoryItem(it) })
    }

    return mutableListOf<HistoryItem<*>>().apply {
        addEvents(member.posts)
        addEvents(member.houses)
        addEvents(member.experiences)
        addEvents(compressConstituencies(member.historicConstituencies))
        addEvents(compressParties(member.parties))
        addEvents(member.committees)
        addEvents(member.financialInterests)

        sortBy { it.start }
    }
}


@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
suspend fun compressParties(parties: List<PartyAssociationWithParty>?) =
    compressConsecutiveItems(
        parties,
        areItemsTheSame = { a, b -> a.party == b.party },
        combineFunc = { a, b ->
            a.copy(
                partyAssocation = a.partyAssocation.copy(
                    end = b.end
                )
            )
        }
    )

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
suspend fun compressConstituencies(constituencies: List<HistoricalConstituencyWithElection>?) =
    compressConsecutiveItems(
        constituencies,
        areItemsTheSame = { a, b ->
            a.constituency == b.constituency
        },
        combineFunc = { a, b ->
            a.copy(
                historicalConstituency = a.historicalConstituency.copy(
                    end = b.historicalConstituency.end
                )
            )
        }
    )

data class HistoryItem<T: Temporal>(
    val item: T,
    override val start: LocalDate? = when (item) {
        is Periodic -> item.start
        is Dated -> item.date
        else -> null
    },
    override val end: LocalDate? = when (item) {
        is Periodic -> item.end
        else -> null
    }
): Periodic
