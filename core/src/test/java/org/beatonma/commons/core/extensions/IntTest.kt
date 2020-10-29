package org.beatonma.commons.core.extensions

import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class IntTest {
    @Test
    fun int_lerp_isCorrect() {
        // Upwards
        0.lerp(2, 0.5F) shouldbe 1
        9.lerp(15, 0.5F) shouldbe 12

        // With negative numbers
        (-1).lerp(4, 0.8F) shouldbe 3
        (-10).lerp(-5, 0.2F) shouldbe -9

        // Downwards
        3.lerp(-2, 0.8F) shouldbe -1
        (-10).lerp(-5, 0.2F) shouldbe -9
        (-5).lerp(-10, 0.2F) shouldbe -6
    }
}
