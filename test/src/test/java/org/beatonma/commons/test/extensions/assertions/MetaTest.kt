package org.beatonma.commons.test.extensions.assertions

import androidx.test.filters.SmallTest
import org.junit.Test

@SmallTest
class MetaTest {
    @Test
    fun assertThrowsAssertionError_isCorrect() {
        assertThrowsAssertionError { null.assertNotNull() }
        assertThrowsAssertionError { "".assertNull() }
    }
}
