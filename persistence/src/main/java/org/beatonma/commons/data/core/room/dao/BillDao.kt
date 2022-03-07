package org.beatonma.commons.data.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.allNotNull
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.FlowList
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillData
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationData
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationLink
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorData
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.BillXPublication
import org.beatonma.commons.data.core.room.entities.bill.BillXSession
import org.beatonma.commons.data.core.room.entities.bill.BillXStage
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill


@Dao
interface BillDao {
    @Query("""SELECT * FROM bills WHERE billdata_id = :billId""")
    fun getBillData(billId: ParliamentID): Flow<BillData>

    @Query("""SELECT * FROM bill_types WHERE billtype_id = :typeId""")
    fun getBillType(typeId: ParliamentID): Flow<BillType>

    @Query("""SELECT * FROM bill_stages WHERE billstage_id = :stageId""")
    fun getStage(stageId: ParliamentID): Flow<BillStage>

    @Query("""
        SELECT * FROM parliamentary_sessions
        WHERE session_id = :sessionId""")
    fun getSession(sessionId: ParliamentID): Flow<ParliamentarySession>

    @Query("""
        SELECT * FROM bills_x_sessions
        INNER JOIN parliamentary_sessions ON bills_x_sessions.sessionId = parliamentary_sessions.session_id
        WHERE billId = :billId
        ORDER BY parliamentary_sessions.session_name DESC
    """)
    fun getSessionsForBill(billId: ParliamentID): FlowList<ParliamentarySession>

    @Query("""
        SELECT * FROM bills_x_stages
        INNER JOIN bill_stages ON bills_x_stages.stageId = bill_stages.billstage_id
        WHERE billId = :billId
    """)
    fun getStagesForBill(billId: ParliamentID): FlowList<BillStage>

    @Transaction
    @Query("""
        SELECT * FROM bills_x_publications
        INNER JOIN bill_publications ON bills_x_publications.publicationId = bill_publications.billpub_id
        WHERE billId = :billId
        ORDER BY bill_publications.billpub_date DESC
    """)
    fun getPublicationsForBill(billId: ParliamentID): FlowList<BillPublication>

    @Query("""
        SELECT * FROM bill_sponsors
        LEFT JOIN member_profiles ON bill_sponsors.billsponsor_member_id = member_id
        WHERE billsponsor_bill_id = :billId
    """)
    fun getSponsorsForBill(billId: ParliamentID): FlowList<BillSponsor>

    @Transaction
    @Query("""SELECT * FROM zeitgeist_bills""")
    fun getZeitgeistBills(): FlowList<ZeitgeistBill>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillData(bill: BillData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillData(bills: List<BillData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillType(type: BillType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillStages(stages: List<BillStage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSessions(sessions: List<ParliamentarySession>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillXSessions(sessions: List<BillXSession>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillXStages(stages: List<BillXStage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillXPublications(publications: List<BillXPublication>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillPublications(publications: List<BillPublicationData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillPublicationLinks(links: List<BillPublicationLink>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBillSponsorData(data: List<BillSponsorData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZeitgeistBills(zeitgeistBills: List<ZeitgeistBill>)


    @Transaction
    fun insertBill(
        data: BillData,
        type: BillType,
        stages: List<BillStage>,
        sessions: List<ParliamentarySession>,
        publications: List<BillPublicationData>,
        pubLinks: List<BillPublicationLink>,
        sponsorData: List<BillSponsorData>,
    ) {
        insertBillType(type)
        insertSessions(sessions)
        insertBillData(data);
        insertBillStages(stages)
        insertBillPublications(publications)
        insertBillPublicationLinks(pubLinks)
        insertBillSponsorData(sponsorData)

        insertBillXSessions(sessions.map { session ->
            BillXSession(
                billId = data.id,
                sessionId = session.id
            )
        })

        insertBillXStages(stages.map { stage ->
            BillXStage(
                billId = data.id,
                stageId = stage.parliamentdotuk
            )
        })

        insertBillXPublications(publications.map { pub ->
            BillXPublication(
                billId = data.id,
                publicationId = pub.parliamentdotuk
            )
        })
    }

    @Transaction
    fun insertBill(bill: Bill) {
        with(bill) {
            insertBill(
                data = data,
                type = type,
                stages = stages,
                sessions = sessions,
                publications = publications.map(BillPublication::data),
                pubLinks = publications.map(BillPublication::links).flatten(),
                sponsorData = sponsors.map { sponsor ->
                    BillSponsorData(
                        id = sponsor.id,
                        billId = this.parliamentdotuk,
                        memberId = sponsor.member?.parliamentdotuk,
                        organisation = sponsor.organisation,
                    )
                }
            )
        }
    }

    @Transaction
    fun getBill(billId: ParliamentID): Flow<Bill> = channelFlow {
        val builder = BillBuilder()

        suspend fun submit() {
            if (builder.isComplete) {
                send(builder.toBill())
                close()
            }
        }

        suspend fun <E, T : Collection<E>> fetch(
            id: ParliamentID,
            func: (ParliamentID) -> Flow<T>,
            block: (T) -> Unit,
        ) = launch {
            func(id).collectLatest {
                block(it)
                submit()
            }
        }

        suspend fun <T> fetchObject(
            id: ParliamentID,
            func: (ParliamentID) -> Flow<T>,
            block: suspend (T?) -> Unit,
        ) = launch {
            func(id).collectLatest {
                block(it)
                submit()
            }
        }

        fetchObject(billId, ::getBillData) { billdata ->
            builder.data = billdata

            withNotNull(billdata) { data ->
                fetchObject(data.billTypeId, ::getBillType) {
                    builder.type = it
                }
                fetchObject(data.currentStageId, ::getStage) {
                    builder.currentStage = it
                }
                fetchObject(data.sessionIntroducedId, ::getSession) {
                    builder.sessionIntroduced = it
                }
                fetch(data.id, ::getSessionsForBill) {
                    builder.sessions = it
                }
                fetch(data.id, ::getStagesForBill) {
                    builder.stages = it
                }
                fetch(data.id, ::getPublicationsForBill) {
                    builder.publications = it
                }
                fetch(data.id, ::getSponsorsForBill) {
                    builder.sponsors = it
                }
            }
        }
    }

    private class BillBuilder {
        var data: BillData? = null
        var type: BillType? = null
        var currentStage: BillStage? = null
        var sessionIntroduced: ParliamentarySession? = null
        var sessions: List<ParliamentarySession>? = null
        var stages: List<BillStage>? = null
        var publications: List<BillPublication>? = null
        var sponsors: List<BillSponsor>? = null

        val isComplete: Boolean
            get() = allNotNull(
                data,
                type,
                currentStage,
                sessionIntroduced,
                sessions,
                stages,
                publications,
                sponsors,
            )

        fun toBill(): Bill = Bill(
            data = data!!,
            type = type!!,
            currentStage = currentStage!!,
            sessionIntroduced = sessionIntroduced!!,
            sessions = sessions!!,
            stages = stages!!,
            publications = publications!!,
            sponsors = sponsors!!,
        )
    }
}
