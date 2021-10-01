package org.beatonma.commons.compose.util.math

import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test


class Matrix2x2Test {

    @Test
    fun testScalarMultiplication() {
        val m = Matrix2x2(10f, 6f, 4f, 3f)
        (m * 2f) shouldbe Matrix2x2(20f, 12f, 8f, 6f)
        (m * -3f) shouldbe Matrix2x2(-30f, -18f, -12f, -9f)

        m *= 10f
        m.elements shouldbe arrayOf(100f, 60f, 40f, 30f)
    }

    @Test
    fun testDeterminant() {
        Matrix2x2(3f, 8f, 4f, 6f).determinant() shouldbe -14f
        Matrix2x2(3f, 1f, 7f, -4f).determinant() shouldbe -19f

    }

    @Test
    fun testInverse() {
        Matrix2x2(4f, 7f, 2f, 6f).inverse() shouldbe Matrix2x2(0.6f, -0.7f, -0.2f, 0.4f)

        val m = Matrix2x2(1f, 3f, 2f, 4f)
        m.invert()

        m shouldbe Matrix2x2(-2f, 1.5f, 1f, -.5f)
    }

    @Test
    fun testSolve() {
        val m = Matrix2x2(1f, 1f, 0.9f, 1f)
        val v = arrayOf(100f, 91f)
        val solved = solveLinearEquations(m, v)!!

        solved shouldbe arrayOf(90f, 10f)
    }
}
