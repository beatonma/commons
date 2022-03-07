package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.converters.getBillData
import org.beatonma.commons.repo.converters.getBillType
import org.beatonma.commons.repo.converters.getPublicationData
import org.beatonma.commons.repo.converters.getPublicationLinks
import org.beatonma.commons.repo.converters.getSessions
import org.beatonma.commons.repo.converters.getSponsorData
import org.beatonma.commons.repo.converters.getSponsorMembers
import org.beatonma.commons.repo.converters.getStages
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiBill
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BillRepository @Inject constructor(
    private val commonsApi: CommonsApi,
    private val billDao: BillDao,
    private val memberDao: MemberDao,
) {
    fun getBill(billId: ParliamentID): ResultFlow<Bill> = cachedResultFlow(
        databaseQuery = { billDao.getBill(billId) },
        networkCall = { commonsApi.getBill(billId) },
        saveCallResult = ::saveApiBill,
    )

    @VisibleForTesting
    suspend internal fun saveApiBill(apiBill: ApiBill) {
        memberDao.safeInsertProfiles(apiBill.getSponsorMembers(), ifNotExists = true)

        billDao.insertBill(
            apiBill.getBillData(),
            apiBill.getBillType(),
            apiBill.getStages(),
            apiBill.getSessions(),
            apiBill.getPublicationData(),
            apiBill.getPublicationLinks(),
            apiBill.getSponsorData(),
        )
    }
}
