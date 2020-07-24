package org.beatonma.commons.data.core.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.merge
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.FlowIoResult
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.cachedResultFlow
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.entities.bill.*
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BillRepo"

@Singleton
class BillRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val billDao: BillDao,
) {

    fun getBill(parliamentdotuk: ParliamentID): FlowIoResult<CompleteBill> = cachedResultFlow(
        databaseQuery = { getCompleteBillFlow(parliamentdotuk) },
        networkCall = { remoteSource.getBill(parliamentdotuk) },
        saveCallResult = { bill -> billDao.insertCompleteBill(parliamentdotuk, bill) }
    ).flowOn(Dispatchers.IO)

    fun getCompleteBillFlow(parliamentdotuk: ParliamentID) = channelFlow<CompleteBill> {
        val builder = CompleteBill()
        val dataSourceFunctions = listOf(
            BillDao::getBillFlow,
            BillDao::getBillStagesFlow,
            BillDao::getBillPublicationsFlow,
            BillDao::getBillSponsorsFlow,
            BillDao::getBillSessionFlow,
            BillDao::getBillTypeFlow,
        )

        val mergedFlow = dataSourceFunctions.map {
            it.invoke(billDao, parliamentdotuk)
        }.merge()

        mergedFlow.collect { data ->
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
    }.flowOn(Dispatchers.IO)
}
