package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.LiveDataList
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.*

@Dao
interface BillDao {

    @Transaction
    @Query("""SELECT * FROM featured_bills""")
    fun getFeaturedBills(): LiveDataList<FeaturedBillWithBill>

    @Query("""SELECT * FROM bills WHERE bill_parliamentdotuk = :parliamentdotuk""")
    fun getBill(parliamentdotuk: ParliamentID): LiveData<Bill>

    @Query("""SELECT * FROM bill_publications WHERE bill_pub_bill_id = :parliamentdotuk""")
    fun getBillPublications(parliamentdotuk: ParliamentID): LiveDataList<BillPublication>

    @Query("""SELECT * FROM bill_sponsors WHERE sponsor_bill_id = :parliamentdotuk""")
    fun getBillSponsors(parliamentdotuk: ParliamentID): LiveDataList<BillSponsor>

    @Transaction
    @Query("""SELECT * FROM bills WHERE bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillWithRelations(parliamentdotuk: ParliamentID): LiveData<BillWithSessionAndType>

    @Transaction
    @Query("""SELECT * FROM bill_stages WHERE billstage_bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillStages(parliamentdotuk: ParliamentID): LiveDataList<BillStageWithSittings>

    @Query("""SELECT * FROM parliamentary_sessions
        LEFT JOIN bills ON bills.bill_session_id = parliamentary_sessions.session_parliamentdotuk
        WHERE bills.bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillSession(parliamentdotuk: ParliamentID): LiveData<ParliamentarySession>

    @Query("""SELECT * FROM bill_types
        LEFT JOIN bills ON bills.bill_type_id = bill_types.billtype_name
        WHERE bills.bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillType(parliamentdotuk: ParliamentID): LiveData<BillType>

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
    suspend fun insertCompleteBill(parliamentdotuk: ParliamentID, apiBill: ApiBill) {
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
                sitting.toBillStageSitting(billStageId = stage.parliamentdotuk)
            }
        }.flatten())

        insertBillSponsors(apiBill.sponsors.map { it.toBillSponsor(billId = parliamentdotuk) })
        insertBillPublications(apiBill.publications.map { it.toBillPublication(billId = parliamentdotuk) })
    }
}
