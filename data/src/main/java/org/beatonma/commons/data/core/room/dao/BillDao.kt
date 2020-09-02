package org.beatonma.commons.data.core.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.FlowList
import org.beatonma.commons.data.core.room.entities.bill.*

@Dao
interface BillDao {

    @Transaction
    @Query("""SELECT * FROM featured_bills""")
    fun getFeaturedBills(): FlowList<FeaturedBillWithBill>

    @Query("""SELECT * FROM bills WHERE bill_parliamentdotuk = :parliamentdotuk""")
    fun getBill(parliamentdotuk: ParliamentID): Flow<Bill>

    @Query("""SELECT * FROM bill_publications WHERE bill_pub_bill_id = :parliamentdotuk""")
    fun getBillPublications(parliamentdotuk: ParliamentID): FlowList<BillPublication>

    @Query("""SELECT * FROM bill_sponsors WHERE sponsor_bill_id = :parliamentdotuk""")
    fun getBillSponsors(parliamentdotuk: ParliamentID): FlowList<BillSponsorWithParty>

    @Transaction
    @Query("""SELECT * FROM bill_stages WHERE billstage_bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillStages(parliamentdotuk: ParliamentID): FlowList<BillStageWithSittings>

    @Query("""SELECT * FROM parliamentary_sessions
        LEFT JOIN bills ON bills.bill_session_id = parliamentary_sessions.session_parliamentdotuk
        WHERE bills.bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillSession(parliamentdotuk: ParliamentID): Flow<ParliamentarySession>

    @Query("""SELECT * FROM bill_types
        LEFT JOIN bills ON bills.bill_type_id = bill_types.billtype_name
        WHERE bills.bill_parliamentdotuk = :parliamentdotuk""")
    fun getBillType(parliamentdotuk: ParliamentID): Flow<BillType>

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
}
