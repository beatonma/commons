package org.beatonma.commons.repo.repository

import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.FlowIoResult
import org.beatonma.commons.data.cachedResultFlow
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.repo.CommonsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton  @Suppress("unused")
class DivisionRepository @Inject constructor(
    private val remoteSource: CommonsApi,
    private val divisionDao: DivisionDao,
) {
    fun getDivision(house: House, parliamentdotuk: ParliamentID): FlowIoResult<DivisionWithVotes> = cachedResultFlow(
        databaseQuery = { divisionDao.getDivisionWithVotes(parliamentdotuk) },
        networkCall = { remoteSource.getDivision(house, parliamentdotuk) },
        saveCallResult = { TODO() }
//        saveCallResult = { division -> divisionDao.insertApiDivision(parliamentdotuk, division) }
    )
}
