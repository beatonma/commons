package org.beatonma.commons.test.extensions.assertions

import androidx.test.filters.SmallTest
import org.junit.Test

@SmallTest
class ArraysTest {
    @Test
    fun genericArray_assertArrayEquals_isCorrect() {
        arrayOf("1", "t").assertArrayEquals(arrayOf("1", "t"))
        assertThrowsAssertionError {
            arrayOf("1", "t").assertArrayEquals(arrayOf("1", "s"))
        }
    }

    @Test
    fun intArray_assertArrayEquals_isCorrect() {
        intArrayOf(1, 2, 3).assertArrayEquals(intArrayOf(1, 2, 3))
        assertThrowsAssertionError {
            intArrayOf(1, 2, 3).assertArrayEquals(intArrayOf(2, 3, 4))
        }
    }

    @Test
    fun booleanArray_assertArrayEquals_isCorrect() {
        booleanArrayOf(true, true, false).assertArrayEquals(booleanArrayOf(true, true, false))
        assertThrowsAssertionError {
            booleanArrayOf(true, true, false).assertArrayEquals(booleanArrayOf(true, false, true))
        }
    }

    @Test
    fun charArray_assertArrayEquals_isCorrect() {
        charArrayOf('a', 'b', '3').assertArrayEquals(charArrayOf('a', 'b', '3'))
        assertThrowsAssertionError {
            charArrayOf('a', 'b', '3').assertArrayEquals(charArrayOf('a', 'b', '_'))
        }
    }

    @Test
    fun byteArray_assertArrayEquals_isCorrect() {
        byteArrayOf(0, 1, 64, 127).assertArrayEquals(byteArrayOf(0, 1, 64, 127))
        assertThrowsAssertionError {
            byteArrayOf(0, 1, 64, 127).assertArrayEquals(byteArrayOf(0, -1, 63, 127))
        }
    }

    @Test
    fun longArray_assertArrayEquals_isCorrect() {
        longArrayOf(10000L, 200000000L, 300000000L).assertArrayEquals(longArrayOf(10000L, 200000000L, 300000000L))
        assertThrowsAssertionError {
            longArrayOf(10000L, 200000000L, 300000000L).assertArrayEquals(longArrayOf(10000L, 2000L, 3000L))
        }
    }

    @Test
    fun shortArray_assertArrayEquals_isCorrect() {
        shortArrayOf(0, 1, 32767).assertArrayEquals(shortArrayOf(0, 1, 32767))
        assertThrowsAssertionError {
            shortArrayOf(0, 1, 32767).assertArrayEquals(shortArrayOf(0, 1, 32500))
        }
    }
}
