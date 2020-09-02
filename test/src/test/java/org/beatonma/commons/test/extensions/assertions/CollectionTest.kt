package org.beatonma.commons.test.extensions.assertions

import org.junit.Test

class CollectionTest {
    @Test
    fun assertEach_isCorrect() {
        listOf(1, 2, 3).assertEach(
            { it shouldbe 1 },
            { it shouldbe 2 },
            { it shouldbe 3 },
        )
    }


    @Test
    fun assertEach_throwsWithBadAssertion() {
        assertThrowsAssertionError {
            listOf(1, 2, 3).assertEach(
                { it shouldbe 1 },
                { it shouldbe 1 },
                { it shouldbe 3 },
            )
        }
    }
}
