package org.beatonma.commons.test.extensions.assertions

import android.graphics.Rect
import android.graphics.RectF
import org.junit.Assert as JUnit

/**
 * Ensure the sides of the receiver match the given values
 */
fun Rect.assertEquals(left: Int, top: Int, right: Int, bottom: Int) {
    JUnit.assertEquals(left, this.left)
    JUnit.assertEquals(top, this.top)
    JUnit.assertEquals(right, this.right)
    JUnit.assertEquals(bottom, this.bottom)
}

/**
 * Assert that at least one given value is different than the receiver values
 */
fun Rect.assertNotEquals(left: Int, top: Int, right: Int, bottom: Int) {
    JUnit.assertFalse(
            left == this.left
                    && top == this.top
                    && right == this.right
                    && bottom == this.bottom)
}


/**
 * Ensure the sides of the receiver match the given values
 */
fun RectF.assertEquals(left: Float, top: Float, right: Float, bottom: Float) {
    this.left.assertFuzzyEquals(left)
    this.top.assertFuzzyEquals(top)
    this.right.assertFuzzyEquals(right)
    this.bottom.assertFuzzyEquals(bottom)
}
