package org.beatonma.commons.app.memberprofile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.datarendering.formatDate
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.CommonsRepository
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.*
import org.beatonma.lib.util.kotlin.extensions.stringCompat
import java.util.*
import javax.inject.Inject

class MemberProfileViewModel @Inject constructor(
    private val repository: CommonsRepository,
    private val application: CommonsApplication,
) : AndroidViewModel(application) {

    // Data
    private var member = InnerCompleteMember()

    lateinit var completeMember: LiveData<IoResult<CompleteMember>>
    lateinit var profile: LiveData<MemberProfile>
    lateinit var addresses: LiveData<List<PhysicalAddress>>
    lateinit var weblinks: LiveData<List<WebAddress>>
    lateinit var posts: LiveData<List<Post>>
    lateinit var committeeMemberships: LiveData<List<CommitteeMemberWithChairs>>
    lateinit var houseMemberships: LiveData<List<HouseMembership>>
    lateinit var financialInterests: LiveData<List<FinancialInterest>>
    lateinit var experiences: LiveData<List<Experience>>
    lateinit var topicsOfInterest: LiveData<List<TopicOfInterest>>
    lateinit var historicalConstituencies: LiveData<List<HistoricalConstituencyWithElection>>
    lateinit var partyAssociations: LiveData<List<PartyAssociationWithParty>>

    val livedataMediator: MediatorLiveData<CompleteMember> = MediatorLiveData()

    val snippets: MutableLiveData<List<Snippet>> = MutableLiveData()

    private var snippetJob: Job? = null
    private val snippetObserver = Observer<CompleteMember> { generateSnippets(it) }
    private val calendar: Calendar = Calendar.getInstance()

    fun forMember(parliamentdotuk: Int) {
        repository.run {
            completeMember = observeMember(parliamentdotuk)
            profile = observeMemberProfile(parliamentdotuk)
            addresses = observeAddresses(parliamentdotuk)
            weblinks = observeWebAddresses(parliamentdotuk)
            posts = observePosts(parliamentdotuk)
            committeeMemberships = observeCommitteeMemberships(parliamentdotuk)
            houseMemberships = observeHouseMemberships(parliamentdotuk)
            financialInterests = observeFinancialInterests(parliamentdotuk)
            experiences = observeExperiences(parliamentdotuk)
            topicsOfInterest = observeTopicsOfInterest(parliamentdotuk)
            historicalConstituencies = observeHistoricalConstituencies(parliamentdotuk)
            partyAssociations = observePartyAssociations(parliamentdotuk)
        }

        livedataMediator.apply {
            addSource(completeMember) {}
            addSource(member.member) { value = it }
            addSource(profile) { member.updateProfile(it) }
            addSource(addresses) { member.updateAddresses(it) }
            addSource(weblinks) { member.updateWeblinks(it) }
            addSource(posts) { member.updatePosts(it) }
            addSource(committeeMemberships) { member.updateCommitteeMemberships(it) }
            addSource(houseMemberships) { member.updateHouses(it) }
            addSource(financialInterests) { member.updateFinancialInterests(it) }
            addSource(experiences) { member.updateExperiences(it) }
            addSource(topicsOfInterest) { member.updateTopics(it) }
            addSource(historicalConstituencies) { member.updateHistoricalConstituencies(it) }
            addSource(partyAssociations) { member.updatePartyAssociations(it) }
        }

        livedataMediator.observeForever(snippetObserver)
    }

    override fun onCleared() {
        livedataMediator.removeObserver(snippetObserver)

        super.onCleared()
    }


    private fun generateSnippets(member: CompleteMember) {
        snippetJob?.cancel()
        snippetJob = viewModelScope.launch {
            snippets.value = listOfNotNull(
                *profileSnippets(member.profile),
                *webAddressSnippets(member.weblinks),
                *postSnippets(member.posts),
                *committeeMembershipSnippets(member.committees),
                *houseMembershipSnippets(member.houses),
                *financialInterestSnippets(member.financialInterests),
                *experienceSnippets(member.experiences),
                *topicOfInterestSnippets(member.topicsOfInterest)
            ).shuffled()
        }
    }

    private fun profileSnippets(profile: MemberProfile?): Array<Snippet> {
        profile ?: return arrayOf()
        return listOfNotNull(
            birthSnippet(profile)
        ).toTypedArray()
    }

    private fun birthSnippet(profile: MemberProfile): Snippet? {
        val dob: String? = profile.dateOfBirth?.let { formatDate(it) }
        val place: Town? = profile.placeOfBirth
        val age: Int? = profile.age

        return when {
            dob != null && place != null && age != null -> Snippet(
                R.string.snippet_birth_date.formatted(dob),
                R.string.snippet_birth_place.formatted(place.town, place.country),
                subtitle = R.string.snippet_birth_age.formatted(age))

            dob != null && age != null -> Snippet(
                R.string.snippet_birth_date.formatted(dob),
                R.string.snippet_birth_age.formatted(age)
            )

            else -> null
        }
    }

    private fun webAddressSnippets(addresses: List<WebAddress>?): Array<Snippet> =
        addresses.toSnippets { address ->
            Snippet(
                address.description,
                address.url,
                clickActionText = R.string.action_open_url.formatted(),
                onclick = { context ->
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(address.url)
                        })
                }
            )
        }

    private fun postSnippets(posts: List<Post>?): Array<Snippet> = posts.toSnippets { post ->
        val postType: String = (when (post.postType) {
            Post.PostType.GOVERNMENTAL -> R.string.snippet_post_type_governmental
            Post.PostType.PARLIAMENTARY -> R.string.snippet_post_type_parliamentary
            Post.PostType.OPPOSITION -> R.string.snippet_post_type_opposition
        }).formatted()

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
                R.string.snippet_member_of.formatted(membership.membership.name),
                "chairs: [${membership.chairs.firstOrNull()}]",
                dateRange(membership.membership.start, membership.membership.end) ?: ""
            )
        }

    private fun houseMembershipSnippets(memberships: List<HouseMembership>?): Array<Snippet> =
        memberships.toSnippets { membership ->
            Snippet(
                R.string.snippet_member_of_house.formatted(membership.house),
                dateRange(membership.start, membership.end) ?: ""
            )
        }

    private fun financialInterestSnippets(interests: List<FinancialInterest>?): Array<Snippet> =
        interests.toSnippets { interest ->
            Snippet(
                R.string.snippet_financial_interest.formatted(interest.category),
                interest.description
            )
        }

    private fun experienceSnippets(experiences: List<Experience>?): Array<Snippet> =
        experiences.toSnippets { experience ->
            val title = if (experience.organisation == null) {
                experience.title
            }
            else {
                R.string.snippet_experience.formatted(experience.title, experience.organisation)
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

    // Helpers
    /**
     * Generate an array of Snippets from a given list of objects
     */
    private fun <T> List<T>?.toSnippets(builder: ((T) -> Snippet?)): Array<Snippet> =
        this?.mapNotNull { builder(it) }?.toTypedArray() ?: arrayOf()

    private fun @receiver:StringRes Int.formatted(vararg formatArgs: Any): String =
        application.stringCompat(this, *formatArgs)

    private fun date(yyyy_mm__dd: String) = formatDate(yyyy_mm__dd, calendar)

    private fun dateRange(start: String?, end: String?): String? = when {
        start == null -> null
        end == null -> R.string.snippet_date_since.formatted(date(start))
        else -> R.string.snippet_date_range.formatted(date(start), date(end))
    }
}

/**
 * Container for small chunks of information to display in
 */
data class Snippet(
    val title: String,
    val content: String,
    val subtitle: String? = null,
    val subcontent: String? = null,
    val clickActionText: String? = null, // If onclick is set this text will be displayed on the button
    val onclick: ((Context) -> Unit)? = null,
)

private class InnerCompleteMember {
    val member: MutableLiveData<CompleteMember> = MutableLiveData()
    private var _member = CompleteMember()

    fun updateProfile(profile: MemberProfile?) =
        update(profile) { copy(profile = it) }

    fun updateAddresses(addresses: List<PhysicalAddress>?) =
        update(addresses) { copy(addresses = it) }

    fun updateWeblinks(weblinks: List<WebAddress>?) =
        update(weblinks) { copy(weblinks = it) }

    fun updatePosts(posts: List<Post>?) =
        update(posts) { copy(posts = it) }

    fun updateCommitteeMemberships(committees: List<CommitteeMemberWithChairs>?) =
        update(committees) { copy(committees = it) }

    fun updateTopics(topics: List<TopicOfInterest>?) =
        update(topics) { copy(topicsOfInterest = it) }

    fun updateHouses(houses: List<HouseMembership>?) =
        update(houses) { copy(houses = it) }

    fun updateFinancialInterests(interests: List<FinancialInterest>?) =
        update(interests) { copy(financialInterests = it) }

    fun updateExperiences(experiences: List<Experience>?) =
        update(experiences) { copy(experiences = it) }

    fun updateHistoricalConstituencies(constituencies: List<HistoricalConstituencyWithElection>) =
        update(constituencies) { copy(historicConstituencies = it) }

    fun updatePartyAssociations(associations: List<PartyAssociationWithParty>?) =
        update(associations) { copy(parties = it) }

    private inline fun <reified T> update(obj: T, block: CompleteMember.(T) -> CompleteMember): CompleteMember {
        if (obj != null) {
            _member = block.invoke(_member, obj)
            member.value = _member
        }
        return _member
    }
}
