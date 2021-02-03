package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.core.room.entities.bill.CompleteBillBuilder
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.converters.toBill
import org.beatonma.commons.repo.converters.toBillPublication
import org.beatonma.commons.repo.converters.toBillPublicationDetail
import org.beatonma.commons.repo.converters.toBillSponsor
import org.beatonma.commons.repo.converters.toBillStage
import org.beatonma.commons.repo.converters.toBillStageSitting
import org.beatonma.commons.repo.converters.toBillType
import org.beatonma.commons.repo.converters.toParliamentarySession
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.remotesource.api.UkParliamentApi
import org.beatonma.commons.repo.result.DataEmissionStrategy
import org.beatonma.commons.repo.result.NetworkCall
import org.beatonma.commons.repo.result.NetworkCallStrategy
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiBill
import org.beatonma.commons.ukparliament.models.UkApiBillPublication
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BillRepo"

@Singleton
class BillRepository @Inject constructor(
    private val commonsApi: CommonsApi,
    private val ukParliamentApi: UkParliamentApi,
    private val billDao: BillDao,
) {
    fun getBill(billId: ParliamentID): ResultFlow<CompleteBill> = cachedResultFlow(
        databaseQuery = { getCompleteBill(billId) },
        NetworkCall(
            { commonsApi.getBill(billId) },
            { apiBill -> saveBill(billDao, billId, apiBill) }
        ),
        NetworkCall(
            { ukParliamentApi.getBillPublications(billId) },
            { publications ->
                saveBillPublicationsDetail(billDao, billId, publications)
            }
        ),
        callStrategy = NetworkCallStrategy.Serial,
        emitStrategy = DataEmissionStrategy.Continuous
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getCompleteBill(billId: ParliamentID) = channelFlow<CompleteBill> {
        val builder = CompleteBillBuilder()

        suspend fun submitCompleteBill() {
            if (builder.isComplete) {
                send(builder.toCompleteBill())
            }
        }

        suspend fun <E, T: Collection<E>> fetchList(
            func: BillDao.(ParliamentID) -> Flow<T>,
            block: (T) -> Unit,
        ) = launch {
            billDao.func(billId).collect {
                block(it)
                submitCompleteBill()
            }
        }

        suspend fun <T> fetchObject(
            func: BillDao.(ParliamentID) -> Flow<T>,
            block: (T?) -> Unit,
        ) = launch {
            billDao.func(billId).collect {
                block(it)
                submitCompleteBill()
            }
        }

        fetchObject(BillDao::getBill) { builder.bill = it }
        fetchList(BillDao::getBillStages) { builder.stages = it }
        fetchList(BillDao::getBillPublications) {builder.publications = it }
        fetchList(BillDao::getBillSponsors) { builder.sponsors = it }
        fetchObject(BillDao::getBillSession) { builder.session = it }
        fetchObject(BillDao::getBillType) { builder.type = it }
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun saveBill(dao: BillDao, billId: ParliamentID, apiBill: ApiBill) {
        dao.run {
            withNotNull(apiBill.type) {
                insertBillType(it.toBillType())
            }
            withNotNull(apiBill.session) {
                insertParliamentarySession(it.toParliamentarySession())
            }
            insertBill(apiBill.toBill())
            insertBillStages(apiBill.stages.map { apiStage ->
                apiStage.toBillStage(billId)
            })
            insertBillStageSittings(
                apiBill.stages.map { stage ->
                    stage.sittings.map { sitting ->
                        sitting.toBillStageSitting(billStageId = stage.parliamentdotuk)
                    }
                }.flatten()
            )
            insertBillSponsors(apiBill.sponsors.map { it.toBillSponsor(billId = billId) })
            insertOrUpdateBillPublications(apiBill.publications.map { it.toBillPublication(billId = billId) })
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun saveBillPublicationsDetail(
        dao: BillDao,
        billId: ParliamentID,
        publications: List<UkApiBillPublication>
    ) {
        dao.run {
            updateBillPublicationDetail(
                publications.map {
                    it.toBillPublicationDetail(billId)
                }
            )
        }
    }
}
