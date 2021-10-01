package org.beatonma.commons.compose.util.math

import kotlin.math.absoluteValue
import kotlin.math.max

private const val size = 2

/**
 * A simple fixed-size 2x2 matrix, currently used to predict minimum scrolling size for
 * [org.beatonma.commons.compose.components.newcollapsibleheader.NewCollapsibleHeaderLayout].
 *
 * The insight of how to use a matrix to solve a system of linear equations this came from this
 * excellent article by Dion Saputra:
 *   https://dionsapoetra.medium.com/solving-linear-equation-system-a9f13e0a96c5
 */
internal class Matrix2x2(val elements: Array<Float>) {
    init {
        check(elements.size == 4) { "Matrix2x2 must have 4 values (got ${elements.size})" }
    }

    constructor(init: (Int) -> Float = { 0f }) : this(Array(2 * 2, init))
    constructor(a: Float, b: Float, c: Float, d: Float) : this(arrayOf(a, b, c, d))

    private operator fun get(index: Int) = elements[index]
    private operator fun set(index: Int, value: Float) {
        elements[index] = value
    }

    private operator fun component1() = elements[0]
    private operator fun component2() = elements[1]
    private operator fun component3() = elements[2]
    private operator fun component4() = elements[3]

    operator fun get(row: Int, column: Int) = elements[row * size + column]
    operator fun set(row: Int, column: Int, value: Float) {
        elements[row * size + column] = value
    }

    fun set(a: Float, b: Float, c: Float, d: Float) {
        elements[0] = a
        elements[1] = b
        elements[2] = c
        elements[3] = d
    }

    operator fun times(multiplier: Float): Matrix2x2 = map { it * multiplier }
    operator fun timesAssign(multiplier: Float) {
        for (i in elements.indices) {
            elements[i] = elements[i] * multiplier
        }
    }

    operator fun times(vector: Array<Float>): Array<Float> {
        check(vector.size == size) { "Vector must have $size values! (got ${vector.size})" }
        return arrayOf(
            this[0, 0] * vector[0] + this[0, 1] * vector[1],
            this[1, 0] * vector[0] + this[1, 1] * vector[1]
        )
    }

    operator fun timesAssign(vector: Array<Float>) {
        val (a, b) = vector
        vector[0] = this[0, 0] * a + this[0, 1] * b
        vector[1] = this[1, 0] * a + this[1, 1] * b
    }

    private fun map(transform: (Float) -> Float) = Matrix2x2 { transform(this[it]) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix2x2

        for (i in elements.indices) {
            val thisValue = this[i]
            val otherValue = other[i]
            if ((thisValue - otherValue).absoluteValue >= max(
                    Math.ulp(thisValue),
                    Math.ulp(otherValue)
                ) * 2f
            ) {
                return false
            }
        }

        return true
    }

    override fun toString(): String {
        val (a, b, c, d) = this
        return "[[$a, $b], [$c, $d]]"
    }

    override fun hashCode(): Int = elements.contentHashCode()

    fun determinant(): Float {
        val (a, b, c, d) = this
        return (a * d) - (b * c)
    }

    fun inverse(): Matrix2x2? {
        val determinant = determinant()
        if (determinant == 0f) return null

        val inverseDeterminant = 1f / determinant
        val (a, b, c, d) = this
        return Matrix2x2(d, -b, -c, a) * inverseDeterminant
    }

    fun invert() {
        val determinant = determinant()

        if (determinant == 0f) throw ArithmeticException("Cannot invert a matrix with determinant == 0!")

        val (a, b, c, d) = this
        this[0] = d
        this[1] = -b
        this[2] = -c
        this[3] = a
        this *= 1f / determinant
    }
}

/**
 * https://en.wikipedia.org/wiki/System_of_linear_equations#Matrix_equation
 *
 * Ax = b
 * -> x = inverse(A) * b
 *
 * Calculate the vector x using the coefficients matrix A and known values vector b.
 */
internal fun solveLinearEquations(
    coefficients: Matrix2x2,
    knownValues: Array<Float>
): Array<Float>? =
    coefficients.inverse()?.times(knownValues)
