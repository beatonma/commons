package org.beatonma.commons.data.dao

import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.sampledata.SampleBill
import org.beatonma.commons.test.extensions.assertions.shouldAllBe
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Test


class BillDaoTest : BaseRoomDaoTest<BillDao>() {
    override val dao: BillDao
        get() = db.billDao()

    @Before
    override fun setUp() {
        super.setUp()

        runBlocking {
            dao.insertBill(SampleBill)
        }
    }

    @Test
    fun bill_inserted_and_retrieved_correctly() {
        runQuery(
            { dao.getBill(SampleBill.parliamentdotuk) }
        ) {
            val expected = SampleBill
            data shouldbe expected.data
            type shouldbe expected.type
            currentStage shouldbe expected.currentStage
            stages shouldbe expected.stages
            sessionIntroduced shouldbe expected.sessionIntroduced
            sessions shouldAllBe expected.sessions
            publications shouldAllBe expected.publications
            with(sponsors.first()) {
                id shouldbe 1
                // Member cannot be retrieved here
                organisation shouldbe expected.sponsors.first().organisation
            }
        }
    }
}
