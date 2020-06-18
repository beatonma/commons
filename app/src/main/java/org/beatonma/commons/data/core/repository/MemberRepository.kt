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
import org.beatonma.commons.kotlin.extensions.allNotNull
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
            updatePredicate = { m ->
                allNotNull(
                    m.profile,
                    m.addresses,
                    m.weblinks,
                    m.posts,
                    m.committees,
                    m.houses,
                    m.financialInterests,
                    m.experiences,
                    m.topicsOfInterest,
                    m.historicConstituencies,
                    m.parties,
                )
            },
        ) { member ->
            addSource(memberDao.getMemberProfile(parliamentdotuk)) { profile ->
                member.update({ it.profile != profile }) { copy(profile = profile) }
            }
            addSource(memberDao.getPhysicalAddresses(parliamentdotuk)) { addresses ->
                member.update({ it.addresses != addresses }) { copy(addresses = addresses) }
            }
            addSource(memberDao.getWebAddresses(parliamentdotuk)) { weblinks ->
                member.update({ it.weblinks != weblinks }) { copy(weblinks = weblinks) }
            }
            addSource(memberDao.getPosts(parliamentdotuk)) {posts ->
                member.update({ it.posts != posts }) { copy(posts = posts) }
            }
            addSource(memberDao.getCommitteeMembershipWithChairship(parliamentdotuk)) { committees ->
                member.update({ it.committees != committees }) { copy(committees = committees) }
            }
            addSource(memberDao.getHouseMemberships(parliamentdotuk)) { houses ->
                member.update({ it.houses != houses }) { copy(houses = houses) }
            }
            addSource(memberDao.getFinancialInterests(parliamentdotuk)) { financialInterests ->
                member.update({ it.financialInterests != financialInterests }) { copy(financialInterests = financialInterests) }
            }
            addSource(memberDao.getExperiences(parliamentdotuk)) { experiences ->
                member.update({ it.experiences != experiences }) { copy(experiences = experiences) }
            }
            addSource(memberDao.getTopicsOfInterest(parliamentdotuk)) { topicsOfInterest ->
                member.update({ it.topicsOfInterest != topicsOfInterest }) { copy(topicsOfInterest = topicsOfInterest) }
            }
            addSource(memberDao.getHistoricalConstituencies(parliamentdotuk)) { historicConstituencies ->
                member.update({ it.historicConstituencies != historicConstituencies }) { copy(historicConstituencies = historicConstituencies) }
            }
            addSource(memberDao.getPartyAssociations(parliamentdotuk)) { parties ->
                member.update({ it.parties != parties }) { copy(parties = parties) }
            }
            addSource(memberDao.getParty(parliamentdotuk)) { party ->
                member.update({ it.party != party }) { copy(party = party) }
            }
            addSource(memberDao.getConstituency(parliamentdotuk)) { constituency ->
                member.update({ it.constituency != constituency }) { copy(constituency = constituency) }
            }
        }
}
