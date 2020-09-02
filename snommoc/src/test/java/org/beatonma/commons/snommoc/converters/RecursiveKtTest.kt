package org.beatonma.commons.snommoc.converters

import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class RecursiveKtTest {

    @Test
    fun takeFirstSuccessful_isCorrect() {
        takeFirstSuccessful(
            { 0 },
            { 1 },
            { 2 }
        ) shouldbe 0

        takeFirstSuccessful(
            { throw NumberFormatException() },
            { 1 },
            { 2 }
        ) shouldbe 1

        takeFirstSuccessful(
            { 0 },
            { throw Exception() },
            { 2 }
        ) shouldbe 0

        takeFirstSuccessful(
            { throw IllegalStateException() },
            { throw ArrayIndexOutOfBoundsException() },
            { throw RuntimeException() },
            { 4 },
            { 5 },
        ) shouldbe 4
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
        ) shouldbe 0
        exceptionCallCount shouldbe 0

        takeFirstSuccessful(
            { throw NumberFormatException() },
            { throw ArrayIndexOutOfBoundsException() },
            { 2 },
            { 3 },
            onException = onException
        ) shouldbe 2
        exceptionCallCount shouldbe 2
    }
}
