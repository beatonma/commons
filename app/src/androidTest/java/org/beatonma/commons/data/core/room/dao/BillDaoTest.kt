package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.androidTest.getOrAwaitValue
import org.beatonma.commons.data.BaseRoomDaoTest
import org.beatonma.commons.data.core.room.dao.testdata.API_BILL
import org.beatonma.commons.data.core.room.dao.testdata.BILL_PUK
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BillDaoInsertCompleteBillTest::class,
)
class BillDaoTestSuite

class BillDaoInsertCompleteBillTest: BaseRoomDaoTest<BillDao>() {
    override val dao: BillDao
        get() = db.billDao()

    @Before
    override fun setUp() {
        super.setUp()
        runBlocking {
            dao.insertCompleteBill(BILL_PUK, API_BILL)
        }
    }

    private fun <T> daoTest(func: BillDao.(Int) -> LiveData<T>, testBlock: T.() -> Unit) =
        dao.func(BILL_PUK).getOrAwaitValue { testBlock(this) }

    @Test
    fun ensure_Bill_is_written_and_retrieved_correctly() {
        daoTest(BillDao::getBill) {
            parliamentdotuk shouldbe BILL_PUK
            title shouldbe "Presumption of Death"
            description shouldbe "A Bill to make provision in relation to the presumed deaths of missing persons; and for connected purposes."
            actName shouldbe "Deep Sea Mining Act"
            label shouldbe "Presumption of Death"
            homepage shouldbe "http://services.parliament.uk/bills/presumptionofdeath.html"
            date shouldbe "2009-06-25"
            ballotNumber shouldbe 4
            billChapter shouldbe "15"
            isPrivate shouldbe false
            isMoneyBill shouldbe false
            publicInvolvementAllowed shouldbe true
        }
    }

    @Test
    fun ensure_BillPublication_is_written_and_retrieved_correctly() {
        daoTest(BillDao::getBillPublications) {
            first().run {
                billId shouldbe BILL_PUK
                parliamentdotuk shouldbe 397898
                title shouldbe "Bill as introduced"
            }
        }
    }

    @Test
    fun ensure_BillWithSessionAndType_is_written_and_retrieved_correctly() {
        daoTest(BillDao::getBillWithRelations) {
            bill.run {
                parliamentdotuk shouldbe BILL_PUK
                title shouldbe "Presumption of Death"
                description shouldbe "A Bill to make provision in relation to the presumed deaths of missing persons; and for connected purposes."
                actName shouldbe "Deep Sea Mining Act"
                label shouldbe "Presumption of Death"
                homepage shouldbe "http://services.parliament.uk/bills/presumptionofdeath.html"
                date shouldbe "2009-06-25"
                ballotNumber shouldbe 4
                billChapter shouldbe "15"
                isPrivate shouldbe false
                isMoneyBill shouldbe false
                publicInvolvementAllowed shouldbe true
            }
            session.run {
                parliamentdotuk shouldbe 377312
                name shouldbe "2008-2009"
            }
            type.run {
                name shouldbe "Ballot"
                description shouldbe "Private Members' Bill (Ballot Bill)"
            }
        }
    }

    @Test
    fun ensure_BillSponsor_is_written_and_retrieved_correctly() {
        daoTest(BillDao::getBillSponsors) {
            first().run {
                billId shouldbe BILL_PUK
                parliamentdotuk shouldbe 1727
                name shouldbe "Baroness Wilcox"
            }
        }
    }

    @Test
    fun ensure_BillStage_and_BillStageSitting_is_written_and_retrieved_correctly() {
        daoTest(BillDao::getBillStages) {
            val first = first()
            first.stage.run {
                parliamentdotuk shouldbe 6697
                type shouldbe "Order of Commitment discharged"
            }
            first.sittings.first().run {
                billStageId shouldbe 6697
                parliamentdotuk shouldbe 8684
                date shouldbe "2014-05-14"
                isFormal shouldbe false
                isProvisional shouldbe true
            }
        }
    }
}