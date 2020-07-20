package org.beatonma.commons.data.core.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.beatonma.commons.data.*
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBill
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBillWithBill
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivision
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivisionWithDivision
import org.beatonma.commons.data.core.room.entities.member.FeaturedMember
import org.beatonma.commons.data.core.room.entities.member.FeaturedMemberProfile
import org.beatonma.commons.kotlin.extensions.dump
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeaturedRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val memberDao: MemberDao,
    private val billDao: BillDao,
    private val divisionDao: DivisionDao,
) {
//    fun getFeaturedPeople(): LiveDataIoResultList<FeaturedMemberProfile> = resultLiveData(
//        databaseQuery = { memberDao.getFeaturedProfiles() },
//        networkCall = { remoteSource.getFeaturedPeople() },
//        saveCallResult = { profiles ->
//            memberDao.safeInsertProfiles(profiles, ifNotExists = true)
//
//            memberDao.apply {
//                insertFeaturedPeople(
//                    profiles.map { profile -> FeaturedMember(profile.parliamentdotuk) }
//                )
//            }
//        }
//    )

    fun getFeaturedPeople(): FlowIoResultList<FeaturedMemberProfile> = resultFlow(
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

    fun getFeaturedBills(): FlowIoResultList<FeaturedBillWithBill> = resultFlow(
        databaseQuery = { billDao.getFeaturedBills() },
        networkCall = { remoteSource.getFeaturedBills() },
        saveCallResult = { bills ->
            billDao.insertBills(bills)

            billDao.insertFeaturedBills(
                bills.map { FeaturedBill(it.parliamentdotuk) }
            )
        }
    )

//    fun getFeaturedBills(): LiveDataIoResultList<FeaturedBillWithBill> = resultLiveData(
//        databaseQuery = { billDao.getFeaturedBills() },
//        networkCall = { remoteSource.getFeaturedBills() },
//        saveCallResult = { bills ->
//            billDao.insertBills(bills)
//
//            billDao.insertFeaturedBills(
//                bills.map { FeaturedBill(it.parliamentdotuk) }
//            )
//        }
//    )

    // This works
    fun getFeaturedDivisions(): Flow<IoResultList<FeaturedDivisionWithDivision>> =
        divisionDao.getFeaturedDivisionsFlow().map { SuccessResult(it.dump(), "DBOK") }

    // This works
//    fun getFeaturedDivisionsFlow(): Flow<IoResultList<FeaturedDivisionWithDivision>> = flow<IoResultList<FeaturedDivisionWithDivision>> {
//        divisionDao.getFeaturedDivisionsFlow()
//            .mapLatest {
//                SuccessResult(it, "DB OK")
//            }
//            .collect {
//                emit(it)
//            }
//    }

    fun getFeaturedDivisionsFlow(): Flow<IoResultList<FeaturedDivisionWithDivision>> = resultFlow(
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
