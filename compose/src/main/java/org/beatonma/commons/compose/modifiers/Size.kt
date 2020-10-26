package org.beatonma.commons.compose.modifiers

import androidx.compose.ui.Alignment
import androidx.compose.ui.LayoutModifier
import androidx.compose.ui.Measurable
import androidx.compose.ui.MeasureScope
import androidx.compose.ui.Modifier
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
            progress, align, affectHeight = true, affectWidth = false,
        ) { size, layoutDirection ->
            align.align(size, layoutDirection)
        }
    )
}

/**
 * Helper for animating width between zero and the content width, based on progress.
 */
fun Modifier.wrapContentWidth(progress: Float, align: Alignment = Alignment.Center): Modifier {
    return this.then(
        WrapContentModifier(
            progress, align, affectHeight = false, affectWidth = true,
        ) { size, layoutDirection ->
            align.align(size, layoutDirection)
        }
    )
}

/**
 * Helper for animating size between zero and the content size, based on progress.
 */
fun Modifier.wrapContentSize(progress: Float, align: Alignment = Alignment.Center): Modifier =
    this.then(
        WrapContentModifier(
            progress, align, affectHeight = true, affectWidth = true,
        ) { size, layoutDirection ->
            align.align(size, layoutDirection)
        }
    )

private data class WrapContentModifier(
    private val progress: Float,
    private val alignment: Any,
    private val affectHeight: Boolean,
    private val affectWidth: Boolean,
    private val unbounded: Boolean = true,
    private val alignmentCallback: (IntSize, LayoutDirection) -> IntOffset,
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureScope.MeasureResult {
        val constrainedProgress = progress.coerceAtLeast(0F)

        val wrappedConstraints = Constraints(
            minWidth = if (affectWidth) 0 else constraints.minWidth,
            minHeight = if (affectHeight) 0 else constraints.minHeight,
            maxWidth = if (affectWidth && unbounded) {
                Constraints.Infinity
            }
            else {
                constraints.maxWidth
            },
            maxHeight = if (affectHeight && unbounded) {
                Constraints.Infinity
            }
            else {
                constraints.maxHeight
            }
        )

        val placeable = measurable.measure(wrappedConstraints)

        val wrapperWidth = if (affectWidth) {
            (placeable.width.toFloat() * constrainedProgress).roundToInt()
                .coerceIn(constraints.minWidth, constraints.maxWidth)
        }
        else {
            placeable.width.coerceIn(constraints.minWidth, constraints.maxWidth)
        }

        val wrapperHeight = if (affectHeight) {
            (placeable.height.toFloat() * constrainedProgress).roundToInt()
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
                IntSize(wrapperWidth - placeable.width, wrapperHeight - placeable.height),
                layoutDirection
            )
            placeable.place(position)
        }
    }
}
