package org.beatonma.commons.data.deserialization.api

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.deserialization.api.expected.*
import org.beatonma.commons.network.retrofit.CommonsService
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 * Tests that make actual network calls to make sure we are able to read the actual API correctly.
 * Ensures that any API-adjacent changes on either end do not slip passed unnoticed.
 */
@HiltAndroidTest
class LiveDeserializationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var service: CommonsService

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun ensure_bill_instance_is_deserialized_correctly() {
        runBlocking {
            val actualBill = service.getBill(393258).body()!!
            val expectedBill = expectedApiBill()

            actualBill shouldbe expectedBill
        }
    }

    @Test
    fun ensure_commonsdivision_instance_is_deserialized_correctly() {
        runBlocking {
            val actualDivision = service.getDivision(House.commons, 161145).body()!!
            val expectedDivision = expectedApiDivisionCommons()

            actualDivision shouldbe expectedDivision
        }
    }

    @Test
    fun ensure_lordsdivision_instance_is_deserialized_correctly() {
        runBlocking {
            val actualDivision = service.getDivision(House.lords, 712319).body()!!
            val expectedDivision = expectedApiDivisionLords()

            actualDivision shouldbe expectedDivision
        }
    }

    @Test
    fun ensure_constituency_instance_is_deserialized_correctly() {
        runBlocking {
            val actualConstituency = service.getConstituency(147277).body()!!
            val expectedConstituency = expectedApiConstituency()


            actualConstituency.boundary shouldbe expectedConstituency.boundary
            actualConstituency.memberProfile shouldbe expectedConstituency.memberProfile
            actualConstituency.results shouldbe expectedConstituency.results
            actualConstituency shouldbe expectedConstituency
        }
    }

    @Test
    fun ensure_member_instance_is_deserialized_correctly() {
        runBlocking {
            val actualMember = service.getMember(965).body()!!
            val expectedMember = expectedApiMember()

            actualMember.profile shouldbe expectedMember.profile
            actualMember.addresses shouldbe expectedMember.addresses
            actualMember.committees shouldbe expectedMember.committees
            actualMember.houses shouldbe expectedMember.houses
            actualMember.financialInterests shouldbe expectedMember.financialInterests
            actualMember.experiences shouldbe expectedMember.experiences
            actualMember.topicsOfInterest shouldbe expectedMember.topicsOfInterest
            actualMember.constituencies shouldbe expectedMember.constituencies
            actualMember.parties shouldbe expectedMember.parties
            actualMember shouldbe expectedMember
        }
    }

    @Test
    fun ensure_constituency_election_result_is_deserialized_correctly() {
        runBlocking {
            val actualResult = service.getConstituencyElectionResults(electionId = 19, constituencyId = 147277).body()!!
            val expectedResult = expectedApiConstituencyElectionResult()

            actualResult.candidates shouldbe expectedResult.candidates
            actualResult.election shouldbe expectedResult.election
            actualResult.constituency shouldbe expectedResult.constituency
            actualResult shouldbe expectedResult
        }
    }
}
