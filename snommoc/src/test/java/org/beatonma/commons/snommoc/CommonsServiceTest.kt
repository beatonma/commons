package org.beatonma.commons.snommoc

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.beatonma.commons.network.core.Http
import org.beatonma.commons.snommoc.dagger.CommonsServiceModule
import org.beatonma.commons.test.extensions.assertions.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test


private fun String.assertContains(substring: String, ignoreCase: Boolean = false) {
    contains(substring, ignoreCase).assertTrue("'$substring' not found in $this")
}

class HttpClientTest {
    private val mockServer = MockWebServer()
    private val httpClient: OkHttpClient get() = CommonsServiceModule().provideCommonsHttpClient()

    @Before
    fun setUp() {
        mockServer.apply {
            start()
            enqueue(MockResponse())
        }
    }

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
        val userAgent = request.getHeader(Http.Header.USER_AGENT)!!
        userAgent.run{
            arrayOf(
                "Android",
            ).forEach(::assertContains)
        }
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}
