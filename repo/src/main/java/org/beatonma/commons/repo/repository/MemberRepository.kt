package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.CompleteMemberBuilder
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.constituency.NoConstituency
import org.beatonma.commons.data.core.room.entities.member.NoParty
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.converters.toCommitteeChairship
import org.beatonma.commons.repo.converters.toCommitteeMembership
import org.beatonma.commons.repo.converters.toConstituency
import org.beatonma.commons.repo.converters.toElection
import org.beatonma.commons.repo.converters.toExperience
import org.beatonma.commons.repo.converters.toFinancialInterest
import org.beatonma.commons.repo.converters.toHistoricalConstituency
import org.beatonma.commons.repo.converters.toHouseMembership
import org.beatonma.commons.repo.converters.toMemberProfile
import org.beatonma.commons.repo.converters.toParty
import org.beatonma.commons.repo.converters.toPartyAssociation
import org.beatonma.commons.repo.converters.toPhysicalAddress
import org.beatonma.commons.repo.converters.toPost
import org.beatonma.commons.repo.converters.toTopicOfInterest
import org.beatonma.commons.repo.converters.toWebAddress
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiCompleteMember
import javax.inject.Inject
import javax.inject.Singleton

interface MemberRepository {
    fun getMember(parliamentdotuk: ParliamentID): ResultFlow<CompleteMember>
    suspend fun saveMember(dao: MemberDao, parliamentdotuk: ParliamentID, member: ApiCompleteMember)
}

@Singleton
class MemberRepositoryImpl @Inject constructor(
    private val remoteSource: CommonsApi,
    private val memberDao: MemberDao,
) : MemberRepository {
    override fun getMember(parliamentdotuk: ParliamentID): ResultFlow<CompleteMember> =
        cachedResultFlow(
            databaseQuery = { getCompleteMember(parliamentdotuk) },
            networkCall = { remoteSource.getMember(parliamentdotuk) },
            saveCallResult = { member -> saveMember(memberDao, parliamentdotuk, member) },
            distinctUntilChanged = true
        ).catch {
            println("getMember ERROR $it")
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getCompleteMember(parliamentdotuk: ParliamentID): Flow<CompleteMember> = channelFlow {
        val builder = CompleteMemberBuilder()

        suspend fun submitCompleteMember() {
            if (builder.isComplete) {
                send(builder.toCompleteMember())
            }
        }

        suspend fun <T> suspendedFetch(
            func: suspend MemberDao.(ParliamentID) -> Flow<T>,
            block: (T?) -> Unit,
        ) = launch {
            memberDao.func(parliamentdotuk).collect {
                block(it)
                submitCompleteMember()
            }
        }

        suspend fun <T> fetch(
            func: MemberDao.(ParliamentID) -> Flow<T>,
            block: (T) -> Unit,
        ) = launch {
            memberDao.func(parliamentdotuk).collect {
                block(it)
                submitCompleteMember()
            }
        }

        suspend fun <T> fetchNullable(
            func: MemberDao.(ParliamentID) -> Flow<T>,
            block: (T?) -> Unit,
        ) = launch {
            memberDao.func(parliamentdotuk).collect {
                block(it)
                submitCompleteMember()
            }
        }

        suspendedFetch(MemberDao::getMemberProfileTimestampedFlow) {
            builder.profile = it
        }
        fetch(MemberDao::getPhysicalAddresses) {
            builder.addresses = it
        }
        fetch(MemberDao::getWebAddresses) {
            builder.weblinks = it
        }
        fetch(MemberDao::getPosts) {
            builder.posts = it
        }
        fetch(MemberDao::getCommitteeMembershipWithChairship) {
            builder.committees = it
        }
        fetch(MemberDao::getHouseMemberships) {
            builder.houses = it
        }
        fetch(MemberDao::getFinancialInterests) {
            builder.financialInterests = it
        }
        fetch(MemberDao::getExperiences) {
            builder.experiences = it
        }
        fetch(MemberDao::getTopicsOfInterest) {
            builder.topicsOfInterest = it
        }
        fetch(MemberDao::getHistoricalConstituencies) {
            builder.historicConstituencies = it
        }
        fetch(MemberDao::getPartyAssociations) {
            builder.parties = it
        }
        fetchNullable(MemberDao::getParty) {
            builder.party = it ?: NoParty
        }
        fetchNullable(MemberDao::getConstituency) {
            builder.constituency = it ?: NoConstituency
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    override suspend fun saveMember(
        dao: MemberDao,
        parliamentdotuk: ParliamentID,
        member: ApiCompleteMember,
    ) {
        with(dao) {
            insertPartiesIfNotExists(member.parties.map { it.party.toParty() })
            insertPartyIfNotExists(member.profile.party.toParty())
            insertConstituenciesIfNotExists(
                member.constituencies.map { it.constituency.toConstituency() }
            )
            withNotNull(member.profile.constituency) {
                // Not necessarily included in member.constituencies list.
                insertConstituencyIfNotExists(it.toConstituency())
            }

            safeInsertProfile(member.profile.toMemberProfile())

            insertPhysicalAddresses(
                member.addresses.physical.map { it.toPhysicalAddress(memberId = parliamentdotuk) }
            )
            insertWebAddresses(
                member.addresses.web.map { it.toWebAddress(memberId = parliamentdotuk) }
            )

            insertPosts(member.posts.governmental.map { post ->
                post.toPost(memberId = parliamentdotuk, postType = Post.PostType.GOVERNMENTAL)
            })
            insertPosts(member.posts.parliamentary.map { post ->
                post.toPost(memberId = parliamentdotuk, postType = Post.PostType.PARLIAMENTARY)
            })
            insertPosts(member.posts.opposition.map { post ->
                post.toPost(memberId = parliamentdotuk, postType = Post.PostType.OPPOSITION)
            })

            insertCommitteeMemberships(member.committees.map { membership ->
                membership.toCommitteeMembership(parliamentdotuk)
            })

            member.committees.forEach { committee ->
                insertCommitteeChairships(committee.chairs.map { chair ->
                    chair.toCommitteeChairship(
                        committeeId = committee.parliamentdotuk,
                        memberId = parliamentdotuk
                    )
                })
            }

            insertHouseMemberships(member.houses.map { house ->
                house.toHouseMembership(memberId = parliamentdotuk)
            })

            insertFinancialInterests(member.financialInterests.map { it.toFinancialInterest(memberId = parliamentdotuk) })
            insertExperiences(member.experiences.map { it.toExperience(memberId = parliamentdotuk) })
            insertTopicsOfInterest(member.topicsOfInterest.map { it.toTopicOfInterest(memberId = parliamentdotuk) })

            insertElections(member.constituencies.map { it.election.toElection() })
            insertMemberForConstituencies(member.constituencies.map {
                it.toHistoricalConstituency(parliamentdotuk)
            })

            insertPartyAssociations(member.parties.map { it.toPartyAssociation(parliamentdotuk) })
        }
    }
}
