package org.beatonma.commons.data.core.repository

import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DivisionRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val divisionDao: DivisionDao,
) {
    fun observeDivision(house: House, parliamentdotuk: ParliamentID): LiveDataIoResult<DivisionWithVotes> = resultLiveData(
        databaseQuery = { divisionDao.getDivisionWithVotes(parliamentdotuk) },
        networkCall = { remoteSource.getDivision(house, parliamentdotuk) },
        saveCallResult = { division -> divisionDao.insertApiDivision(parliamentdotuk, division) }
    )
}
