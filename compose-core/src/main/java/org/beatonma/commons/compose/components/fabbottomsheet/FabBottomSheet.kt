package org.beatonma.commons.compose.components.fabbottomsheet

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.withEasing
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.modifiers.consumePointerInput
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.shape.BottomSheetShape
import org.beatonma.commons.compose.systemui.navigationBarsPadding
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.themed.elevation
import org.beatonma.commons.themed.padding


/**
 * Displays a FloatingActionButton which expands into a bottom sheet dialog when tapped.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FabBottomSheet(
    fabClickLabel: String?,
    modifier: Modifier = Modifier,
    state: MutableState<FabBottomSheetState> = rememberFabBottomSheetState(),
    fabColor: Color = colors.primary,
    fabContentColor: Color = colors.onPrimary,
    sheetColor: Color = colors.surface,
    sheetContentColor: Color = colors.onSurface,
    onDismiss: () -> Unit = {},
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit,
) {
    FabBottomSheet(
        fabClickLabel = fabClickLabel,
        state = state.value,
        onStateChange = { newState -> state.value = newState },
        fabColor = fabColor,
        fabContentColor = fabContentColor,
        sheetColor = sheetColor,
        sheetContentColor = sheetContentColor,
        fabContent = fabContent,
        bottomSheetContent = bottomSheetContent,
        modifier = modifier,
        onDismiss = onDismiss,
    )
}

/**
 * Displays a FloatingActionButton which expands into a bottom sheet dialog when tapped.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FabBottomSheet(
    fabClickLabel: String?,
    modifier: Modifier = Modifier,
    state: FabBottomSheetState,
    onStateChange: (FabBottomSheetState) -> Unit,
    fabColor: Color = colors.primary,
    fabContentColor: Color = colors.onPrimary,
    sheetColor: Color = colors.surface,
    sheetContentColor: Color = colors.onSurface,
    onDismiss: () -> Unit = {},
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit,
) {
    val progress by state.animateExpansionAsState()
    val resolvedContentColor = getContentColor(fabContentColor, sheetContentColor, progress)
    val resolvedSurfaceColor = getSurfaceColor(fabColor, sheetColor, progress)

    FabBottomSheetLayout(
        fabClickLabel = fabClickLabel,
        state = state,
        onStateChange = onStateChange,
        progress = progress,
        fabContent = fabContent,
        bottomSheetContent = bottomSheetContent,
        modifier = modifier,
        surfaceColor = resolvedSurfaceColor,
        contentColor = resolvedContentColor,
        onDismiss = onDismiss,
    )
}

@Composable
private fun FabBottomSheetLayout(
    fabClickLabel: String?,
    state: FabBottomSheetState,
    onStateChange: (FabBottomSheetState) -> Unit,
    progress: Float,
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit,
    modifier: Modifier,
    surfaceColor: Color,
    contentColor: Color,
    onDismiss: () -> Unit,
) {
    ModalScrim(
        alignment = Alignment.BottomEnd,
        visible = state == FabBottomSheetState.BottomSheet,
        onClickAction = {
            onStateChange(FabBottomSheetState.Fab)
            onDismiss()
        },
        onClickLabel = stringResource(R.string.content_description_close_overlay)
    ) {
        val surfaceShape = getSurfaceShape(progress)

        if (progress == 0F) {
            Fab(
                surfaceShape,
                surfaceColor = surfaceColor,
                contentColor = contentColor,
                onClick = { onStateChange(FabBottomSheetState.BottomSheet) },
                onClickLabel = fabClickLabel,
                modifier = modifier,
                content = { fabContent(0F) }
            )
        } else {
            BottomSheet(
                progress = progress,
                surfaceShape = surfaceShape,
                surfaceColor = surfaceColor,
                contentColor = contentColor,
                modifier = modifier,
                fabContent = fabContent,
                bottomSheetContent = bottomSheetContent
            )
        }
    }
}

@Composable
private fun Fab(
    surfaceShape: Shape,
    surfaceColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    onClickLabel: String?,
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier
            .padding(padding.Fab)
            .navigationBarsPadding() // The bottom sheet should extend behind navigation bar as it expands.
            .testTag(TestTag.Fab)
            .clickable(
                onClickLabel = onClickLabel,
                onClick = onClick
            ),
        shape = surfaceShape,
        color = surfaceColor,
        contentColor = contentColor,
        elevation = elevation.ModalSurface,
    ) {
        Box(content = content)
    }
}

@Composable
private fun BottomSheet(
    progress: Float,
    surfaceShape: Shape,
    surfaceColor: Color,
    contentColor: Color,
    modifier: Modifier,
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit
) {
    Surface(
        modifier
            .padding(progress.lerpBetween(padding.Fab, padding.Zero))
            .navigationBarsPadding(scale = progress.reversed()) // The bottom sheet should extend behind navigation bar as it expands.
            .consumePointerInput()
            .testTag(TestTag.FabBottomSheet),
        shape = surfaceShape,
        color = surfaceColor,
        contentColor = contentColor,
        elevation = elevation.ModalSurface,
    ) {
        Box {
            if (progress != 1F) {
                fabContent(progress)
            }
            bottomSheetContent(progress)
        }
    }
}

@Composable
private fun getSurfaceColor(
    fabColor: Color,
    sheetColor: Color,
    progress: Float,
): Color =
    progress
        .progressIn(0F, 0.4F)
        .lerpBetween(fabColor, sheetColor)

@Composable
private fun getContentColor(
    fabContentColor: Color,
    sheetContentColor: Color,
    progress: Float,
): Color =
    progress
        .progressIn(0F, 0.4F)
        .lerpBetween(fabContentColor, sheetContentColor)

@Composable
private fun getSurfaceShape(progress: Float): CornerBasedShape {
    val fabShape = CircleShape
    val expandedShape = BottomSheetShape

    val eased = progress.withEasing(LinearOutSlowInEasing)

    return eased.lerpBetween(fabShape, expandedShape)
}
