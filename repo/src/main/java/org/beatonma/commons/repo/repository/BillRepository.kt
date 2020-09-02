package org.beatonma.commons.repo.repository

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.entities.bill.*
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.FlowIoResult
import org.beatonma.commons.repo.converters.*
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.snommoc.models.ApiBill
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BillRepo"

@Singleton
class BillRepository @Inject constructor(
    private val remoteSource: CommonsApi,
    private val billDao: BillDao,
) {

    fun getBill(parliamentdotuk: ParliamentID): FlowIoResult<CompleteBill> = cachedResultFlow(
        databaseQuery = { getCompleteBill(parliamentdotuk) },
        networkCall = { remoteSource.getBill(parliamentdotuk) },
        saveCallResult = { apiBill -> saveBill(billDao, parliamentdotuk, apiBill) },
        distinctUntilChanged = false,
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getCompleteBill(parliamentdotuk: ParliamentID) = channelFlow<CompleteBill> {
        val builder = CompleteBill()
        val dataSourceFunctions = listOf(
            BillDao::getBill,
            BillDao::getBillStages,
            BillDao::getBillPublications,
            BillDao::getBillSponsors,
            BillDao::getBillSession,
            BillDao::getBillType,
        )

        val mergedFlow = dataSourceFunctions.map { func ->
            func.invoke(billDao, parliamentdotuk)
        }.merge()

        mergedFlow.collect { data: Any? ->
            if (data is List<*>) {
                val first = data.firstOrNull()

                @Suppress("UNCHECKED_CAST")
                when (first) {
                    is BillPublication -> builder.publications = data as List<BillPublication>
                    is BillStageWithSittings -> builder.stages = data as List<BillStageWithSittings>
                    is BillSponsorWithParty -> builder.sponsors = data as List<BillSponsorWithParty>
                    null -> Unit
                    else -> error("All valid types must be handled: ${data.javaClass.canonicalName}")
                }
            }
            else {
                when (data) {
                    is Bill -> builder.bill = data
                    is BillType -> builder.type = data
                    is ParliamentarySession -> builder.session = data
                    null -> Unit
                    else -> error("All valid types must be handled: ${data.javaClass.canonicalName}")
                }
            }

            send(builder)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun saveBill(dao: BillDao, parliamentdotuk: ParliamentID, apiBill: ApiBill) {
        dao.run {
            withNotNull(apiBill.type) {
                insertBillType(it.toBillType())
            }
            withNotNull(apiBill.session) {
                insertParliamentarySession(it.toParliamentarySession())
            }
            insertBill(apiBill.toBill())
            insertBillStages(apiBill.stages.map { apiStage ->
                apiStage.toBillStage(
                    parliamentdotuk
                )
            })
            insertBillStageSittings(
                apiBill.stages.map { stage ->
                    stage.sittings.map { sitting ->
                        sitting.toBillStageSitting(billStageId = stage.parliamentdotuk)
                    }
                }.flatten()
            )
            insertBillSponsors(apiBill.sponsors.map { it.toBillSponsor(billId = parliamentdotuk) })
            insertBillPublications(apiBill.publications.map { it.toBillPublication(billId = parliamentdotuk) })
        }
    }
}
