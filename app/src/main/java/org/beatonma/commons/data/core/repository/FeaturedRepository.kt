package org.beatonma.commons.data.core.repository

import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.LiveDataIoResultList
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBill
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBillWithBill
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivision
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivisionWithDivision
import org.beatonma.commons.data.core.room.entities.member.FeaturedMember
import org.beatonma.commons.data.core.room.entities.member.FeaturedMemberProfile
import org.beatonma.commons.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeaturedRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val memberDao: MemberDao,
    private val billDao: BillDao,
    private val divisionDao: DivisionDao,
) {
    fun observeFeaturedPeople(): LiveDataIoResultList<FeaturedMemberProfile> = resultLiveData(
        databaseQuery = { memberDao.getFeaturedProfiles() },
        networkCall = { remoteSource.getFeaturedPeople() },
        saveCallResult = { profiles ->
            memberDao.safeInsertProfiles(profiles, ifNotExists = true)

            memberDao.apply {
                insertFeaturedPeople(
                    profiles.map { profile -> FeaturedMember(profile.parliamentdotuk) }
                )
            }
        }
    )

    fun observeFeaturedBills(): LiveDataIoResultList<FeaturedBillWithBill> = resultLiveData(
        databaseQuery = { billDao.getFeaturedBills() },
        networkCall = { remoteSource.getFeaturedBills() },
        saveCallResult = { bills ->
            billDao.insertBills(bills)

            billDao.insertFeaturedBills(
                bills.map { FeaturedBill(it.parliamentdotuk) }
            )
        }
    )

    fun observeFeaturedDivisions(): LiveDataIoResultList<FeaturedDivisionWithDivision> = resultLiveData(
        databaseQuery = { divisionDao.getFeaturedDivisions() },
        networkCall = { remoteSource.getFeaturedDivisions() },
        saveCallResult = { divisions ->
            divisionDao.insertDivisions(divisions.map { it.toDivision() })
            divisionDao.insertFeaturedDivisions(
                divisions.map { FeaturedDivision(it.parliamentdotuk) }
            )
        }
    )
}
