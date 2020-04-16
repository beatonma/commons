package org.beatonma.commons.data.core.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBill
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBillWithBill
import org.beatonma.commons.data.core.room.entities.division.*
import org.beatonma.commons.data.core.room.entities.member.FeaturedMember
import org.beatonma.commons.data.core.room.entities.member.FeaturedMemberProfile
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "CommonsRepo"

@Singleton
class CommonsRepository @Inject constructor(
    val context: Context,
    private val commonsRemoteDataSource: CommonsRemoteDataSource,
    private val memberDao: MemberDao,
    private val billDao: BillDao,
    private val divisionDao: DivisionDao,
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

    fun observeFeaturedBills(): LiveData<IoResult<List<FeaturedBillWithBill>>> = resultLiveData(
        databaseQuery = { billDao.getFeaturedBills() },
        networkCall = { commonsRemoteDataSource.getFeaturedBills() },
        saveCallResult = { bills ->
            billDao.insertBills(bills)

            billDao.insertFeaturedBills(
                bills.map { FeaturedBill(it.parliamentdotuk) }
            )
        }
    )

    fun observeFeaturedDivisions(): LiveData<IoResult<List<FeaturedDivisionWithDivision>>> = resultLiveData(
        databaseQuery = { divisionDao.getFeaturedDivisions() },
        networkCall = { commonsRemoteDataSource.getFeaturedDivisions() },
        saveCallResult = { divisions ->
            divisionDao.insertDivisions(divisions.map { it.toDivision() })
            divisionDao.insertFeaturedDivisions(
                divisions.map { FeaturedDivision(it.parliamentdotuk) }
            )
        }
    )

    fun observeBill(parliamentdotuk: Int): LiveData<IoResult<CompleteBill>> = resultLiveData(
        databaseQuery = { observeCompleteBill(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getBill(parliamentdotuk) },
        saveCallResult = { bill -> billDao.insertCompleteBill(parliamentdotuk, bill) }
    )

    fun observeMember(parliamentdotuk: Int): LiveData<IoResult<CompleteMember>> = resultLiveData(
        databaseQuery = { observeCompleteMember(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getMember(parliamentdotuk) },
        saveCallResult = { member -> memberDao.insertCompleteMember(parliamentdotuk, member) }
    )

    fun observeDivision(house: House, parliamentdotuk: Int): LiveData<IoResult<DivisionWithVotes>> = resultLiveData(
        databaseQuery = { divisionDao.getDivisionWithVotes(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getDivision(house, parliamentdotuk) },
        saveCallResult = { division -> divisionDao.insertApiDivision(parliamentdotuk, division) }
    )

    fun observeCommonsVotesForMember(parliamentdotuk: Int): LiveData<IoResult<List<VoteWithDivision>>> = resultLiveData(
        databaseQuery = { memberDao.getCommonsVotesForMember(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getCommonsVotesForMember(parliamentdotuk) },
        saveCallResult = { memberVotes ->
            divisionDao.insertDivisions(memberVotes.map {
                it.division.copy(house = House.Commons)
            })
            divisionDao.insertVotes(memberVotes.map { memberVote ->
                Vote(
                    memberId = parliamentdotuk,
                    divisionId = memberVote.division.parliamentdotuk,
                    voteType = memberVote.voteType,
                    memberName = ""
                )
            })
        }
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun observeCompleteMember(parliamentdotuk: Int): LiveData<CompleteMember> {
        val member = MutableCompleteMember()

        return MediatorLiveData<CompleteMember>().apply {
            addSource(member.value) {
                // Propagate changes from mutable member to observer
                value = it
            }

            addSource(memberDao.getMemberProfile(parliamentdotuk)) {
                member.update { copy(profile = it) }
            }
            addSource(memberDao.getPhysicalAddresses(parliamentdotuk)) {
                member.update { copy(addresses = it) }
            }
            addSource(memberDao.getWebAddresses(parliamentdotuk)) {
                member.update { copy(weblinks = it) }
            }
            addSource(memberDao.getPosts(parliamentdotuk)) {
                member.update { copy(posts = it) }
            }
            addSource(memberDao.getCommitteeMembershipWithChairship(parliamentdotuk)) {
                member.update { copy(committees = it) }
            }
            addSource(memberDao.getHouseMemberships(parliamentdotuk)) {
                member.update { copy(houses = it) }
            }
            addSource(memberDao.getFinancialInterests(parliamentdotuk)) {
                member.update { copy(financialInterests = it) }
            }
            addSource(memberDao.getExperiences(parliamentdotuk)) {
                member.update { copy(experiences = it) }
            }
            addSource(memberDao.getTopicsOfInterest(parliamentdotuk)) {
                member.update { copy(topicsOfInterest = it) }
            }
            addSource(memberDao.getHistoricalConstituencies(parliamentdotuk)) {
                member.update { copy(historicConstituencies = it) }
            }
            addSource(memberDao.getPartyAssociations(parliamentdotuk)) {
                member.update { copy(parties = it) }
            }
        }
    }

    private fun observeCompleteBill(parliamentdotuk: Int): LiveData<CompleteBill> {
        val bill =
            MutableCompleteBill()

        return MediatorLiveData<CompleteBill>().apply {
            addSource(bill.value) {
                // Propagate changes from mutable bill to observer
                value = it
            }

            addSource(billDao.getBill(parliamentdotuk)) { _bill ->
                bill.update { copy(bill = _bill) }
            }
            addSource(billDao.getBillType(parliamentdotuk)) { type ->
                bill.update { copy(type = type) }
            }
            addSource(billDao.getBillSession(parliamentdotuk)) { session ->
                bill.update { copy(session = session) }
            }
            addSource(billDao.getBillPublications(parliamentdotuk)) { publications ->
                bill.update { copy(publications = publications) }
            }
            addSource(billDao.getBillSponsors(parliamentdotuk)) { sponsors ->
                bill.update { copy(sponsors = sponsors) }
            }
            addSource(billDao.getBillStages(parliamentdotuk)) { stages ->
                bill.update { copy(stages = stages) }
            }
        }
    }
}


private class MutableCompleteBill: Mutator<CompleteBill>() {
    override var mutable: CompleteBill = CompleteBill()
}

private class MutableCompleteMember: Mutator<CompleteMember>() {
    override var mutable: CompleteMember = CompleteMember()
}

private abstract class Mutator<D> {
    val value: MutableLiveData<D> = MutableLiveData()
    protected abstract var mutable: D

    inline fun update(block: D.() -> D): D {
        mutable = block.invoke(mutable)
        value.value = mutable
        return mutable
    }
}
