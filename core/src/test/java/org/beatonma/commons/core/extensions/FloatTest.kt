package org.beatonma.commons.core.extensions

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
        .5F.mapTo(0, 255) shouldbe 128
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

    @Test
    fun float_progressIn_isCorrect() {
        10F.progressIn(0F, 10F).assertFuzzyEquals(1F)
        0F.progressIn(0F, 10F).assertFuzzyEquals(0F)

        4F.progressIn(8F, 10F).assertFuzzyEquals(0F)
        9F.progressIn(8F, 10F).assertFuzzyEquals(0.5F)
        11F.progressIn(8F, 10F).assertFuzzyEquals(1F)
    }

    @Test
    fun float_triangle_isCorrect() {
        100F.triangle(0F) shouldbe 0F
        100F.triangle(.25F) shouldbe 50F
        100F.triangle(.5F) shouldbe 100F
        100F.triangle(.75F) shouldbe 50F
        100F.triangle(1F) shouldbe 0F

        100F.triangle(.5F, inflectAt = .75F).assertFuzzyEquals(200F / 3F)
        100F.triangle(.75F, inflectAt = .75F) shouldbe 100F
        100F.triangle(1F, inflectAt = .75F) shouldbe 0F
    }
}
