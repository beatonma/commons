package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.repo.converters.toDivision
import org.beatonma.commons.repo.converters.toParty
import org.beatonma.commons.repo.converters.toVote
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiDivision
import javax.inject.Inject
import javax.inject.Singleton

@Singleton @Suppress("unused")
class DivisionRepository @Inject constructor(
    private val remoteSource: CommonsApi,
    private val divisionDao: DivisionDao,
) {
    fun getDivision(
        house: House,
        parliamentdotuk: ParliamentID,
    ): Flow<IoResult<DivisionWithVotes>> = cachedResultFlow(
        databaseQuery = { divisionDao.getDivisionWithVotes(parliamentdotuk) },
        networkCall = { remoteSource.getDivision(house, parliamentdotuk) },
        saveCallResult = { division -> saveDivision(divisionDao, parliamentdotuk, division) }
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun saveDivision(dao: DivisionDao, parliamentdotuk: ParliamentID, apiDivision: ApiDivision) {
        with(dao) {
            insertPartiesIfNotExists(apiDivision.votes.mapNotNull { it.party?.toParty() })
            insertDivision(apiDivision.toDivision())

            apiDivision.votes.map { apiVote -> apiVote.toVote(parliamentdotuk) }

            insertVotes(
                apiDivision.votes.map { apiVote -> apiVote.toVote(parliamentdotuk) }
            )
        }
    }
}
