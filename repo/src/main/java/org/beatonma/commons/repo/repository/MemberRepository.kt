package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.FlowIoResult
import org.beatonma.commons.repo.converters.*
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiCompleteMember
import javax.inject.Inject
import javax.inject.Singleton

interface MemberRepository {
    fun getMember(parliamentdotuk: ParliamentID): FlowIoResult<CompleteMember>
    suspend fun saveMember(dao: MemberDao, parliamentdotuk: ParliamentID, member: ApiCompleteMember)
}

@Singleton
class MemberRepositoryImpl @Inject constructor(
    private val remoteSource: CommonsApi,
    private val memberDao: MemberDao,
): MemberRepository {
    override fun getMember(parliamentdotuk: ParliamentID): FlowIoResult<CompleteMember> = cachedResultFlow(
        databaseQuery = { getCompleteMember(parliamentdotuk) },
        networkCall = { remoteSource.getMember(parliamentdotuk) },
        saveCallResult = { member -> saveMember(memberDao, parliamentdotuk, member) },
        distinctUntilChanged = false
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getCompleteMember(parliamentdotuk: ParliamentID): Flow<CompleteMember> = channelFlow {
        val builder = CompleteMember()

        val profile = memberDao.getMemberProfileTimestampedFlow(parliamentdotuk)
        val dataSourceFunctions = listOf(
            MemberDao::getPhysicalAddresses,
            MemberDao::getWebAddresses,
            MemberDao::getPosts,
            MemberDao::getCommitteeMembershipWithChairship,
            MemberDao::getHouseMemberships,
            MemberDao::getFinancialInterests,
            MemberDao::getExperiences,
            MemberDao::getTopicsOfInterest,
            MemberDao::getHistoricalConstituencies,
            MemberDao::getPartyAssociations,
            MemberDao::getParty,
            MemberDao::getConstituency,
        )

        val mergedFlow = merge(
            profile,
            dataSourceFunctions.map { func ->
                func.invoke(memberDao, parliamentdotuk)
            }.merge()
        )

        mergedFlow.collect { data: Any? ->
            if (data is List<*>) {
                val first = data.firstOrNull()

                @Suppress("UNCHECKED_CAST")
                when (first) {
                    is PhysicalAddress -> builder.addresses = data as List<PhysicalAddress>
                    is WebAddress -> builder.weblinks = data as List<WebAddress>
                    is Post -> builder.posts = data as List<Post>
                    is CommitteeMemberWithChairs -> builder.committees = data as List<CommitteeMemberWithChairs>
                    is HouseMembership -> builder.houses = data as List<HouseMembership>
                    is FinancialInterest -> builder.financialInterests = data as List<FinancialInterest>
                    is Experience -> builder.experiences = data as List<Experience>
                    is TopicOfInterest -> builder.topicsOfInterest = data as List<TopicOfInterest>
                    is HistoricalConstituencyWithElection -> builder.historicConstituencies = data as List<HistoricalConstituencyWithElection>
                    is PartyAssociationWithParty -> builder.parties = data as List<PartyAssociationWithParty>
                    null -> Unit
                    else -> error("All valid types must be handled: ${data.javaClass.canonicalName}")
                }
            }
            else {
                when (data) {
                    is MemberProfile -> builder.profile = data
                    is Constituency -> builder.constituency = data
                    is Party -> builder.party = data
                    null -> Unit
                    else -> error("All valid types must be handled: ${data.javaClass.canonicalName}")
                }
            }
            send(builder)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    override suspend fun saveMember(dao: MemberDao, parliamentdotuk: ParliamentID, member: ApiCompleteMember) {
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
