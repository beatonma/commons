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
import org.beatonma.commons.core.extensions.lerpTo

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
        val intrinsicWidth = measurable.minIntrinsicWidth(0)
        val intrinsicHeight = measurable.minIntrinsicHeight(0)

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
        val interpolatedConstraints = wrappedConstraints.lerpTo(fillConstraints,
            constrainedHorizontalProgress,
            constrainedVerticalProgress)

        val placeable = measurable.measure(interpolatedConstraints)

        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

private fun Constraints.lerpTo(
    other: Constraints,
    horizontalProgress: Float,
    verticalProgress: Float,
): Constraints {
    val minW = minWidth.lerpTo(other.minWidth, horizontalProgress)
    val maxW = maxWidth.lerpTo(other.maxWidth, horizontalProgress).coerceAtLeast(minW)
    val minH = minHeight.lerpTo(other.minHeight, verticalProgress)
    val maxH = maxHeight.lerpTo(other.maxHeight, verticalProgress).coerceAtLeast(minH)

    return Constraints(
        minWidth = minW,
        maxWidth = maxW,
        minHeight = minH,
        maxHeight = maxH,
    )
}
