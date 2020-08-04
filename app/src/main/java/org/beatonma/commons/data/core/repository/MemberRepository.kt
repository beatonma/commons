package org.beatonma.commons.data.core.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.FlowIoResult
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.cachedResultFlow
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.*
import javax.inject.Inject
import javax.inject.Singleton

interface MemberRepository {
    fun getMember(parliamentdotuk: ParliamentID): FlowIoResult<CompleteMember>
}

@Singleton
class MemberRepositoryImpl @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val memberDao: MemberDao,
): MemberRepository {
    override fun getMember(parliamentdotuk: ParliamentID): FlowIoResult<CompleteMember> = cachedResultFlow(
        databaseQuery = { getCompleteMember(parliamentdotuk) },
        networkCall = { remoteSource.getMember(parliamentdotuk) },
        saveCallResult = { member -> memberDao.insertCompleteMember(parliamentdotuk, member) },
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
}
