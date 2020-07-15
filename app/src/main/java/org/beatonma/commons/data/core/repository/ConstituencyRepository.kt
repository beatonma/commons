package org.beatonma.commons.data.core.repository

import androidx.lifecycle.LiveData
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.constituency.CompleteConstituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.data.livedata.observeComplete
import org.beatonma.commons.data.resultLiveData
import org.beatonma.commons.kotlin.extensions.allNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConstituencyRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val constituencyDao: ConstituencyDao,
    private val memberDao: MemberDao,

    ) {
    fun observeConstituency(parliamentdotuk: ParliamentID): LiveDataIoResult<CompleteConstituency> = resultLiveData(
        databaseQuery = { observeConstituencyDetails(parliamentdotuk) },
        networkCall = { remoteSource.getConstituency(parliamentdotuk) },
        saveCallResult = { apiConstituency ->
            constituencyDao.insertConstituency(apiConstituency.toConstituency())

            memberDao.safeInsertProfile(apiConstituency.memberProfile, ifNotExists = true)

            apiConstituency.boundary?.also { boundary ->
                constituencyDao.insertBoundary(
                    boundary.toConstituencyBoundary(constituencyId = apiConstituency.parliamentdotuk)
                )
            }

            constituencyDao.insertElections(apiConstituency.results.map { it.election })
            memberDao.safeInsertProfiles(apiConstituency.results.map { it.member }, ifNotExists = true)

            constituencyDao.insertElectionResults(apiConstituency.results.map { result ->
                result.toConstituencyResult(parliamentdotuk)
            })
        }
    )

    fun observeConstituencyResultsForElection(
        constituencyId: Int,
        electionId: Int
    ): LiveDataIoResult<ConstituencyElectionDetailsWithExtras> = resultLiveData(
        databaseQuery = { observeConstituencyElectionDetails(constituencyId, electionId) },
        networkCall = { remoteSource.getConstituencyDetailsForElection(constituencyId, electionId) },
        saveCallResult = { result ->
            constituencyDao.insertConstituencyElectionDetails(result.toConstituencyElectionDetails())
            constituencyDao.insertCandidates(
                result.candidates.map { apiCandidate ->
                    apiCandidate.toConstituencyCandidate(result.parliamentdotuk)
                }
            )
        }
    )

    private fun observeConstituencyDetails(parliamentdotuk: ParliamentID): LiveData<CompleteConstituency> =
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

    private fun observeConstituencyElectionDetails(
            constituencyId: ParliamentID,
            electionId: ParliamentID
        ): LiveData<ConstituencyElectionDetailsWithExtras> = observeComplete(
            ConstituencyElectionDetailsWithExtras(),
            updatePredicate = { c -> c.details != null },
        ) { details ->
            addSource(constituencyDao.getElection(electionId)) { election ->
                details.update { copy(election = election) }
            }

            addSource(constituencyDao.getConstituency(constituencyId)) { constituency ->
                details.update { copy(constituency = constituency) }
            }

            addSource(constituencyDao.getDetailsAndCandidatesForElection(constituencyId, electionId)) { result ->
                result ?: return@addSource
                details.update {
                    copy(
                        details = result.details,
                        candidates = result.candidates,
                    )
                }
            }
        }
}
