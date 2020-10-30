package org.beatonma.commons.compose.modifiers

import androidx.compose.ui.Alignment
import androidx.compose.ui.LayoutModifier
import androidx.compose.ui.Measurable
import androidx.compose.ui.MeasureScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import org.beatonma.commons.core.extensions.lerp
import kotlin.math.roundToInt

/**
 * Helper for animating height between zero and the content height, based on progress.
 */
fun Modifier.wrapContentHeight(progress: Float, align: Alignment = Alignment.Center): Modifier {
    return this.then(
        WrapContentModifier(
            progress, alignment = align, affectHeight = true, affectWidth = false,
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
            progress, alignment = align, affectHeight = false, affectWidth = true,
        ) { size, layoutDirection ->
            align.align(size, layoutDirection)
        }
    )
}

/**
 * Helper for animating size between zero and the content size, based on progress.
 */
fun Modifier.wrapContentSize(
    horizontalProgress: Float,
    verticalProgress: Float,
    align: Alignment = Alignment.Center,
): Modifier =
    this.then(
        WrapContentModifier(
            horizontalProgress = horizontalProgress, verticalProgress = verticalProgress,
            alignment = align, affectHeight = true, affectWidth = true,
        ) { size, layoutDirection ->
            align.align(size, layoutDirection)
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
    private val unbounded: Boolean = true,
    private val alignmentCallback: (IntSize, LayoutDirection) -> IntOffset,
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureScope.MeasureResult {
        val constrainedHorizontalProgress = horizontalProgress.coerceAtLeast(0F)
        val constrainedVerticalProgress = verticalProgress.coerceAtLeast(0F)

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
                IntSize(wrapperWidth - placeable.width, wrapperHeight - placeable.height),
                layoutDirection
            )
            placeable.place(position)
        }
    }
}

/**
 * Helper for animating width between zero and the content width, based on progress.
 */
fun Modifier.wrapContentOrFillHeight(
    progress: Float,
    align: Alignment = Alignment.Center,
): Modifier {
    return this.then(
        WrapOrFillModifier(
            progress, affectHeight = true, affectWidth = false,
            inspectorInfo = debugInspectorInfo {
                name = "wrapContentOrFillWidth"
                properties["align"] = align
                properties["progress"] = progress
            }
        )
    )
}

/**
 * Helper for animating width between zero and the content width, based on progress.
 */
fun Modifier.wrapContentOrFillWidth(
    progress: Float,
    align: Alignment = Alignment.Center,
): Modifier {
    return this.then(
        WrapOrFillModifier(
            progress, affectHeight = false, affectWidth = true,
            inspectorInfo = debugInspectorInfo {
                name = "wrapContentOrFillWidth"
                properties["align"] = align
                properties["progress"] = progress
            }
        )
    )
}

/**
 * Helper for animating width between zero and the content width, based on progress.
 */
fun Modifier.wrapContentOrFillSize(
    horizontalProgress: Float,
    verticalProgress: Float,
    align: Alignment = Alignment.Center,
): Modifier {
    return this.then(
        WrapOrFillModifier(
            horizontalProgress = horizontalProgress,
            verticalProgress = verticalProgress,
            affectHeight = true,
            affectWidth = true,
            inspectorInfo = debugInspectorInfo {
                name = "wrapContentOrFillWidth"
                properties["align"] = align
                properties["horizontalProgress"] = horizontalProgress
                properties["verticalProgress"] = verticalProgress
            }
        )
    )
}

/**
 * Interpolate between wrapped and fill states.
 */
private class WrapOrFillModifier(
    private val progress: Float = 0F,
    private val horizontalProgress: Float = progress,
    private val verticalProgress: Float = progress,
    private val affectHeight: Boolean,
    private val affectWidth: Boolean,
    inspectorInfo: InspectorInfo.() -> Unit,
) : LayoutModifier, InspectorValueInfo(inspectorInfo) {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureScope.MeasureResult {

        // Wrap content
        val wrappedConstraints = Constraints(
            minWidth = if (affectWidth) 0 else constraints.minWidth,
            minHeight = if (affectHeight) 0 else constraints.minHeight,
            maxWidth = constraints.maxWidth,
            maxHeight = constraints.maxHeight
        )

        // Fill space
        val fillConstraints = Constraints(
            minWidth = if (constraints.hasBoundedWidth && affectWidth) constraints.maxWidth else constraints.minWidth,
            maxWidth = constraints.maxWidth,
            minHeight = if (constraints.hasBoundedHeight && affectHeight) constraints.maxHeight else constraints.minHeight,
            maxHeight = constraints.maxHeight
        )

        val constrainedHorizontalProgress = horizontalProgress.coerceAtLeast(0F)
        val constrainedVerticalProgress = verticalProgress.coerceAtLeast(0F)
        val interpolatedConstraints = wrappedConstraints.lerp(fillConstraints,
            constrainedHorizontalProgress,
            constrainedVerticalProgress)

        val placeable = measurable.measure(interpolatedConstraints)

        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

private fun Constraints.lerp(
    other: Constraints,
    horizontalProgress: Float,
    verticalProgress: Float,
): Constraints =
    Constraints(
        minWidth = minWidth.lerp(other.minWidth, horizontalProgress),
        maxWidth = maxWidth.lerp(other.maxWidth, horizontalProgress),
        minHeight = minHeight.lerp(other.minHeight, verticalProgress),
        maxHeight = maxHeight.lerp(other.maxHeight, verticalProgress),
    )
