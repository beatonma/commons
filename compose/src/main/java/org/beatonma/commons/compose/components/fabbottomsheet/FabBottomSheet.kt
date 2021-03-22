package org.beatonma.commons.compose.components.fabbottomsheet

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.withEasing
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.modifiers.consumePointerInput
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.theme.compose.Elevation
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.CORNER_SMALL
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding


/**
 * Displays a FAB which expands into a bottom sheet dialog.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FabBottomSheet(
    fabClickLabel: String?,
    modifier: Modifier = Modifier,
    state: MutableState<FabBottomSheetState> = rememberFabBottomSheetState(),
    surfaceColor: Color? = null,
    contentColor: Color? = null,
    onDismiss: () -> Unit = {},
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit,
) {
    val progress by state.value.animateExpansionAsState()
    val resolvedSurfaceColor = surfaceColor ?: getSurfaceColor(progress)

    FabBottomSheetLayout(
        fabClickLabel = fabClickLabel,
        state = state.value,
        onStateChange = { newState -> state.value = newState },
        progress = progress,
        fabContent = fabContent,
        bottomSheetContent = bottomSheetContent,
        modifier = modifier,
        surfaceColor = resolvedSurfaceColor,
        contentColor = contentColor ?: contentColorFor(resolvedSurfaceColor),
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
        }
        else {
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
            .padding(Padding.Fab)
            .navigationBarsPadding() // The bottom sheet should extend behind navigation bar as it expands.
            .shadow(Elevation.ModalSurface, surfaceShape)
            .testTag("fab_bottomsheet_surface__fab")
            .clickable(
                onClickLabel = onClickLabel,
                onClick = onClick
            )
        ,
        shape = surfaceShape,
        color = surfaceColor,
        contentColor = contentColor,
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
            .padding(progress.lerpBetween(Padding.Fab, Padding.Zero))
            .navigationBarsPadding(scale = progress.reversed()) // The bottom sheet should extend behind navigation bar as it expands.
            .shadow(Elevation.ModalSurface, surfaceShape)
            .consumePointerInput()
            .testTag("fab_bottomsheet_surface")
        ,
        shape = surfaceShape,
        color = surfaceColor,
        contentColor = contentColor,
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
private fun getSurfaceColor(progress: Float) =
    progress.progressIn(0F, 0.4F).lerpBetween(colors.primary, colors.surface)

private fun getSurfaceShape(progress: Float): Shape {
    val eased = progress.withEasing(LinearOutSlowInEasing)
    val upperCornerSize = eased.lerpBetween(24, CORNER_SMALL).dp
    val lowerCornerSize = eased.lerpBetween(24, 0).dp
    return RoundedCornerShape(upperCornerSize, upperCornerSize, lowerCornerSize, lowerCornerSize)
}


@Composable @Preview
fun FabBottomSheetPreview() {
    ProvideWindowInsets {
        FabBottomSheet(
            fabClickLabel = "open",
            fabContent = { FabText("Clickme", it) },
            bottomSheetContent = { progress ->
                BottomSheetText(progress = progress) {
                    Column {
                        var text by remember { mutableStateOf("") }
                        TextField(text, onValueChange = { v -> text = v })
                        Row(Modifier.fillMaxWidth()) {
                            Spacer(
                                Modifier
                                    .fillMaxWidth(.5F * progress)
                                    .height(200.dp * progress)
                                    .background(Color.Red)
                            )
                            Spacer(
                                Modifier
                                    .fillMaxWidth(progress)
                                    .height(200.dp * progress)
                                    .background(Color.Green)
                            )
                        }
                    }
                }
            }
        )
    }
}
