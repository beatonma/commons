package org.beatonma.commons.repo.repository

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.repo.BaseRoomTest
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.result.Success
import org.beatonma.commons.sampledata.SampleApiConstituencyElectionDetails
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.test.fakeOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ConstituencyRepositoryTest: BaseRoomTest() {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    lateinit var repository: ConstituencyRepository

    private val dao: ConstituencyDao
        get() = db.constituencyDao()

    private val memberDao: MemberDao
        get() = db.memberDao()

    @Before
    override fun setUp() {
        super.setUp()

        repository = ConstituencyRepository(
            @Suppress("RedundantSuspendModifier", "unused", "UNUSED_PARAMETER")
            fakeOf(CommonsApi::class, object {
                suspend fun getConstituencyDetailsForElection(cid: Int, eid: Int) =
                    Success(SampleApiConstituencyElectionDetails)
            }),
            dao,
            memberDao
        )
    }

    @Test
    fun ensureConstituencyElectionDetails_arePersistedCorrectly() {
        runQuery(
            { repository.getConstituencyResultsForElection(147277, 19) }
        ) { data ->
            data.candidates.size shouldbe 8
            data.election.name shouldbe "2010 General Election"
            data.constituency.name shouldbe "Uxbridge and South Ruislip"
            data.details.turnout shouldbe 45076
        }
    }
}
