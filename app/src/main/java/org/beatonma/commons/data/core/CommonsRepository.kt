package org.beatonma.commons.data.core

import android.content.Context
import androidx.lifecycle.LiveData
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
        databaseQuery = { memberDao.getCompleteMember(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getMember(parliamentdotuk) },
        saveCallResult = { member -> memberDao.insertCompleteMember(parliamentdotuk, member) }
    )

    fun observeMemberProfile(parliamentdotuk: Int): LiveData<MemberProfile> =
        memberDao.getMemberProfile(parliamentdotuk)

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
}

