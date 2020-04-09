package org.beatonma.commons.data.core.room.dao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.BaseRoomDaoTest
import org.beatonma.commons.data.core.room.entities.division.VoteType
import org.beatonma.commons.data.testdata.API_DIVISION
import org.beatonma.commons.data.testdata.DIVISION_PUK
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DivisionDaoInsertApiDivisionTest::class,
)
class DivisionDaoTestSuite


class DivisionDaoInsertApiDivisionTest: BaseRoomDaoTest<DivisionDao>() {
    override val dao: DivisionDao
        get() = db.divisionDao()

    override val testPukId: Int = DIVISION_PUK

    @Before
    override fun setUp() {
        super.setUp()

        runBlocking(Dispatchers.Main) {
            dao.insertApiDivision(testPukId, API_DIVISION)
        }
    }

    @Test
    fun ensure_Division_is_written_and_retrieved_correctly() {
        daoTest(DivisionDao::getDivision) {
            run {
                parliamentdotuk shouldbe 229684
                title shouldbe "Statutory Instruments: Motion for Approval. That the draft Infrastructure Planning (Radioactive Waste Geological Disposal Facilities) Order 2015, which was laid before this House on 12 January, be approved. Q acc agreed to."
                date shouldbe "2015-03-25"
                ayes shouldbe 275
                noes shouldbe 33
                passed shouldbe true
            }
        }
    }

    @Test
    fun ensure_DivisionWithVotes_is_retrieved_correctly() {
        daoTest(DivisionDao::getDivisionWithVotes) {
            division.run {
                parliamentdotuk shouldbe 229684
                title shouldbe "Statutory Instruments: Motion for Approval. That the draft Infrastructure Planning (Radioactive Waste Geological Disposal Facilities) Order 2015, which was laid before this House on 12 January, be approved. Q acc agreed to."
                date shouldbe "2015-03-25"
                ayes shouldbe 275
                noes shouldbe 33
                passed shouldbe true
            }
            votes.size shouldbe 5
            votes.first { it.memberId == 1609 }.run {
                memberName shouldbe "Chloe Smith"
                voteType shouldbe VoteType.Abstains
            }
            votes.first { it.memberId == 3989 }.run {
                memberName shouldbe "Jack Lopresti"
                voteType shouldbe VoteType.AyeVote
            }
            votes.first { it.memberId == 1587 }.run {
                memberName shouldbe "Pat McFadden"
                voteType shouldbe VoteType.DidNotVote
            }
            votes.first { it.memberId == 1503 }.run {
                memberName shouldbe "Mr Jamie Reed"
                voteType shouldbe VoteType.NoVote
            }
            votes.first { it.memberId == 31415926 }.run {
                memberName shouldbe "Fake Member"
                voteType shouldbe VoteType.SuspendedOrExpelledVote
            }
        }
    }
}