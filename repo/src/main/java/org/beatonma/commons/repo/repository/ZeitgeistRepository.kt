package org.beatonma.commons.repo.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.converters.getZeitgeistBills
import org.beatonma.commons.repo.converters.getZeitgeistDivisions
import org.beatonma.commons.repo.converters.getZeitgeistMembers
import org.beatonma.commons.repo.converters.toMemberProfile
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiZeitgeist
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ZeitgeistRepository @Inject constructor(
    private val remoteSource: CommonsApi,
    private val memberDao: MemberDao,
    private val billDao: BillDao,
    private val divisionDao: DivisionDao,
) {
    fun getZeitgeist(): ResultFlow<Zeitgeist> = cachedResultFlow(
        databaseQuery = ::loadZeitgeist,
        networkCall = remoteSource::getZeitgeist,
        saveCallResult = ::saveZeitgeist
    )

    private suspend fun saveZeitgeist(zeitgeist: ApiZeitgeist) {
        with(billDao) {
            insertZeitgeistBills(zeitgeist.getZeitgeistBills())
        }

        with(divisionDao) {
            insertZeitgeistDivisions(zeitgeist.getZeitgeistDivisions())
        }

        with(memberDao) {
            safeInsertProfiles(zeitgeist.people.map { it.target.toMemberProfile() },
                ifNotExists = true)
            insertZeitgeistMembers(zeitgeist.getZeitgeistMembers())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadZeitgeist(): Flow<Zeitgeist> = channelFlow {
        val zeitgeist = Zeitgeist()

        launch {
            billDao.getZeitgeistBills().collectLatest {
                zeitgeist.bills = it
                send(zeitgeist)
            }
        }

        launch {
            divisionDao.getZeitgeistDivisions().collectLatest {
                zeitgeist.divisions = it
                send(zeitgeist)
            }
        }

        launch {
            memberDao.getZeitgeistMembers().collectLatest {
                zeitgeist.members = it
                send(zeitgeist)
            }
        }
    }
}
