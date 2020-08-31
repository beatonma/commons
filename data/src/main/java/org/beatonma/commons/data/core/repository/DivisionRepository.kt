package org.beatonma.commons.data.core.repository

import org.beatonma.commons.data.CommonsApi
import org.beatonma.commons.data.FlowIoResult
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.cachedResultFlow
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.member.House
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DivisionRepository @Inject constructor(
    private val remoteSource: CommonsApi,
    private val divisionDao: DivisionDao,
) {
    fun getDivision(house: House, parliamentdotuk: ParliamentID): FlowIoResult<DivisionWithVotes> = cachedResultFlow(
        databaseQuery = { divisionDao.getDivisionWithVotes(parliamentdotuk) },
        networkCall = { remoteSource.getDivision(house, parliamentdotuk) },
        saveCallResult = { division -> divisionDao.insertApiDivision(parliamentdotuk, division) }
    )
}
