package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.constituency.*
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.FlowIoResult
import org.beatonma.commons.repo.converters.*
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiConstituency
import org.beatonma.commons.snommoc.models.ApiConstituencyElectionDetails
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConstituencyRepository @Inject constructor(
    private val remoteSource: CommonsApi,
    private val constituencyDao: ConstituencyDao,
    private val memberDao: MemberDao,
) {
    fun getConstituency(parliamentdotuk: ParliamentID): FlowIoResult<CompleteConstituency> = cachedResultFlow(
        databaseQuery = { getCachedConstituencyDetails(parliamentdotuk) },
        networkCall = { remoteSource.getConstituency(parliamentdotuk) },
        saveCallResult = { apiConstituency -> saveConstituency(constituencyDao, memberDao, parliamentdotuk, apiConstituency) },
        distinctUntilChanged = false,
    )


    fun getConstituencyResultsForElection(
        constituencyId: Int,
        electionId: Int
    ): FlowIoResult<ConstituencyElectionDetailsWithExtras> = cachedResultFlow(
        databaseQuery = { getCachedConstituencyElectionDetails(constituencyId, electionId) },
        networkCall = { remoteSource.getConstituencyDetailsForElection(constituencyId, electionId) },
        saveCallResult = { result -> saveRseults(constituencyDao, result) }
    )

    private fun getCachedConstituencyDetails(parliamentdotuk: ParliamentID): Flow<CompleteConstituency> = channelFlow {
        val builder = CompleteConstituency()
        val dataSourceFunctions = listOf(
            ConstituencyDao::getConstituencyWithBoundary,
            ConstituencyDao::getElectionResults,
        )
        val mergedFlow = dataSourceFunctions.map {
            it.invoke(constituencyDao, parliamentdotuk)
        }.merge()

        mergedFlow.collect { data ->
            if (data is List<*>) {
                val first = data.firstOrNull()
                when (first) {
                    is ConstituencyResultWithDetails -> builder.electionResults = data as List<ConstituencyResultWithDetails>
                    null -> Unit
                    else -> error("All valid types must be handled: ${data.javaClass.canonicalName}")
                }
            }
            else {
                when (data) {
                    is ConstituencyWithBoundary -> {
                        builder.constituency = data.constituency
                        builder.boundary = data.boundary
                    }
                    null -> Unit
                    else -> error("All valid types must be handled: ${data.javaClass.canonicalName}")
                }
            }
            send(builder)
        }
    }

    private fun getCachedConstituencyElectionDetails(
        constituencyId: ParliamentID,
        electionId: ParliamentID
    ): Flow<ConstituencyElectionDetailsWithExtras> = channelFlow {
        val builder = ConstituencyElectionDetailsWithExtras()

        val mergedFlow = merge(
            constituencyDao.getConstituency(constituencyId),
            constituencyDao.getElection(electionId),
            constituencyDao.getDetailsAndCandidatesForElection(constituencyId, electionId)
        )

        mergedFlow.collect { data: Any? ->
            when (data) {
                is Constituency -> builder.constituency = data
                is Election -> builder.election = data
                is ConstituencyElectionDetailsWithCandidates -> {
                    builder.candidates = data.candidates
                    builder.details = data.details
                }
                null -> Unit
                else -> error("All valid types must be handled: ${data.javaClass.canonicalName}")
            }
            send(builder)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun saveConstituency(constituencyDao: ConstituencyDao, memberDao: MemberDao, parliamentdotuk: ParliamentID, apiConstituency: ApiConstituency) {
        constituencyDao.insertConstituency(apiConstituency.toConstituency())
        memberDao.safeInsertProfile(apiConstituency.memberProfile?.toMemberProfile(), ifNotExists = true)
        apiConstituency.boundary?.also { boundary ->
            constituencyDao.insertBoundary(
                boundary.toConstituencyBoundary(constituencyId = apiConstituency.parliamentdotuk)
            )
        }
        constituencyDao.insertElections(apiConstituency.results.map { it.election.toElection() })
        memberDao.safeInsertProfiles(apiConstituency.results.map { it.member.toMemberProfile() }, ifNotExists = true)
        constituencyDao.insertElectionResults(apiConstituency.results.map { result ->
            result.toConstituencyResult(parliamentdotuk)
        })
    }

    suspend fun saveRseults(dao: ConstituencyDao, result: ApiConstituencyElectionDetails) {
        with (dao) {
            safeInsertConstituency(result.constituency.toConstituency(), ifNotExists = true)
            insertElection(result.election.toElection())
            insertConstituencyElectionDetails(result.toConstituencyElectionDetails())
            insertCandidates(
                result.candidates.map { apiCandidate ->
                    apiCandidate.toConstituencyCandidate(result.parliamentdotuk)
                }
            )
        }
    }
}
