package org.beatonma.commons.data.core.repository

import androidx.lifecycle.LiveData
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.livedata.observeComplete
import org.beatonma.commons.data.resultLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val billDao: BillDao,
) {
    fun observeBill(parliamentdotuk: ParliamentID): LiveDataIoResult<CompleteBill> = resultLiveData(
        databaseQuery = { observeCompleteBill(parliamentdotuk) },
        networkCall = { remoteSource.getBill(parliamentdotuk) },
        saveCallResult = { bill -> billDao.insertCompleteBill(parliamentdotuk, bill) }
    )

    private fun observeCompleteBill(parliamentdotuk: ParliamentID): LiveData<CompleteBill> =
        observeComplete(
            CompleteBill(),
            updatePredicate = { b -> b.bill != null },
        ) { bill ->
            addSource(billDao.getBill(parliamentdotuk)) { _bill ->
                bill.update { copy(bill = _bill) }
            }
            addSource(billDao.getBillType(parliamentdotuk)) { type ->
                bill.update { copy(type = type) }
            }
            addSource(billDao.getBillSession(parliamentdotuk)) { session ->
                bill.update { copy(session = session) }
            }
            addSource(billDao.getBillPublications(parliamentdotuk)) { publications ->
                bill.update { copy(publications = publications) }
            }
            addSource(billDao.getBillSponsors(parliamentdotuk)) { sponsors ->
                bill.update { copy(sponsors = sponsors) }
            }
            addSource(billDao.getBillStages(parliamentdotuk)) { stages ->
                bill.update { copy(stages = stages) }
            }
        }
}
