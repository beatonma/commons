package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.constituency.CompleteConstituency
import org.beatonma.commons.data.core.room.entities.constituency.CompleteConstituencyBuilder
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsBuilder
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.converters.toConstituency
import org.beatonma.commons.repo.converters.toConstituencyBoundary
import org.beatonma.commons.repo.converters.toConstituencyCandidate
import org.beatonma.commons.repo.converters.toConstituencyElectionDetails
import org.beatonma.commons.repo.converters.toConstituencyResult
import org.beatonma.commons.repo.converters.toElection
import org.beatonma.commons.repo.converters.toMemberProfile
import org.beatonma.commons.repo.remotesource.api.CommonsApi
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
    fun getConstituency(constituencyId: ParliamentID): ResultFlow<CompleteConstituency> =
        cachedResultFlow(
            databaseQuery = { getCompleteConstituency(constituencyId) },
            networkCall = { remoteSource.getConstituency(constituencyId) },
            saveCallResult = { saveConstituency(constituencyId, it) },
            distinctUntilChanged = false
        )

    fun getConstituencyResultsForElection(
        constituencyId: Int,
        electionId: Int,
    ): ResultFlow<ConstituencyElectionDetailsWithExtras> = cachedResultFlow(
        databaseQuery = { getCachedConstituencyElectionDetails(constituencyId, electionId) },
        networkCall = {
            remoteSource.getConstituencyDetailsForElection(constituencyId, electionId)
        },
        saveCallResult = ::saveResults
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getCompleteConstituency(constituencyId: ParliamentID): Flow<CompleteConstituency> =
        channelFlow {
            val builder = CompleteConstituencyBuilder()

            suspend fun submitCompleteConstituency() {
                if (builder.isComplete) {
                    send(builder.toCompleteConstituency())
                    close()
                }
            }

            suspend fun <E, T : Collection<E>> fetchList(
                func: ConstituencyDao.(ParliamentID) -> Flow<T>,
                block: (T) -> Unit,
            ) = launch {
                constituencyDao.func(constituencyId).collect {
                    block(it)
                    submitCompleteConstituency()
                }
            }

            suspend fun <T> fetchObject(
                func: ConstituencyDao.(ParliamentID) -> Flow<T?>,
                block: (T?) -> Unit,
            ) = launch {
                constituencyDao.func(constituencyId).collectLatest {
                    block(it)
                    submitCompleteConstituency()
                }
            }

            fetchObject(ConstituencyDao::getConstituencyWithBoundary) {
                builder.constituency = it?.constituency
                builder.boundary = it?.boundary
            }
            fetchList(ConstituencyDao::getElectionResults) {
                builder.member = it.firstOrNull()
                builder.electionResults =
                    if (it.size > 1) it.drop(1)
                    else listOf()
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getCachedConstituencyElectionDetails(
        constituencyId: ParliamentID,
        electionId: ParliamentID
    ): Flow<ConstituencyElectionDetailsWithExtras> =
        channelFlow {
            val builder = ConstituencyElectionDetailsBuilder()

            suspend fun submitCompleteConstituency() {
                if (builder.isComplete) {
                    send(builder.toConstituencyElectionDetailsWithExtras())
                }
            }

            suspend fun <T> fetchObject(
                func: ConstituencyDao.(ParliamentID, ParliamentID) -> Flow<T?>,
                block: (T?) -> Unit,
            ) = launch {
                constituencyDao.func(constituencyId, electionId).collectLatest {
                    block(it)
                    submitCompleteConstituency()
                }
            }

            suspend fun <T> fetchObject(
                func: ConstituencyDao.(ParliamentID) -> Flow<T?>,
                targetId: ParliamentID,
                block: (T?) -> Unit,
            ) = launch {
                constituencyDao.func(targetId).collectLatest {
                    block(it)
                    submitCompleteConstituency()
                }
            }

            fetchObject(ConstituencyDao::getElection, targetId = electionId) {
                builder.election = it
            }

            fetchObject(ConstituencyDao::getConstituency, targetId = constituencyId) {
                builder.constituency = it
            }

            fetchObject(ConstituencyDao::getDetailsAndCandidatesForElection) {
                builder.details = it?.details
                builder.candidates = it?.candidates
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun saveConstituency(
        parliamentdotuk: ParliamentID,
        apiConstituency: ApiConstituency,
    ) {
        constituencyDao.insertConstituency(apiConstituency.toConstituency())
        memberDao.safeInsertProfile(
            apiConstituency.memberProfile?.toMemberProfile(),
            ifNotExists = true
        )
        apiConstituency.boundary?.also { boundary ->
            constituencyDao.insertBoundary(
                boundary.toConstituencyBoundary(constituencyId = apiConstituency.parliamentdotuk)
            )
        }
        constituencyDao.insertElections(apiConstituency.results.map { it.election.toElection() })
        memberDao.safeInsertProfiles(
            apiConstituency.results.map { it.member.toMemberProfile() },
            ifNotExists = true
        )
        constituencyDao.insertElectionResults(apiConstituency.results.map { result ->
            result.toConstituencyResult(parliamentdotuk)
        })
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun saveResults(
        result: ApiConstituencyElectionDetails
    ) {
        memberDao.safeInsertProfiles(
            result.candidates.mapNotNull { it.profile?.toMemberProfile() },
            ifNotExists = true
        )

        constituencyDao.safeInsertElectionResults(
            result.constituency.toConstituency(),
            result.election.toElection(),
            result.toConstituencyElectionDetails(),
            result.candidates.map { it.toConstituencyCandidate(result.parliamentdotuk) }
        )
    }
}
