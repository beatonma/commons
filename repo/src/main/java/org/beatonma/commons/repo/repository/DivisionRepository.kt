package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.division.CommonsDivision
import org.beatonma.commons.repo.converters.getLordsDivisionData
import org.beatonma.commons.repo.converters.getParties
import org.beatonma.commons.repo.converters.getSponsor
import org.beatonma.commons.repo.converters.getVotes
import org.beatonma.commons.repo.converters.toDivision
import org.beatonma.commons.repo.converters.toParty
import org.beatonma.commons.repo.converters.toVote
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiCommonsDivision
import org.beatonma.commons.snommoc.models.ApiLordsDivision
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DivisionRepository @Inject constructor(
    private val remoteSource: CommonsApi,
    private val divisionDao: DivisionDao,
    private val memberDao: MemberDao,
) {
    fun getCommonsDivision(
        parliamentdotuk: ParliamentID,
    ): Flow<IoResult<CommonsDivision>> = cachedResultFlow(
        databaseQuery = { divisionDao.getDivisionWithVotes(parliamentdotuk) },
        networkCall = { remoteSource.getCommonsDivision(parliamentdotuk) },
        saveCallResult = { division -> saveDivision(parliamentdotuk, division) }
    )

    fun getLordsDivision(parliamentdotuk: ParliamentID) = cachedResultFlow(
        databaseQuery = { divisionDao.getLordsDivision(parliamentdotuk) },
        networkCall = { remoteSource.getLordsDivision(parliamentdotuk) },
        saveCallResult = ::saveLordsDivision
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun saveDivision(parliamentdotuk: ParliamentID, apiDivision: ApiCommonsDivision) {
        with(divisionDao) {
            insertPartiesIfNotExists(apiDivision.votes.map { it.party.toParty() })
            insertDivision(apiDivision.toDivision())

            apiDivision.votes.map { apiVote -> apiVote.toVote(parliamentdotuk) }

            insertVotes(
                apiDivision.votes.map { apiVote -> apiVote.toVote(parliamentdotuk) }
            )
        }
    }

    suspend fun saveLordsDivision(division: ApiLordsDivision) {
        with(memberDao) {
            insertPartiesIfNotExists(division.getParties())
            safeInsertProfile(division.getSponsor())
        }

        with(divisionDao) {
            safeInsertLordsDivision(
                division.getLordsDivisionData(),
                division.getVotes(),
            )
        }
    }
}
