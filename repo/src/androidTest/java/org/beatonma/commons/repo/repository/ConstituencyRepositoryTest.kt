package org.beatonma.commons.repo.repository

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.repo.BaseRoomTest
import org.beatonma.commons.repo.deserialization.api.expected.expectedApiConstituencyElectionResult
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.test.extensions.util.awaitValue
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
            fakeOf(CommonsApi::class, object {  }),
            dao,
            memberDao
        )
    }

    @Test
    fun ensureConstituencyElectionDetails_arePersistedCorrectly() {
        runBlocking(Dispatchers.Main) {
            repository.saveResults(
                dao,
                expectedApiConstituencyElectionResult()
            )
        }

        runBlocking {
            repository.getCachedConstituencyElectionDetails(147277, 19)
                .awaitValue(1)
                .first()
                .run {
                    candidates.size shouldbe 8
                    election.name shouldbe "2010 General Election"
                    constituency.name shouldbe "Uxbridge and South Ruislip"
                    details.turnout shouldbe 45076
                }
        }
    }
}
