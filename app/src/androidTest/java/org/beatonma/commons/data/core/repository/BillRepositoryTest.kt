package org.beatonma.commons.data.core.repository

import fakeIt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.androidTest.asDate
import org.beatonma.commons.androidTest.getOrAwaitValue
import org.beatonma.commons.data.BaseRoomTest
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.testdata.API_BILL
import org.beatonma.commons.data.testdata.BILL_PUK
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
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
            fakeIt(CommonsRemoteDataSource::class, object {

            }),
            db.billDao()
        )

        runBlocking(Dispatchers.Main) {
            dao.insertCompleteBill(BILL_PUK, API_BILL)
        }
    }

    @Test
    fun ensure_observeCompleteBill_mediatorLiveData_is_constructed_correctly() {
        runBlocking(Dispatchers.Main) {
            val data = repository.observeCompleteBill(BILL_PUK)

            data.getOrAwaitValue {
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
                    first().run {
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
