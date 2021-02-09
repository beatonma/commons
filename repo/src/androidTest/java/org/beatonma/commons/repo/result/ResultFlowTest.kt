package org.beatonma.commons.repo.result

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.repo.BaseRoomTest
import org.beatonma.commons.repo.converters.toMemberProfile
import org.beatonma.commons.repo.testdata.EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON
import org.beatonma.commons.repo.testdata.EXAMPLE_MEMBER_PROFILE_KEIR_STARMER
import org.beatonma.commons.repo.testdata.MEMBER_PUK_BORIS_JOHNSON
import org.beatonma.commons.repo.testdata.MEMBER_PUK_KEIR_STARMER
import org.beatonma.commons.snommoc.models.ApiMemberProfile
import org.beatonma.commons.test.extensions.assertions.assertEach
import org.beatonma.commons.test.extensions.assertions.assertInstanceOf
import org.beatonma.commons.test.extensions.assertions.shouldBeInstanceOf
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.test.extensions.util.awaitValues
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite
import java.util.concurrent.TimeoutException

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CacheResultFlowTest::class,
    ResultFlowLocalPreferredTest::class,
    ResultFlowNoCacheTest::class,
    NetworkCallsTest::class,
)
class ResultFlowSuite


class CacheResultFlowTest: ResultFlowTest() {
    @Test
    fun cachedResultFlow_with_no_cached_value_should_emit_loading_until_network_call_completes() {
        runBlocking {
            cachedResultFlow(
                databaseQuery = { getProfileFromDatabase(MEMBER_PUK_KEIR_STARMER) },
                networkCall = { makeSuccessfulNetworkCall() },
                saveCallResult = ::saveProfiles
            )
                .awaitValues(latchCount = 3, timeoutThrows = false)
                .single()
                .assertEach(
                    { it shouldbe IoLoading },
                    { (it as Success).data.parliamentdotuk shouldbe MEMBER_PUK_KEIR_STARMER },
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
                networkCall = { makeSuccessfulNetworkCall(listOf(editedNetworkResult)) },
                saveCallResult = ::saveProfiles
            )
                .awaitValues(latchCount = 3)
                .single()
                .assertEach(
                    { it shouldbe IoLoading },
                    { (it as Success).data.name shouldbe "Boris Johnson" }, // Cached before network call
                    { (it as Success).data.name shouldbe "Joris Bohnson" }, // After network call
                )
        }
    }

    @Test
    fun cachedResultFlow_should_emit_cached_result_and_network_errors() {
        runBlocking {
            // Local cached version
            saveProfiles(listOf(EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON))

            cachedResultFlow(
                databaseQuery = { getProfileFromDatabase(MEMBER_PUK_BORIS_JOHNSON) },
                networkCall = ::makeErrorfulNetworkCall,
                saveCallResult = ::saveProfiles
            )
                .awaitValues(latchCount = 3)
                .single()
                .assertEach(
                    { it shouldbe IoLoading },
                    { (it as Success).data.name shouldbe "Boris Johnson" }, // Cached before network call
                    { it shouldBeInstanceOf ErrorCode::class }
                )
        }
    }

    @Test
    fun cachedResultFlow_with_no_cache_should_emit_loading_and_network_errors() {
        runBlocking {
            cachedResultFlow(
                databaseQuery = { getProfileFromDatabase(MEMBER_PUK_BORIS_JOHNSON) },
                networkCall = ::makeErrorfulNetworkCall,
                saveCallResult = ::saveProfiles
            )
                .awaitValues(latchCount = 2, timeoutThrows = false)
                .single()
                .assertEach(
                    { it shouldbe IoLoading },
                    { it shouldBeInstanceOf ErrorCode::class }
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
                networkCall = { makeSuccessfulNetworkCall(listOf(editedNetworkResult)) },
                saveCallResult = ::saveProfiles
            )
                .awaitValues(latchCount = 2, timeoutThrows = false)
                .single()

            results.assertEach(
                { it shouldbe IoLoading },
                { (it as Success).data.name shouldbe "Boris Johnson" },
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
                networkCall = { makeSuccessfulNetworkCall(listOf(editedNetworkResult)) },
                saveCallResult = ::saveProfiles
            ).awaitValues(latchCount = 2, timeoutThrows = false)
                .single()
                .assertEach(
                    { it shouldbe IoLoading },
                    { (it as Success).data.name shouldbe "Joris Bohnson" }  // Updated network name - no pre-cached version exists
                )
        }
    }

    @Test
    fun resultFlowLocalPreferred_should_emit_network_errors() {
        runBlocking {
            // No local cached version

            resultFlowLocalPreferred(
                databaseQuery = { getProfileFromDatabase(MEMBER_PUK_BORIS_JOHNSON) },
                networkCall = ::makeErrorfulNetworkCall,
                saveCallResult = ::saveProfiles
            ).awaitValues(latchCount = 2, timeoutThrows = false)
                .single()
                .assertEach(
                    { it shouldbe IoLoading },
                    { it shouldBeInstanceOf ErrorCode::class }
                )
        }
    }
}


class ResultFlowNoCacheTest: ResultFlowTest() {
    @Test
    fun resultFlowNoCache_should_emit_loading_then_network_result_with_no_database() {
        runBlocking {
            resultFlowNoCache {
                makeSuccessfulNetworkCall(
                    listOf(
                        EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON,
                        EXAMPLE_MEMBER_PROFILE_KEIR_STARMER
                    )
                )
            }.awaitValues(2)
                .single()
                .assertEach(
                    { it shouldbe IoLoading },
                    {
                        (it as Success).data.assertEach(
                            { profile -> profile.name shouldbe "Boris Johnson" },
                            { profile -> profile.name shouldbe "Keir Starmer" },
                        )
                    },
                )
        }
    }


    @Test
    fun resultFlowNoCache_should_emit_loading_then_network_errors() {
        runBlocking {
            resultFlowNoCache { makeErrorfulNetworkCall<Nothing>() }
                .awaitValues(2)
                .single()
                .assertEach(
                    { it shouldbe IoLoading },
                    { it shouldBeInstanceOf ErrorCode::class }
                )
        }
    }
}

class NetworkCallsTest {
    private val results = mutableListOf<Int>()
    private val networkCalls = arrayOf(
        NetworkCall(
            {
                delay(500)
                Success(2)
            }, { n ->
                results += n
            }
        ),
        NetworkCall(
            {
                delay(500)
                Success(3)
            }, { n ->
                results += n
            }
        )
    )

    @Test
    fun makeNetworkCalls_withParallelStrategy_runsInParallel() {
        runBlocking {
            makeNetworkCalls(
                *networkCalls,
                strategy = NetworkCallStrategy.Parallel
            ).awaitValues(2, timeout = 550)
                .collect {
                    it.size shouldbe 2
                    results.sum() shouldbe 5
                }
        }
    }

    @Test
    fun makeNetworkCalls_withSerialStrategy_runsSuccessfully() {
        // Just make sure the tasks run.
        runBlocking {
            makeNetworkCalls(
                *networkCalls,
                strategy = NetworkCallStrategy.Serial
            ).awaitValues(2, timeout = 1100)
                .collect {
                    it.size shouldbe 2
                    results.sum() shouldbe 5
                }
        }
    }

    @Test
    fun makeNetworkCalls_withSerialStrategy_tasksRunSequentially() {
        // awaitValues should time out when tasks run one after another
        runBlocking {
            makeNetworkCalls(
                *networkCalls,
                strategy = NetworkCallStrategy.Serial
            ).awaitValues(2, timeout = 750)
                .catch { e ->
                    println("awaitValue throws: $e")
                    e.assertInstanceOf(TimeoutException::class)
                }
                .collect {
                    it.size shouldbe 1
                    results.sum() shouldbe 2
                }
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

    protected suspend fun makeSuccessfulNetworkCall(
        responseData: List<ApiMemberProfile> = listOf(EXAMPLE_MEMBER_PROFILE_KEIR_STARMER)
    ): IoResult<List<ApiMemberProfile>> {
        delay(100)
        return Success(responseData)
    }

    protected suspend fun <T> makeErrorfulNetworkCall(): IoResult<T> {
        delay(100)
        return ErrorCode(ResponseCode(400), "")
    }
}
