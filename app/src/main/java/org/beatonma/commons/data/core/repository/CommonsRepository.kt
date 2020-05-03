package org.beatonma.commons.data.core.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBill
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBillWithBill
import org.beatonma.commons.data.core.room.entities.constituency.CompleteConstituency
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.division.*
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.data.livedata.observeComplete
import org.beatonma.commons.data.resultLiveData
import org.beatonma.commons.kotlin.extensions.allNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommonsRepository @Inject constructor(
    val context: Context,
    private val commonsRemoteDataSource: CommonsRemoteDataSource,
    private val memberDao: MemberDao,
    private val billDao: BillDao,
    private val divisionDao: DivisionDao,
    private val constituencyDao: ConstituencyDao,
) {
    fun observeFeaturedPeople(): LiveData<IoResult<List<FeaturedMemberProfile>>> = resultLiveData(
        databaseQuery = { memberDao.getFeaturedProfiles() },
        networkCall = { commonsRemoteDataSource.getFeaturedPeople() },
        saveCallResult = { profiles ->
            saveProfiles(profiles)

            memberDao.apply {
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

    fun observeConstituency(parliamentdotuk: Int): LiveData<IoResult<CompleteConstituency>> = resultLiveData(
        databaseQuery = { observeConstituencyDetails(parliamentdotuk) },
        networkCall = { commonsRemoteDataSource.getConstituency(parliamentdotuk) },
        saveCallResult = { apiConstituency ->
            constituencyDao.insertConstituency(apiConstituency.toConstituency())

            saveProfile(apiConstituency.memberProfile, ifNotExists = true)

            apiConstituency.boundary?.also { boundary ->
                constituencyDao.insertBoundary(
                    boundary.copy(constituencyId = apiConstituency.parliamentdotuk)
                )
            }

            memberDao.insertElections(apiConstituency.results.map { it.election })
            saveProfiles(apiConstituency.results.map { it.member }, ifNotExists = true)

            constituencyDao.insertElectionResults(apiConstituency.results.map { result ->
                result.toConstituencyResult(parliamentdotuk)
            })
        }
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun observeCompleteMember(parliamentdotuk: Int): LiveData<CompleteMember> =
        observeComplete(
            CompleteMember(),
            updatePredicate = { m -> m.profile != null },
        ) { member ->
            addSource(memberDao.getMemberProfile(parliamentdotuk)) {
                member.update { copy(profile = it) }
            }
            addSource(memberDao.getParty(parliamentdotuk)) {
                member.update { copy(party = it) }
            }
            addSource(memberDao.getConstituency(parliamentdotuk)) {
                member.update { copy(constituency = it) }
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

    private fun observeCompleteBill(parliamentdotuk: Int): LiveData<CompleteBill> =
        observeComplete(
            CompleteBill(),
            updatePredicate = { b -> b.bill != null },
        ) { bill ->
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

    private fun observeConstituencyDetails(parliamentdotuk: Int): LiveData<CompleteConstituency> =
        observeComplete(
            CompleteConstituency(),
            updatePredicate = { c -> allNotNull(c.boundary, c.constituency, c.electionResults) },
        ) { constituency ->
            addSource(constituencyDao.getConstituencyWithBoundary(parliamentdotuk)) {
                constituency.update {
                    copy(
                        constituency = it.constituency,
                        boundary = it.boundary,
                    )
                }
            }
            addSource(constituencyDao.getElectionResults(parliamentdotuk)) { results ->
                constituency.update {
                    copy(
                        electionResults = results,
                    )
                }
            }
        }

    private suspend fun saveProfile(profile: MemberProfile?, ifNotExists: Boolean = false) {
        profile ?: return

        // The embedded party/constituency are low-detail so we do not want to overwrite any
        // high-detail records we might already hold.
        saveConstituency(profile.constituency, true)
        saveParty(profile.party, true)

        if (ifNotExists) {
            memberDao.insertProfileIfNotExists(profile)
        }
        else {
            memberDao.insertProfile(profile)
        }
    }

    private suspend fun saveProfiles(profiles: List<MemberProfile>, ifNotExists: Boolean = false) {
        saveConstituencies(profiles.mapNotNull { it.constituency }, ifNotExists = true)
        saveParties(profiles.map { it.party }, ifNotExists = true)

        if (ifNotExists) {
            memberDao.insertProfilesIfNotExists(profiles)
        }
        else {
            memberDao.insertProfiles(profiles)
        }
    }

    private suspend fun saveConstituency(constituency: Constituency?, ifNotExists: Boolean = false) {
        constituency ?: return

        if (ifNotExists) {
            constituencyDao.insertConstituencyIfNotExists(constituency)
        }
        else {
            constituencyDao.insertConstituency(constituency)
        }
    }

    private suspend fun saveConstituencies(constituencies: List<Constituency>, ifNotExists: Boolean = false) {
        if (ifNotExists) {
            constituencyDao.insertConstituenciesIfNotExists(constituencies)
        }
        else {
            constituencyDao.insertConstituencies(constituencies)
        }
    }

    private suspend fun saveParty(party: Party?, ifNotExists: Boolean = false) {
        party ?: return
        if (ifNotExists) {
            memberDao.insertPartyIfNotExists(party)
        }
        else {
            memberDao.insertParty(party)
        }
    }

    private suspend fun saveParties(parties: List<Party>, ifNotExists: Boolean = false) {
        if (ifNotExists) {
            memberDao.insertPartiesIfNotExists(parties)
        }
        else {
            memberDao.insertParties(parties)
        }
    }
}
