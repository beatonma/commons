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

    @Query("""SELECT * FROM bills WHERE bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillWithRelations(parliamentdotuk: Int): LiveData<BillWithSessionAndType>

    @Query("""SELECT * FROM bill_stages WHERE billstage_bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillStages(parliamentdotuk: Int): LiveData<List<BillStageWithSittings>>

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
    suspend fun insertCompleteBill(parliamentdotuk: Int, bill: ApiBill) {
        insertBillType(bill.type)
        insertParliamentarySession(bill.session)

        insertBill(Bill(
            parliamentdotuk = parliamentdotuk,
            title = bill.title,
            description = bill.description,
            actName = bill.actName,
            label = bill.label,
            homepage = bill.homepage,
            date = bill.date,
            ballotNumber = bill.ballotNumber,
            billChapter = bill.billChapter,
            isPrivate = bill.isPrivate,
            isMoneyBill = bill.isMoneyBill,
            publicInvolvementAllowed = bill.publicInvolvementAllowed,
            sessionId = bill.session.parliamentdotuk,
            typeId = bill.type.name
        ))

        insertBillStages(bill.stages.map { apiStage ->
            BillStage(
                billId = parliamentdotuk,
                parliamentdotuk = apiStage.parliamentdotuk,
                type = apiStage.type
            )
        })

        insertBillStageSittings(
        bill.stages.map { stage ->
            stage.sittings.map { sitting ->
                sitting.copy(billStageId = stage.parliamentdotuk)
            }
        }.flatten())

        insertBillSponsors(bill.sponsors.map { it.copy(billId = parliamentdotuk) })
        insertBillPublications(bill.publications.map { it.copy(billId = parliamentdotuk) })
    }
}