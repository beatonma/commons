package org.beatonma.commons.compose.util

import androidx.compose.ui.graphics.Color
import org.beatonma.commons.core.extensions.mapToByte
import org.beatonma.commons.core.extensions.normalize
import org.beatonma.commons.test.extensions.assertions.assertFuzzyEquals
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test
import kotlin.random.Random

fun anyColor() = Color(
    red = Random.nextInt(255),
    green = Random.nextInt(255),
    blue = Random.nextInt(255),
    alpha = Random.nextInt(255)
)

class ColorTest {

    @Test
    fun check_HslColor_isCorrect() {
        val hsl = HslColor(72F, .8F, .4F)

        hsl.run {
            hue shouldbe 72F
            saturation shouldbe 0.8F
            lightness shouldbe 0.4F
        }

        hsl.set(27F, .7F, .1F)
        hsl.run {
            hue shouldbe 27F
            saturation shouldbe 0.7F
            lightness shouldbe 0.1F
        }
    }

    @Test
    fun check_Color_toHslColor_isCorrect() {
        fun HslColor.expectPureHue(h: Float) {
            hue shouldbe h
            saturation shouldbe 1F
            lightness shouldbe 0.5F
        }

        Color.Red.toHslColor().expectPureHue(0F)
        Color.Yellow.toHslColor().expectPureHue(60F)
        Color.Green.toHslColor().expectPureHue(120F)
        Color.Cyan.toHslColor().expectPureHue(180F)
        Color.Blue.toHslColor().expectPureHue(240F)
        Color.Magenta.toHslColor().expectPureHue(300F)
    }

    @Test
    fun check_HslColor_toColor_isCorrect() {
        Color.Red.red shouldbe 1F
        Color.Yellow.red shouldbe 1F
        Color.Red.toHslColor()
            .toColor()
            .red shouldbe 1F
    }

    /**
     * Compare mid-calculation values to those in this example:
     *   https://www.niwa.nu/2013/05/math-behind-colorspace-conversions-rgb-hsl/
     *
     * The example rounds its calculated values at various points. Therefore, to keep in step with
     * its values we will replace our own calculated values with the values from the example, after
     * ensuring the calculated values are correct.
     */
    @Test
    fun check_HslColor_toColor_benchmark() {
        val color = Color(24, 98, 118)

        val r = color.red
        val g = color.green
        val b = color.blue

        run {
            val min = minOf(r, g, b)
            val max = maxOf(r, g, b)

            val lightness = (min + max) / 2F

            min.assertFuzzyEquals(0.09F)
            max.assertFuzzyEquals(0.46F)
            lightness.assertFuzzyEquals(0.275F)
        }

        run {
            /*
             * The example rounds its calculated values. Therefore, to keep in step with the example
             * values we will replace our own calculated values with the rounded values in the example.
             */
            val lightness = 0.28F
            val min = 0.09F
            val max = 0.46F
            val diff = max - min

            val saturation = when {
                min == max -> 0F
                lightness <= 0.5F -> diff / (max + min)
                else -> diff / (2F - max - min)
            }

            saturation.assertFuzzyEquals(0.672F)
        }

        run {
            val min = 0.09F
            val max = 0.46F
            val diff = max - min

            val hue = (when (max) {
                min -> 0F // diff == 0
                r -> {
                    (g - b) / diff
                }

                g -> {
                    2.0F + (b - r) / diff
                }

                else -> {
                    4.0F + (r - g) / diff
                }
            }.also {
                it.assertFuzzyEquals(3.21F)
            } * 60F).positiveMod(360F)

            hue.assertFuzzyEquals(192.9F, fuzz = 0.1F)
        }
    }

    /**
     * Compare mid-calculation values to those in this example:
     *   https://www.niwa.nu/2013/05/math-behind-colorspace-conversions-rgb-hsl/
     *
     * The example rounds its calculated values at various points. Therefore, to keep in step with
     * its values we will replace our own calculated values with the values from the example, after
     * ensuring the calculated values are correct.
     */
    @Test
    fun check_Color_toHslColor_benchmark() {
        val h = 193F
        val s = 0.67F
        val l = 0.28F

        val third = 0.333F
        val twoThirds = 0.666F

        run {
            val t1 = when {
                l < 0.5F -> l * (1F + s)
                else -> l + s - l * s
            }.positiveMod(1F)

            val t2 = ((2F * l) - t1).positiveMod(1F)

            t1.assertFuzzyEquals(0.4676F, 0.01F)
            t2.assertFuzzyEquals(0.0924F, 0.01F)
        }

        run {
            val normalisedHue = h.normalize(360F)
            normalisedHue.assertFuzzyEquals(0.536F)
        }

        run {
            val normalisedHue = 0.536F

            val r = (normalisedHue + third).positiveMod(1F)
            val g = normalisedHue
            val b = (normalisedHue - third).positiveMod(1F)

            r.assertFuzzyEquals(0.869F)
            g.assertFuzzyEquals(0.536F)
            b.assertFuzzyEquals(0.203F)
        }

        run {
            val t1 = 0.4676F
            val t2 = 0.0924F

            val r = 0.869F
            val g = 0.536F
            val b = 0.203F

            fun resolve(value: Float) = when {
                value < (1F / 6F) -> {
                    t2 + (t1 - t2) * 6F * value
                }

                value < (1F / 2F) -> {
                    t1
                }

                value < (2F / 3F) -> {
                    t2 + (t1 - t2) * (twoThirds - value) * 6F
                }

                else -> t2
            }.mapToByte()

            val red = resolve(r)
            val green = resolve(g)
            val blue = resolve(b)

            red shouldbe 24
            green shouldbe 98
            blue shouldbe 119
        }
    }

    @Test
    fun check_Color_HsvColor_conversion_equality() {
        Color(0xae, 0x47, 0xf2)
            .toHslColor()
            .toColor()
            .run {
                redByte shouldbe 0xae
                greenByte shouldbe 0x47
                blueByte shouldbe 0xf2
            }

        // Generate random Colors and convert them to HslColor and back again, ensuring the result
        // is the same as the source.
        for (i in 1..50) {
            val c = anyColor()
            c.toHslColor().toColor() shouldbe c
        }
    }
}
