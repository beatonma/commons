package org.beatonma.commons.core.extensions

import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class CollectionsTest {
    @Test
    fun allEqual_isCorrect() {
        allEqual(0, 0, 0, 0) shouldbe true
        allEqual(0, 0, 0, 1) shouldbe false
        allEqual(3, 2, 1) shouldbe false
        allEqual(null, 1, 2) shouldbe false
        allEqual(0, 0, null, 0) shouldbe false
    }

    @Test
    fun allEqualTo_isCorrect() {
        listOf(0, 0, 0, 0, 0).allEqualTo(0) shouldbe true
        listOf(0, 0, 0, 1, 0).allEqualTo(0) shouldbe false

        listOf(0, 0, 0, 0, null).allEqualTo(0) shouldbe false
        listOf(0, 0, 0, 0, null).allEqualTo(0, orNull = true) shouldbe true
        listOf(0, 0, 0, 0, null, 1).allEqualTo(0, orNull = true) shouldbe false
    }
}
