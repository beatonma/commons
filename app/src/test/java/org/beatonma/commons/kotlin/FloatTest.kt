package org.beatonma.commons.kotlin

import org.beatonma.commons.kotlin.extensions.map
import org.beatonma.commons.kotlin.extensions.mapTo
import org.beatonma.commons.kotlin.extensions.normalizeIn
import org.beatonma.commons.test.extensions.assertions.assertFuzzyEquals
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class FloatTest {
    @Test
    fun float_mapTo_in_bounds_isCorrect() {
        0F.mapTo(0, 255) shouldbe 0
        1F.mapTo(0, 255) shouldbe 255
        0F.mapTo(21, 34) shouldbe 21
        1F.mapTo(21, 34) shouldbe 34

        .5F.mapTo(0, 10) shouldbe 5
        .64F.mapTo(0, 100) shouldbe 64
        .5F.mapTo(0, 255) shouldbe 127
    }

    @Test
    fun float_mapTo_out_of_bounds_should_constrain_to_limits() {
        (-1F).mapTo(0, 255) shouldbe 0
        2F.mapTo(0, 255) shouldbe 255
    }

    @Test
    fun float_map_isCorrect() {
        5F.map(0F, 10F, 0F, 1F) shouldbe .5F
        .5F.map(0F, 1F, 0F, 10F) shouldbe 5F
    }

    @Test
    fun float_normalizeIn_isCorrect() {
        .7F.normalizeIn(.6F, .8F).assertFuzzyEquals(.5F)
        50F.normalizeIn(40F, 90F).assertFuzzyEquals(.2F)

        1F.normalizeIn(2F, 4F).assertFuzzyEquals(0F)
        10F.normalizeIn(2F, 4F).assertFuzzyEquals(1F)
    }
}
