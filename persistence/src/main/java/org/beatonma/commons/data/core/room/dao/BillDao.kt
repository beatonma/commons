package org.beatonma.commons.data.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.data.FlowList
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationBasic
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationDetail
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillStageSitting
import org.beatonma.commons.data.core.room.entities.bill.BillStageWithSittings
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.data.core.room.entities.bill.ResolvedZeitgeistBill
import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill

@Dao
interface BillDao {

    @Transaction
    @Query("""SELECT * FROM zeitgeist_bills""")
    fun getZeitgeistBills(): FlowList<ResolvedZeitgeistBill>

    @Query("""SELECT * FROM bills WHERE bill_parliamentdotuk = :billId""")
    fun getBill(billId: ParliamentID): Flow<Bill>

    @Query("""SELECT * FROM bill_publications WHERE bill_pub_bill_id = :billId""")
    fun getBillPublications(billId: ParliamentID): FlowList<BillPublication>

    @Query("""SELECT * FROM bill_sponsors WHERE sponsor_bill_id = :billId""")
    fun getBillSponsors(billId: ParliamentID): FlowList<BillSponsorWithParty>

    @Transaction
    @Query("""SELECT * FROM bill_stages WHERE billstage_bill_parliamentdotuk = :billId""")
    fun getBillStages(billId: ParliamentID): FlowList<BillStageWithSittings>

    @Query("""SELECT * FROM parliamentary_sessions
        LEFT JOIN bills ON bills.bill_session_id = parliamentary_sessions.session_parliamentdotuk
        WHERE bills.bill_parliamentdotuk = :billId""")
    fun getBillSession(billId: ParliamentID): Flow<ParliamentarySession>

    @Query("""SELECT * FROM bill_types
        LEFT JOIN bills ON bills.bill_type_id = bill_types.billtype_name
        WHERE bills.bill_parliamentdotuk = :billId""")
    fun getBillType(billId: ParliamentID): Flow<BillType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<Bill>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: Bill)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillStages(billStages: List<BillStage>)
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillStageSittings(billStageSittings: List<BillStageSitting>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillSponsors(billSponsors: List<BillSponsor>)

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = BillPublication::class)
    suspend fun insertBillPublications(billPublications: List<BillPublicationBasic>): List<Long>

    @Update(entity = BillPublication::class)
    suspend fun updateBillPublications(billPublications: List<BillPublicationBasic>)

    @Update(entity = BillPublication::class)
    suspend fun updateBillPublicationDetail(publications: List<BillPublicationDetail>): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillType(billType: BillType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParliamentarySession(parliamentarySession: ParliamentarySession)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZeitgeistBills(zeitgeistBills: List<ZeitgeistBill>)

    @Transaction
    suspend fun insertOrUpdateBillPublications(publications: List<BillPublicationBasic>) {
        val results = insertBillPublications(publications)

        val runAsUpdate = mutableListOf<BillPublicationBasic>()
        results.fastForEachIndexed { index, result ->
            if (result < 0) {
                runAsUpdate += publications[index]
            }
        }

        updateBillPublications(runAsUpdate)
    }
}
