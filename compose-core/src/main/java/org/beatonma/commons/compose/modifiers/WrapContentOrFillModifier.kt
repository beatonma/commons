package org.beatonma.commons.compose.modifiers

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import org.beatonma.commons.core.extensions.lerpBetween

/**
 * Helper for animating width between zero and the content width, based on progress.
 */
fun Modifier.wrapContentOrFillHeight(
    progress: Float,
    align: Alignment = Alignment.Center,
): Modifier {
    return this.then(
        WrapContentOrFillModifier(
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
        WrapContentOrFillModifier(
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
        WrapContentOrFillModifier(
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
private class WrapContentOrFillModifier(
    progress: Float = 0F,
    private val horizontalProgress: Float = progress,
    private val verticalProgress: Float = progress,
    private val affectHeight: Boolean,
    private val affectWidth: Boolean,
    inspectorInfo: InspectorInfo.() -> Unit,
) : LayoutModifier, InspectorValueInfo(inspectorInfo) {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val (intrinsicWidth, intrinsicHeight) = safeIntrinsicSizeOf(measurable)

        val wrappedWidth = if (affectWidth) intrinsicWidth else constraints.minWidth
        val wrappedHeight = if (affectHeight) intrinsicHeight else constraints.minHeight

        // Wrap content
        val wrappedConstraints = Constraints(
            minWidth = wrappedWidth,
            minHeight = wrappedHeight,
            maxWidth = constraints.maxWidth.coerceAtLeast(wrappedWidth),
            maxHeight = constraints.maxHeight.coerceAtLeast(wrappedHeight)
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
        val interpolatedConstraints = wrappedConstraints.lerpTo(
            fillConstraints,
            constrainedHorizontalProgress,
            constrainedVerticalProgress
        )

        val placeable = measurable.measure(interpolatedConstraints)

        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

/**
 * SubcomposeLayout does not support intrinsic measurements, causing errors whenever LazyColumn(etc)
 * is used within a block marked with this modifier. Return zero size when this happens.
 */
private fun safeIntrinsicSizeOf(measurable: Measurable): IntSize = try {
        IntSize(measurable.minIntrinsicWidth(0), measurable.minIntrinsicHeight(0))
    }
    catch (e: Exception) {
        IntSize.Zero
    }

private fun Constraints.lerpTo(
    other: Constraints,
    horizontalProgress: Float,
    verticalProgress: Float,
): Constraints {
    val minW = horizontalProgress.lerpBetween(minWidth, other.minWidth)
    val maxW = horizontalProgress.lerpBetween(maxWidth, other.maxWidth).coerceAtLeast(minW)
    val minH = verticalProgress.lerpBetween(minHeight, other.minHeight)
    val maxH = verticalProgress.lerpBetween(maxHeight, other.maxHeight).coerceAtLeast(minH)

    return Constraints(
        minWidth = minW,
        maxWidth = maxW,
        minHeight = minH,
        maxHeight = maxH,
    )
}
