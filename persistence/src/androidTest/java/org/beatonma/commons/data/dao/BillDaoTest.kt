package org.beatonma.commons.data.dao

import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillStageSitting
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.test.extensions.util.asDate
import org.junit.Before
import org.junit.Test

private const val BILL_ID = 393258


class BillDaoTest: BaseRoomDaoTest<BillDao>() {
    override val dao: BillDao
        get() = db.billDao()

    @Before
    override fun setUp() {
        super.setUp()
        runBlocking {
            dao.insertParliamentarySession(
                ParliamentarySession(377316, "2013-2014")
            )
            dao.insertBillType(
                BillType(
                    name = "Ballot",
                    description = "Private Members' Bill (Ballot Bill)"
                )
            )

            dao.insertBill(
                Bill(
                    parliamentdotuk = BILL_ID,
                    title = "Presumption of Death",
                    description = "A Bill to make provision in relation to the presumed deaths of missing persons; and for connected purposes.",
                    actName = "Deep Sea Mining Act",
                    label = "Presumption of Death",
                    homepage = "http://services.parliament.uk/bills/presumptionofdeath.html",
                    date = "2009-06-25".asDate(),
                    ballotNumber = 4,
                    billChapter = "15",
                    isPrivate = false,
                    isMoneyBill = false,
                    publicInvolvementAllowed = true,

                    sessionId = 377316,
                    typeId = "Ballot"
                )
            )
        }
    }

    @Test
    fun ensure_Bill_is_written_and_retrieved_correctly() {
        runQuery({ dao.getBill(BILL_ID) }) {
            parliamentdotuk shouldbe BILL_ID
            title shouldbe "Presumption of Death"
            description shouldbe "A Bill to make provision in relation to the presumed deaths of missing persons; and for connected purposes."
            actName shouldbe "Deep Sea Mining Act"
            label shouldbe "Presumption of Death"
            homepage shouldbe "http://services.parliament.uk/bills/presumptionofdeath.html"
            date shouldbe "2009-06-25".asDate()
            ballotNumber shouldbe 4
            billChapter shouldbe "15"
            isPrivate shouldbe false
            isMoneyBill shouldbe false
            publicInvolvementAllowed shouldbe true
        }
    }

    @Test
    fun ensure_BillPublication_is_written_and_retrieved_correctly() {
        runInsert(BillDao::insertBillPublications) {
            listOf(
                BillPublication(
                    billId = BILL_ID,
                    parliamentdotuk = 397898,
                    title = "Bill as introduced"
                )
            )}

        runQuery({ dao.getBillPublications(BILL_ID) }) {
            first().run {
                billId shouldbe BILL_ID
                parliamentdotuk shouldbe 397898
                title shouldbe "Bill as introduced"
            }
        }
    }

    @Test
    fun ensure_BillSponsor_is_written_and_retrieved_correctly() {
        val expected = BillSponsor(
            billId = 393258,
            parliamentdotuk = 1727,
            name = "Baroness Wilcox",
            partyId = 123
        )

        runInsert(BillDao::insertBillSponsors) { listOf(expected) }

        runQuery( { dao.getBillSponsors(393258) }) {
            first().sponsor.run {
                billId shouldbe expected.billId
                parliamentdotuk shouldbe expected.parliamentdotuk
                name shouldbe expected.name
                partyId shouldbe expected.partyId
            }
        }
    }

    @Test
    fun ensure_BillStage_and_BillStageSitting_is_written_and_retrieved_correctly() {
        runInsert(BillDao::insertBillStages) {
            listOf(
                BillStage(
                    billId = 393258,
                    parliamentdotuk = 6697,
                    type = "Order of Commitment discharged",
                )
            )
        }

        runInsert(BillDao::insertBillStageSittings) {
            listOf(
                BillStageSitting(
                    billStageId = 6697,
                    parliamentdotuk = 8684,
                    date = "2014-05-14".asDate(),
                    isFormal = false,
                    isProvisional = true,
                )
            )
        }

        runQuery({ dao.getBillStages(BILL_ID) }) {
            val first = first()
            first.stage.run {
                parliamentdotuk shouldbe 6697
                type shouldbe "Order of Commitment discharged"
            }
            first.sittings.first().run {
                billStageId shouldbe 6697
                parliamentdotuk shouldbe 8684
                date shouldbe "2014-05-14".asDate()
                isFormal shouldbe false
                isProvisional shouldbe true
            }
        }
    }

    @Test
    fun ensure_ParliamentarySession_is_written_and_retrieved_correctly() {
        runQuery({ dao.getBillSession(BILL_ID) }) {
            name shouldbe "2013-2014"
            parliamentdotuk shouldbe 377316
        }
    }

    @Test
    fun ensure_BillType_is_written_and_retrieved_correctly() {
        runQuery({ dao.getBillType(BILL_ID) }) {
            name shouldbe "Ballot"
            description shouldbe "Private Members' Bill (Ballot Bill)"
        }
    }
}
