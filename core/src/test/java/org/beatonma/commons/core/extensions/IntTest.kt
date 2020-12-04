package org.beatonma.commons.core.extensions

import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class IntTest {
    @Test
    fun int_lerp_isCorrect() {
        // Upwards
        0.lerpTo(2, 0.5F) shouldbe 1
        9.lerpTo(15, 0.5F) shouldbe 12

        // With negative numbers
        (-1).lerpTo(4, 0.8F) shouldbe 3
        (-10).lerpTo(-5, 0.2F) shouldbe -9

        // Downwards
        3.lerpTo(-2, 0.8F) shouldbe -1
        (-10).lerpTo(-5, 0.2F) shouldbe -9
        (-5).lerpTo(-10, 0.2F) shouldbe -6
    }
}
