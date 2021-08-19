package org.beatonma.commons.compose.modifiers

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

/**
 * Helper for animating height between zero and the content height, based on progress.
 */
fun Modifier.wrapContentHeight(progress: Float, align: Alignment = Alignment.Center): Modifier {
    return this.then(
        WrapContentModifier(
            progress, alignment = align, affectHeight = true, affectWidth = false,
        ) { size, space, layoutDirection ->
            align.align(size, space, layoutDirection)
        }
    )
}

/**
 * Helper for animating width between zero and the content width, based on progress.
 */
fun Modifier.wrapContentWidth(progress: Float, align: Alignment = Alignment.Center): Modifier {
    return this.then(
        WrapContentModifier(
            progress, alignment = align, affectHeight = false, affectWidth = true,
        ) { size, space, layoutDirection ->
            align.align(size, space, layoutDirection)
        }
    )
}

/**
 * Helper for animating size between zero and the content size, based on progress.
 */
fun Modifier.wrapContentSize(
    horizontalProgress: Float,
    verticalProgress: Float = horizontalProgress,
    align: Alignment = Alignment.Center,
): Modifier =
    this.then(
        WrapContentModifier(
            horizontalProgress = horizontalProgress, verticalProgress = verticalProgress,
            alignment = align, affectHeight = true, affectWidth = true,
        ) { size, space, layoutDirection ->
            align.align(size, space, layoutDirection)
        }
    )

/**
 * Interpolate between zero and wrapped content size.
 */
private data class WrapContentModifier(
    private val progress: Float = 0F,
    private val horizontalProgress: Float = progress,
    private val verticalProgress: Float = progress,
    private val alignment: Any,
    private val affectHeight: Boolean,
    private val affectWidth: Boolean,
    private val unbounded: Boolean = false,
    private val alignmentCallback: (IntSize, IntSize, LayoutDirection) -> IntOffset,
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val constrainedHorizontalProgress = horizontalProgress.coerceAtLeast(0F)
        val constrainedVerticalProgress = verticalProgress.coerceAtLeast(0F)

        val wrappedConstraints = Constraints(
            minWidth = if (affectWidth) 0 else constraints.minWidth,
            minHeight = if (affectHeight) 0 else constraints.minHeight,
            maxWidth = if (affectWidth && unbounded) {
                Constraints.Infinity
            } else {
                constraints.maxWidth
            },
            maxHeight = if (affectHeight && unbounded) {
                Constraints.Infinity
            } else {
                constraints.maxHeight
            }
        )

        val placeable = measurable.measure(wrappedConstraints)

        val wrapperWidth = if (affectWidth) {
            (placeable.width.toFloat() * constrainedHorizontalProgress).roundToInt()
                .coerceIn(constraints.minWidth, constraints.maxWidth)
        }
        else {
            placeable.width.coerceIn(constraints.minWidth, constraints.maxWidth)
        }

        val wrapperHeight = if (affectHeight) {
            (placeable.height.toFloat() * constrainedVerticalProgress).roundToInt()
                .coerceIn(constraints.minHeight, constraints.maxHeight)
        }
        else {
            placeable.height.coerceIn(constraints.minHeight, constraints.maxHeight)
        }

        return layout(
            wrapperWidth,
            wrapperHeight
        ) {
            val position = alignmentCallback(
                IntSize(placeable.width, placeable.height),
                IntSize(wrapperWidth, wrapperHeight),
                layoutDirection
            )
            placeable.place(position)
        }
    }
}
