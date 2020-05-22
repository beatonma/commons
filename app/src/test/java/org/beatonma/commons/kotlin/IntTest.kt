package org.beatonma.commons.kotlin

import android.view.Gravity
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Test

class IntTest {
    @Test
    fun combineFlags_shouldReturnBinaryOrOfEveryGivenInt() {
        combineFlags(0b1, 0b10, 0b100) shouldbe 0b111
        combineFlags(0b1, 0b100) shouldbe 0b101
        combineFlags(0b100, 0b1000) shouldbe 0b1100
        combineFlags(0b111) shouldbe 0b111
    }

    @Test
    fun int_addFlag_isCorrect() {
        0b0.addFlag(0b1) shouldbe 0b1
        0b1.addFlag(0b10) shouldbe 0b11
    }

    @Test
    fun int_hasFlag_isCorrect() {
        0b1.hasFlag(0b10) shouldbe false
        0b1.hasFlag(0b11) shouldbe false

        0b11.hasFlag(0b10) shouldbe true
        0b11.hasFlag(0b1) shouldbe true

        val gravityFlag = Gravity.TOP or Gravity.RIGHT
        gravityFlag.hasFlag(Gravity.TOP) shouldbe true
        gravityFlag.hasFlag(Gravity.RIGHT) shouldbe true
        gravityFlag.hasFlag(Gravity.LEFT) shouldbe false
        gravityFlag.hasFlag(Gravity.BOTTOM) shouldbe false
    }

    @Test
    fun int_hasAllFlags_isCorrect() {
        0b11.hasAllFlags(0b1, 0b10) shouldbe true
        0b11.hasAllFlags(0b1) shouldbe true
        0b11.hasAllFlags(0b10) shouldbe true
        0b11.hasAllFlags(0b1, 0b100) shouldbe false

        0b111.hasAllFlags(0b1, 0b10, 0b100) shouldbe true
        0b111.hasAllFlags(0b1, 0b10, 0b100, 0b1000) shouldbe false
        0b111.hasAllFlags(0b1000) shouldbe false
    }

    @Test
    fun int_hasAnyFlag_isCorrect() {
        0b11.hasAnyFlag(0b1, 0b10) shouldbe true
        0b11.hasAnyFlag(0b1) shouldbe true
        0b11.hasAnyFlag(0b1, 0b100) shouldbe true
        0b11.hasAnyFlag(0b100) shouldbe false
    }

    @Test
    fun int_hasOnlyFlags_isCorrect() {
        0b11.hasOnlyFlags(0b1, 0b10) shouldbe true
        0b111.hasOnlyFlags(0b1, 0b10, 0b100) shouldbe true
        0b111.hasOnlyFlags(0b1, 0b100) shouldbe false
    }

    @Test
    fun int_removeFlag_isCorrect() {
        0b111.removeFlag(0b010) shouldbe 0b101
        0b111.removeFlag(0b001) shouldbe 0b110
        0b111.removeFlag(0b100) shouldbe 0b011
        0b111.removeFlag(0b011) shouldbe 0b100
        0b000.removeFlag(0b001) shouldbe 0b000
    }

    @Test
    fun int_removeFlags_isCorrect() {
        0b111.removeFlags(0b010, 0b001) shouldbe 0b100
    }

    @Test
    fun int_replaceFlag_isCorrect() {
        0b111.replaceFlag(0b010, 0b10000) shouldbe 0b10101
    }
}
