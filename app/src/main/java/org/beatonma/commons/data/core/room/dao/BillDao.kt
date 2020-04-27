package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.room.entities.bill.*

@Dao
interface BillDao {

    @Transaction
    @Query("""SELECT * FROM featured_bills""")
    fun getFeaturedBills(): LiveData<List<FeaturedBillWithBill>>

    @Query("""SELECT * FROM bills WHERE bill_parliamentdotuk = :parliamentdotuk""")
    fun getBill(parliamentdotuk: Int): LiveData<Bill>

    @Query("""SELECT * FROM bill_publications WHERE bill_pub_bill_id = :parliamentdotuk""")
    fun getBillPublications(parliamentdotuk: Int): LiveData<List<BillPublication>>

    @Query("""SELECT * FROM bill_sponsors WHERE sponsor_bill_id = :parliamentdotuk""")
    fun getBillSponsors(parliamentdotuk: Int): LiveData<List<BillSponsor>>

    @Transaction
    @Query("""SELECT * FROM bills WHERE bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillWithRelations(parliamentdotuk: Int): LiveData<BillWithSessionAndType>

    @Transaction
    @Query("""SELECT * FROM bill_stages WHERE billstage_bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillStages(parliamentdotuk: Int): LiveData<List<BillStageWithSittings>>

    @Query("""SELECT * FROM parliamentary_sessions 
        LEFT JOIN bills ON bills.bill_session_id = session_parliamentdotuk
        WHERE bills.bill_parliamentdotuk = :parliamentdotuk
        """)
    fun getBillSession(parliamentdotuk: Int): LiveData<ParliamentarySession>

    @Query("""SELECT * FROM bill_types
        LEFT JOIN bills ON bills.bill_type_id = billtype_name
        WHERE bills.bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillType(parliamentdotuk: Int): LiveData<BillType>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<Bill>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: Bill)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeaturedBills(featuredBills: List<FeaturedBill>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillStages(billStages: List<BillStage>)
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillStageSittings(billStageSittings: List<BillStageSitting>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillSponsors(billSponsors: List<BillSponsor>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillPublications(billPublications: List<BillPublication>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillType(billType: BillType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParliamentarySession(parliamentarySession: ParliamentarySession)

    @Transaction
    suspend fun insertCompleteBill(parliamentdotuk: Int, apiBill: ApiBill) {
        if (apiBill.type != null) {
            insertBillType(apiBill.type)
        }
        if (apiBill.session != null) {
            insertParliamentarySession(apiBill.session)
        }

        insertBill(apiBill.toBill())

        insertBillStages(apiBill.stages.map { apiStage -> apiStage.toBillStage(parliamentdotuk) })

        insertBillStageSittings(
        apiBill.stages.map { stage ->
            stage.sittings.map { sitting ->
                sitting.copy(billStageId = stage.parliamentdotuk)
            }
        }.flatten())

        insertBillSponsors(apiBill.sponsors.map { it.copy(billId = parliamentdotuk) })
        insertBillPublications(apiBill.publications.map { it.copy(billId = parliamentdotuk) })
    }
}
