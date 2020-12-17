package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.constituency.CompleteConstituency
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithCandidates
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyWithBoundary
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.converters.toConstituency
import org.beatonma.commons.repo.converters.toConstituencyBoundary
import org.beatonma.commons.repo.converters.toConstituencyCandidate
import org.beatonma.commons.repo.converters.toConstituencyElectionDetails
import org.beatonma.commons.repo.converters.toConstituencyResult
import org.beatonma.commons.repo.converters.toElection
import org.beatonma.commons.repo.converters.toMemberProfile
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
    fun getConstituency(parliamentdotuk: ParliamentID): ResultFlow<CompleteConstituency> =
        cachedResultFlow(
            databaseQuery = { getCachedConstituencyDetails(parliamentdotuk) },
            networkCall = { remoteSource.getConstituency(parliamentdotuk) },
            saveCallResult = { apiConstituency ->
                saveConstituency(constituencyDao,
                    memberDao,
                    parliamentdotuk,
                    apiConstituency)
            },
            distinctUntilChanged = false,
        )

    fun getConstituencyResultsForElection(
        constituencyId: Int,
        electionId: Int,
    ): ResultFlow<ConstituencyElectionDetailsWithExtras> = cachedResultFlow(
        databaseQuery = { getCachedConstituencyElectionDetails(constituencyId, electionId) },
        networkCall = {
            remoteSource.getConstituencyDetailsForElection(constituencyId,
                electionId)
        },
        saveCallResult = { result -> saveRseults(constituencyDao, result) }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCachedConstituencyDetails(parliamentdotuk: ParliamentID): Flow<CompleteConstituency> = channelFlow {
        val builder = CompleteConstituency()
        val dataSourceFunctions = listOf(
            ConstituencyDao::getConstituencyWithBoundary,
            ConstituencyDao::getElectionResults,
        )
        val mergedFlow = dataSourceFunctions.map {
            it.invoke(constituencyDao, parliamentdotuk)
        }.merge()

        @Suppress("UNCHECKED_CAST")
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
                @Suppress("SENSELESS_NULL_IN_WHEN") // Room result can be null
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

    @OptIn(ExperimentalCoroutinesApi::class)
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
