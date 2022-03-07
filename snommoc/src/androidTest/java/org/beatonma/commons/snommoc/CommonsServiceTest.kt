package org.beatonma.commons.snommoc

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.core.House
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Tests that make actual network calls to make sure we are able to read the actual API correctly.
 * Ensures that any API-adjacent changes on either end do not slip passed unnoticed.
 */
@HiltAndroidTest
class CommonsServiceTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var service: CommonsService

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun member_is_retrieved() {
        runBlocking {
            service.getMember(1423).body()!!.profile.parliamentdotuk shouldbe 1423
        }
    }

    @Test
    fun bill_is_retrieved() {
        runBlocking {
            service.getBill(2835).body()!!.parliamentdotuk shouldbe 2835
        }
    }

    @Test
    fun commonsdivision_is_retrieved() {
        runBlocking {
            service.getDivision(House.commons, 161145).body()!!.parliamentdotuk shouldbe 161145
        }
    }

    @Test
    fun lordsdivision_is_retrieved() {
        runBlocking {
            service.getDivision(House.lords, 2710).body()!!.parliamentdotuk shouldbe 2710
        }
    }

    @Test
    fun constituency_is_retrieved() {
        runBlocking {
            service.getConstituency(147277).body()!!.parliamentdotuk shouldbe 147277
        }
    }

    @Test
    fun constituency_election_result_is_retrieved() {
        runBlocking {
            service.getConstituencyElectionResults(147277, 19).body()!!.run {
                constituency.parliamentdotuk shouldbe 147277
                election.parliamentdotuk shouldbe 19
            }
        }
    }
}
