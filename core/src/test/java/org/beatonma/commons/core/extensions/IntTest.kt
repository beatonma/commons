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

    @Test
    fun int_roundUp_isCorrect() {
        0.roundUp(10) shouldbe 0
        for (n in 1..9) {
            n.roundUp(10) shouldbe 10
        }
        10.roundUp(10) shouldbe 10

        for (n in 211..219) {
            n.roundUp(10) shouldbe 220
        }

        4.roundUp(3) shouldbe 6
        5.roundUp(3) shouldbe 6
        6.roundUp(3) shouldbe 6
        7.roundUp(3) shouldbe 9

        (-1).roundUp(10) shouldbe 0
        (-11).roundUp(10) shouldbe -10
    }

    @Test
    fun int_roundDown_isCorrect() {
        for (n in 0..9) {
            n.roundDown(10) shouldbe 0
        }

        for (n in -1 downTo -9) {
            n.roundDown(10) shouldbe -10
        }

        for (n in 10 downTo 19) {
            n.roundDown(10) shouldbe 10
        }
    }
}
