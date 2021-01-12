package org.beatonma.commons.compose.components

import org.beatonma.commons.test.extensions.assertions.assertFuzzyEquals
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test


class CollapsibleHeaderStateTest {
    @Test
    fun HeaderState_isCorrect() {
        val state = CollapsibleHeaderState().apply {
            updateMaxValue(100F)
        }

        // scrollBy returns unconsumed part of given delta
        state.run {
            value shouldbe 0F
            expandProgress shouldbe 1F

            scrollBy(10F) shouldbe 0F
            value shouldbe 10F
            expandProgress.assertFuzzyEquals(0.9F)

            scrollBy(50F) shouldbe 0F
            value shouldbe 60F
            expandProgress.assertFuzzyEquals(0.4F)

            scrollBy(39F) shouldbe 0F
            scrollBy(1F) shouldbe 0F
            value shouldbe 100F

            scrollBy(1F) shouldbe 1F
            scrollBy(40F) shouldbe 40F
            value shouldbe 100F

            scrollBy(-60F) shouldbe 0F
            value shouldbe 40F

            scrollBy(-30F) shouldbe 0F
            value shouldbe 10F

            scrollBy(-10F) shouldbe 0F
            value shouldbe 0F

            scrollBy(-1F) shouldbe -1F
            value shouldbe 0F

            scrollBy(-10F) shouldbe -10F
            value shouldbe 0F
            expandProgress shouldbe 1F

            scrollBy(130F) shouldbe 30F
            value shouldbe 100F
            expandProgress shouldbe 0F
        }
    }
}
