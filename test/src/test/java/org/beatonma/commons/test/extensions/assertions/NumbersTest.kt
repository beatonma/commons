package org.beatonma.commons.test.extensions.assertions

import androidx.test.filters.SmallTest
import org.junit.Test

@SmallTest
class NumbersTest {
    @Test
    fun assertFuzzyEquals_isCorrect() {
        3.assertFuzzyEquals(4, fuzz = 1)
        3.assertFuzzyEquals(2, fuzz = 1)

        assertThrowsAssertionError { 3.assertFuzzyEquals(5, fuzz = 1) }
        assertThrowsAssertionError { 3.assertFuzzyEquals(1, fuzz = 1) }

        3.assertFuzzyEquals(5, fuzz = 2)
        3.assertFuzzyEquals(1, fuzz = 2)
    }
}
