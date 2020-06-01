package org.beatonma.commons.data.core.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import org.beatonma.commons.data.*
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.division.Vote
import org.beatonma.commons.data.core.room.entities.division.VoteWithDivision
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.livedata.observeComplete
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val memberDao: MemberDao,
    private val divisionDao: DivisionDao,
) {

    fun observeMember(parliamentdotuk: ParliamentID): LiveDataIoResult<CompleteMember> = resultLiveData(
        databaseQuery = { observeCompleteMember(parliamentdotuk) },
        networkCall = { remoteSource.getMember(parliamentdotuk) },
        saveCallResult = { member -> memberDao.insertCompleteMember(parliamentdotuk, member) }
    )

    fun observeCommonsVotesForMember(parliamentdotuk: ParliamentID): LiveDataIoResultList<VoteWithDivision> = resultLiveData(
        databaseQuery = { memberDao.getCommonsVotesForMember(parliamentdotuk) },
        networkCall = { remoteSource.getCommonsVotesForMember(parliamentdotuk) },
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
    fun observeCompleteMember(parliamentdotuk: ParliamentID): LiveData<CompleteMember> =
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
}
