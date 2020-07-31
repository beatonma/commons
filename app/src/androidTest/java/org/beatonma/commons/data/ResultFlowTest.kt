package org.beatonma.commons.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.androidTest.assertEach
import org.beatonma.commons.androidTest.awaitValues
import org.beatonma.commons.androidTest.dump
import org.beatonma.commons.data.core.room.entities.member.ApiMemberProfile
import org.beatonma.commons.data.testdata.EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON
import org.beatonma.commons.data.testdata.EXAMPLE_MEMBER_PROFILE_KEIR_STARMER
import org.beatonma.commons.data.testdata.MEMBER_PUK_BORIS_JOHNSON
import org.beatonma.commons.data.testdata.MEMBER_PUK_KEIR_STARMER
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldBeInstanceOf
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CacheResultFlowTest::class,
    ResultFlowLocalPreferredTest::class,
    ResultFlowNoCacheTest::class,
)
class ResultFlowTestSuite


class CacheResultFlowTest: ResultFlowTest() {
    @Test
    fun cachedResultFlow_with_no_cached_value_should_emit_loading_until_network_call_completes() {
        runBlocking {
            cachedResultFlow(
                databaseQuery = { getProfileFromDatabase(MEMBER_PUK_KEIR_STARMER) },
                networkCall = { makeNetworkCall() },
                saveCallResult = ::saveProfiles
            )
                .awaitValues(latchCount = 3, timeoutThrows = false)
                .single()
                .dump()
                .assertEach(
                    { it shouldBeInstanceOf LoadingResult::class },
                    { it.data!!.parliamentdotuk shouldbe MEMBER_PUK_KEIR_STARMER },
                )
        }
    }

    @Test
    fun cachedResultFlow_should_emit_cached_results_then_update_via_network_call_and_emit_new_value() {
        runBlocking {
            // Local cached version
            saveProfiles(listOf(EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON))

            // Network response with different data
            val editedNetworkResult = EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON.copy(
                name = "Joris Bohnson"
            )

            cachedResultFlow(
                databaseQuery = { getProfileFromDatabase(MEMBER_PUK_BORIS_JOHNSON) },
                networkCall = { makeNetworkCall(listOf(editedNetworkResult)) },
                saveCallResult = ::saveProfiles
            )
                .awaitValues(latchCount = 3)
                .single()
                .assertEach(
                    { it shouldBeInstanceOf LoadingResult::class },
                    { it.data!!.name shouldbe "Boris Johnson" }, // Cached before network call
                    { it.data!!.name shouldbe "Joris Bohnson" }, // After network call
                )
        }
    }
}


class ResultFlowLocalPreferredTest: ResultFlowTest() {

    @Test
    fun resultFlowLocalPreferred_should_emit_cached_result_and_skip_network_call() {
        runBlocking {
            // Local cached version
            saveProfiles(listOf(EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON))

            // Network response with different data
            val editedNetworkResult = EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON.copy(
                name = "Joris Bohnson"
            )

            val results = resultFlowLocalPreferred(
                databaseQuery = { getProfileFromDatabase(MEMBER_PUK_BORIS_JOHNSON) },
                networkCall = { makeNetworkCall(listOf(editedNetworkResult)) },
                saveCallResult = ::saveProfiles
            )
                .awaitValues(latchCount = 3, timeout = 500, timeoutThrows = false)
                .single()

            results.assertEach(
                { it shouldBeInstanceOf LoadingResult::class },
                {
                    it.data!!.run {
                        name shouldbe "Boris Johnson" // Original cached data, network call skipped.
                    }
                },
            )
        }
    }

    @Test
    fun resultFlowLocalPreferred_should_get_value_from_network_when_cache_not_available() {
        runBlocking {
            // No local cached version

            // Network response with different data
            val editedNetworkResult = EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON.copy(
                name = "Joris Bohnson"
            )

            resultFlowLocalPreferred(
                databaseQuery = { getProfileFromDatabase(MEMBER_PUK_BORIS_JOHNSON) },
                networkCall = { makeNetworkCall(listOf(editedNetworkResult)) },
                saveCallResult = ::saveProfiles
            ).awaitValues(latchCount = 3, timeout = 500, timeoutThrows = false)
                .single()
                .assertEach(
                    { it shouldBeInstanceOf LoadingResult::class },
                    { it.data!!.name shouldbe "Joris Bohnson" }  // Updated network name - no pre-cached version exists
                )
        }
    }
}


class ResultFlowNoCacheTest: ResultFlowTest() {
    @Test
    fun resultFlowNoCache_should_emit_loading_then_network_result_with_no_database() {
        runBlocking {
            resultFlowNoCache {
                makeNetworkCall(
                    listOf(
                        EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON,
                        EXAMPLE_MEMBER_PROFILE_KEIR_STARMER
                    )
                )
            }.awaitValues(2)
                .single()
                .assertEach(
                    { result -> result shouldBeInstanceOf LoadingResult::class },
                    { result -> result.data!!.assertEach(
                        { it.name shouldbe "Boris Johnson" },
                        { it.name shouldbe "Keir Starmer" },
                    ) }
                )
        }
    }
}


abstract class ResultFlowTest: BaseRoomTest() {

    protected fun getProfileFromDatabase(memberId: ParliamentID) =
        db.memberDao().getMemberProfile(memberId)

    protected suspend fun saveProfiles(profiles: List<ApiMemberProfile>) {
        delay(100)
        db.memberDao().safeInsertProfiles(profiles.map { it.toMemberProfile() }, ifNotExists = false)
    }

    protected suspend fun makeNetworkCall(
        responseData: List<ApiMemberProfile> = listOf(EXAMPLE_MEMBER_PROFILE_KEIR_STARMER)
    ): IoResult<List<ApiMemberProfile>> {
        delay(100)
        return SuccessResult(
            responseData,
            "Fake network result"
        )
    }
}
