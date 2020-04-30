package org.beatonma.commons.kotlin

import org.beatonma.lib.testing.kotlin.extensions.assertions.assertEquals
import org.junit.Test

class RecursiveKtTest {

    @Test
    fun takeFirstSuccessful_isCorrect() {
        takeFirstSuccessful(
            { 0 },
            { 1 },
            { 2 }
        ).assertEquals(0)

        takeFirstSuccessful(
            { throw NumberFormatException() },
            { 1 },
            { 2 }
        ).assertEquals(1)

        takeFirstSuccessful(
            { 0 },
            { throw Exception() },
            { 2 }
        ).assertEquals(0)

        takeFirstSuccessful(
            { throw IllegalStateException() },
            { throw ArrayIndexOutOfBoundsException() },
            { throw RuntimeException() },
            { 4 },
            { 5 },
        ).assertEquals(4)
    }

    @Test
    fun takeFirstSuccessful_onException_isCorrect() {
        var exceptionCallCount = 0
        val onException: (Exception) -> Unit = { exceptionCallCount += 1 }

        takeFirstSuccessful(
            { 0 },
            { 1 },
            { 2 },
            onException = onException
        ).assertEquals(0)
        exceptionCallCount.assertEquals(0)

        takeFirstSuccessful(
            { throw NumberFormatException() },
            { throw ArrayIndexOutOfBoundsException() },
            { 2 },
            { 3 },
            onException = onException
        ).assertEquals(2)
        exceptionCallCount.assertEquals(2)
    }
}
