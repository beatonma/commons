package org.beatonma.commons.data.core.repository

import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.FlowIoResultList
import org.beatonma.commons.data.IoResultList
import org.beatonma.commons.data.cachedResultFlow
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBill
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBillWithBill
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivision
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivisionWithDivision
import org.beatonma.commons.data.core.room.entities.member.FeaturedMemberProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeaturedRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val memberDao: MemberDao,
    private val billDao: BillDao,
    private val divisionDao: DivisionDao,
) {

    fun getFeaturedPeople(): FlowIoResultList<FeaturedMemberProfile> = cachedResultFlow(
        databaseQuery = { memberDao.getFeaturedProfiles() },
        networkCall = { remoteSource.getFeaturedPeople() },
        saveCallResult = { profiles ->
            memberDao.saveFeaturedPeople(profiles.map { it.toMemberProfile() })
        }
    )

    fun getFeaturedBills(): FlowIoResultList<FeaturedBillWithBill> = cachedResultFlow(
        databaseQuery = { billDao.getFeaturedBills() },
        networkCall = { remoteSource.getFeaturedBills() },
        saveCallResult = { bills ->
            billDao.insertBills(bills.map { it.toBill() })

            billDao.insertFeaturedBills(
                bills.map { FeaturedBill(it.parliamentdotuk) }
            )
        }
    )

    fun getFeaturedDivisions(): Flow<IoResultList<FeaturedDivisionWithDivision>> = cachedResultFlow(
        databaseQuery = { divisionDao.getFeaturedDivisionsFlow() },
        networkCall = { remoteSource.getFeaturedDivisions() },
        saveCallResult = { divisions ->
            divisionDao.insertDivisions(divisions.map { it.toDivision() })
            divisionDao.insertFeaturedDivisions(
                divisions.map { FeaturedDivision(it.parliamentdotuk) }
            )
        }
    )
}
