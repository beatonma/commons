package org.beatonma.commons.repo.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill
import org.beatonma.commons.data.core.room.entities.division.ZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.member.ZeitgeistMember
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.converters.toBill
import org.beatonma.commons.repo.converters.toDivision
import org.beatonma.commons.repo.converters.toMemberProfile
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiZeitgeist
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("unused")
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

    suspend fun saveZeitgeist(zeitgeist: ApiZeitgeist) {
        with(billDao) {
            val bills = zeitgeist.bills
            insertBills(bills.map { it.target.toBill() })
            insertZeitgeistBills(bills.map {
                ZeitgeistBill(it.target.parliamentdotuk, it.reason?.name)
            })
        }

        with(divisionDao) {
            val divisions = zeitgeist.divisions
            insertDivisions(divisions.map { it.target.toDivision() })
            insertZeitgeistDivisions(divisions.map {
                ZeitgeistDivision(it.target.parliamentdotuk, it.reason?.name)
            })
        }

        with(memberDao) {
            val members = zeitgeist.people
            safeInsertProfiles(members.map { it.target.toMemberProfile() }, ifNotExists = true)
            insertZeitgeistMembers(members.map {
                ZeitgeistMember(it.target.parliamentdotuk, it.reason?.name)
            })
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadZeitgeist(): Flow<Zeitgeist> = channelFlow {
        val zeitgeist = Zeitgeist()

        launch {
            billDao.getZeitgeistBills().collect {
                zeitgeist.bills = it
                send(zeitgeist)
            }
        }

        launch {
            divisionDao.getZeitgeistDivisions().collect {
                zeitgeist.divisions = it
                send(zeitgeist)
            }
        }

        launch {
            memberDao.getZeitgeistMembers().collect {
                zeitgeist.members = it
                send(zeitgeist)
            }
        }
    }
}
