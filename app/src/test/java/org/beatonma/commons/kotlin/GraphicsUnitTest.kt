package org.beatonma.commons.kotlin

import android.graphics.Rect
import android.graphics.RectF
import org.beatonma.commons.kotlin.extensions.update
import org.beatonma.commons.kotlin.extensions.updateBy
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.test.extensions.mock
import org.beatonma.commons.test.extensions.whenever
import org.junit.Test
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyInt

class GraphicsUnitTest {
    private fun createMockRect(): Rect {
        val rect = mock<Rect>()
        whenever(rect.set(anyInt(), anyInt(), anyInt(), anyInt())).then {
            rect.left = it.getArgument(0)
            rect.top = it.getArgument(1)
            rect.right = it.getArgument(2)
            rect.bottom = it.getArgument(3)
            Unit
        }
        return rect
    }

    private fun createMockRectF(): RectF {
        val rect = mock<RectF>()
        whenever(rect.set(anyFloat(), anyFloat(), anyFloat(), anyFloat())).then {
            rect.left = it.getArgument(0)
            rect.top = it.getArgument(1)
            rect.right = it.getArgument(2)
            rect.bottom = it.getArgument(3)
            Unit
        }
        return rect
    }


    /* Rect */
    @Test
    fun testRect_update() {
        val rect = createMockRect()

        rect.left shouldbe  0
        rect.top shouldbe  0
        rect.right shouldbe  0
        rect.bottom shouldbe  0

        // Update all values
        rect.update(-10, -15, 20, 35)
        rect.left shouldbe  -10
        rect.top shouldbe  -15
        rect.right shouldbe  20
        rect.bottom shouldbe  35

        // Update only single value
        rect.update(top = 8)
        rect.left shouldbe  -10
        rect.top shouldbe  8
        rect.right shouldbe  20
        rect.bottom shouldbe  35

        // Update a different single value
        rect.update(right = 22)
        rect.left shouldbe  -10
        rect.top shouldbe  8
        rect.right shouldbe  22
        rect.bottom shouldbe  35
    }

    @Test
    fun testRect_updateBy() {
        val rect = createMockRect()

        rect.left shouldbe  0
        rect.top shouldbe  0
        rect.right shouldbe  0
        rect.bottom shouldbe  0

        // Update all values
        rect.updateBy(-10, -15, 20, 35)
        rect.left shouldbe  -10
        rect.top shouldbe  -15
        rect.right shouldbe  20
        rect.bottom shouldbe  35

        // Update only single value, ensuring other values do not change
        rect.updateBy(top = 8)
        rect.left shouldbe  -10
        rect.top shouldbe  -7
        rect.right shouldbe  20
        rect.bottom shouldbe  35

        // Update a different single value
        rect.updateBy(right = 22)
        rect.left shouldbe  -10
        rect.top shouldbe  -7
        rect.right shouldbe  42
        rect.bottom shouldbe  35

        // Update with negative value
        rect.updateBy(bottom = -12)
        rect.bottom shouldbe  23
    }


    /* RectF */
    @Test
    fun testRectF_update() {
        val rectF = createMockRectF()

        rectF.left shouldbe  0F
        rectF.top shouldbe  0F
        rectF.right shouldbe  0F
        rectF.bottom shouldbe  0F

        // Update all values
        rectF.update(-10F, -15F, 20F, 35F)
        rectF.left shouldbe  -10F
        rectF.top shouldbe  -15F
        rectF.right shouldbe  20F
        rectF.bottom shouldbe  35F

        // Update only single value
        rectF.update(top = 8F)
        rectF.left shouldbe  -10F
        rectF.top shouldbe  8F
        rectF.right shouldbe  20F
        rectF.bottom shouldbe  35F

        // Update a different single value
        rectF.update(right = 22F)
        rectF.left shouldbe  -10F
        rectF.top shouldbe  8F
        rectF.right shouldbe  22F
        rectF.bottom shouldbe  35F
    }

    @Test
    fun testRectF_updateBy() {
        val rectF = createMockRectF()

        rectF.left shouldbe  0F
        rectF.top shouldbe  0F
        rectF.right shouldbe  0F
        rectF.bottom shouldbe  0F

        // Update all values
        rectF.updateBy(-10F, -15F, 20F, 35F)
        rectF.left shouldbe  -10F
        rectF.top shouldbe  -15F
        rectF.right shouldbe  20F
        rectF.bottom shouldbe  35F

        // Update only single value, ensuring other values do not change
        rectF.updateBy(top = 8F)
        rectF.left shouldbe  -10F
        rectF.top shouldbe  -7F
        rectF.right shouldbe  20F
        rectF.bottom shouldbe  35F

        // Update a different single value
        rectF.updateBy(right = 22F)
        rectF.left shouldbe  -10F
        rectF.top shouldbe  -7F
        rectF.right shouldbe  42F
        rectF.bottom shouldbe  35F

        // Update with negative value
        rectF.updateBy(bottom = -12F)
        rectF.bottom shouldbe  23F
    }
}
