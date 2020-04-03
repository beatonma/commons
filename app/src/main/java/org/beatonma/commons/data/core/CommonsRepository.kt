package org.beatonma.commons.data.core

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.*
import org.beatonma.commons.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "CommonsRepo"

@Singleton
class CommonsRepository @Inject constructor(
    val context: Context,
    private val commonsRemoteDataSource: CommonsRemoteDataSource,
    private val memberDao: MemberDao,
    private val billDao: BillDao
) {
    fun observeFeaturedPeople(): LiveData<IoResult<List<FeaturedMemberProfile>>> = resultLiveData(
        databaseQuery = { memberDao.getFeaturedProfiles() },
        networkCall = { commonsRemoteDataSource.getFeaturedPeople() },
        saveCallResult = { profiles ->
            memberDao.apply {
                profiles.forEach { profile ->
                    insertParty(profile.party)
                    profile.constituency?.let { insertConstituency(it) }
                }

                insertProfiles(profiles)
                insertFeaturedPeople(
                    profiles.map { profile -> FeaturedMember(profile.parliamentdotuk) }
                )
            }
        }
    )

    fun observeMember(parliamentdotuk: Int): LiveData<IoResult<CompleteMember>> = resultLiveData(
        databaseQuery = { observeCompleteMember(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getMember(parliamentdotuk) },
        saveCallResult = { member -> memberDao.insertCompleteMember(parliamentdotuk, member) }
    )

    fun observeMemberProfile(parliamentdotuk: Int): LiveData<MemberProfile> =
        memberDao.getMemberProfile(parliamentdotuk)

    fun observeAddresses(parliamentdotuk: Int): LiveData<List<PhysicalAddress>> =
        memberDao.getPhysicalAddresses(parliamentdotuk)

    fun observeWebAddresses(parliamentdotuk: Int): LiveData<List<WebAddress>> =
        memberDao.getWebAddresses(parliamentdotuk)

    fun observePosts(parliamentdotuk: Int): LiveData<List<Post>> =
        memberDao.getPosts(parliamentdotuk)

    fun observeCommitteeMemberships(parliamentdotuk: Int): LiveData<List<CommitteeMemberWithChairs>> =
        memberDao.getCommitteeMembershipWithChairship(parliamentdotuk)

    fun observeHouseMemberships(parliamentdotuk: Int): LiveData<List<HouseMembership>> =
        memberDao.getHouseMemberships(parliamentdotuk)

    fun observeFinancialInterests(parliamentdotuk: Int): LiveData<List<FinancialInterest>> =
        memberDao.getFinancialInterests(parliamentdotuk)

    fun observeExperiences(parliamentdotuk: Int): LiveData<List<Experience>> =
        memberDao.getExperiences(parliamentdotuk)

    fun observeTopicsOfInterest(parliamentdotuk: Int): LiveData<List<TopicOfInterest>> =
        memberDao.getTopicsOfInterest(parliamentdotuk)

    fun observeHistoricalConstituencies(parliamentdotuk: Int): LiveData<List<HistoricalConstituencyWithElection>> =
        memberDao.getHistoricalConstituencies(parliamentdotuk)

    fun observePartyAssociations(parliamentdotuk: Int): LiveData<List<PartyAssociationWithParty>> =
        memberDao.getPartyAssociations(parliamentdotuk)

    fun observeCompleteMember(parliamentdotuk: Int): LiveData<CompleteMember> {
        val profile = observeMemberProfile(parliamentdotuk)
        val addresses = observeAddresses(parliamentdotuk)
        val weblinks = observeWebAddresses(parliamentdotuk)
        val posts = observePosts(parliamentdotuk)
        val committeeMemberships = observeCommitteeMemberships(parliamentdotuk)
        val houseMemberships = observeHouseMemberships(parliamentdotuk)
        val financialInterests = observeFinancialInterests(parliamentdotuk)
        val experiences = observeExperiences(parliamentdotuk)
        val topicsOfInterest = observeTopicsOfInterest(parliamentdotuk)
        val historicalConstituencies = observeHistoricalConstituencies(parliamentdotuk)
        val partyAssociations = observePartyAssociations(parliamentdotuk)

        val member = MutableCompleteMember()
        return MediatorLiveData<CompleteMember>().apply {
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
    }
}


private class MutableCompleteMember {
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
