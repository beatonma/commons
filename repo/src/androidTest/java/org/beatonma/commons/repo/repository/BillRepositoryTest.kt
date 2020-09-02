package org.beatonma.commons.repo.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.repo.BaseRoomTest
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.androidTest.asDate
import org.beatonma.commons.repo.androidTest.awaitValue
import org.beatonma.commons.repo.testdata.API_BILL
import org.beatonma.commons.repo.testdata.BILL_PUK
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.test.fakeOf
import org.junit.Before
import org.junit.Test

class BillRepositoryTest : BaseRoomTest() {

    lateinit var repository: BillRepository
    private val dao: BillDao
        get() = db.billDao()

    @Before
    override fun setUp() {
        super.setUp()
        repository = BillRepository(
            fakeOf(CommonsApi::class, object {  }),
            db.billDao()
        )

        runBlocking(Dispatchers.Main) {
            repository.saveBill(dao, BILL_PUK, API_BILL)
        }
    }

    @Test
    fun ensure_getCompleteBill_is_constructed_correctly() {
        runBlocking(Dispatchers.Main) {
            repository.getCompleteBill(BILL_PUK)
                .awaitValue(latchCount = 6)
                .single()
                .run {
                    with(bill!!) {
                        parliamentdotuk shouldbe 392545
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

                    with(publications!!) {
                        size shouldbe 1
                        first().run {
                            parliamentdotuk shouldbe 397898
                            title shouldbe "Bill as introduced"
                        }
                    }

                    with(session!!) {
                        name shouldbe "2008-2009"
                        parliamentdotuk shouldbe 377312
                    }

                    with(sponsors!!) {
                        size shouldbe 1
                        first().sponsor.run {
                            parliamentdotuk shouldbe 1727
                            name shouldbe "Baroness Wilcox"
                        }
                    }

                    with(stages!!) {
                        size shouldbe 1
                        first().run {
                            stage.run {
                                parliamentdotuk shouldbe 6697
                                type shouldbe "Order of Commitment discharged"
                            }
                            sittings.first().run {
                                billStageId shouldbe 6697
                                parliamentdotuk shouldbe 8684
                                date shouldbe "2014-05-14".asDate()
                                isFormal shouldbe false
                                isProvisional shouldbe true
                            }
                        }
                    }
                }
        }
    }
}
