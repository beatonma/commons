package org.beatonma.commons.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.beatonma.lib.testing.kotlin.extensions.assertions.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite


fun String.assertContains(substring: String, ignoreCase: Boolean = false) {
    contains(substring, ignoreCase).assertTrue()
}

fun String.assertMatches(regex: Regex) {
    matches(regex).assertTrue()
}


@RunWith(Suite::class)
@Suite.SuiteClasses(
    HttpClientTest::class,
    TwfyHttpClientTest::class
)
class PeopleRoomTestSuite

abstract class BaseHttpClientTest {
    val mockServer = MockWebServer()
    abstract val httpClient: OkHttpClient

    @Before
    fun setUp() {
        mockServer.apply {
            start()
            enqueue(MockResponse())
        }
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}


open class HttpClientTest: BaseHttpClientTest() {
    override val httpClient = HttpClientModule().provideHttpClient()

    /**
     * Ensure that all requests include the user agent for this app.
     */
    @Test
    fun httpClient_hasCorrectUserAgentHeader() {
        httpClient.newCall(
            Request.Builder()
                .url(mockServer.url("/"))
                .build())
            .execute()

        val request = mockServer.takeRequest()
        request.getHeader(HttpHeader.USER_AGENT)!!.run {
            assertContains("Android")
        }
    }
}

class TwfyHttpClientTest: HttpClientTest() {
    override val httpClient = HttpClientModule().provideTwfyHttpClient()

    /**
     * Ensure that all requests to TheyWorkForYou API include the necessary paramaters:
     *  - API key
     *  - Data output format
     */
    @Test
    fun twfyHttpClient_hasCorrectParams() {
        httpClient.newCall(
            Request.Builder()
                .url(mockServer.url("/"))
                .build())
            .execute()

        val request = mockServer.takeRequest()
        request.path!!.run {
            assertMatches(Regex(".*[?&]${TwfyParam.API_KEY}=[\\w]+.*"))
            assertMatches(Regex(".*[?&]${TwfyParam.OUTPUT_FORMAT}=[\\w]+.*"))
        }
    }
}
