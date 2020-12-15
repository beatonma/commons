package org.beatonma.commons.app.ui.compose.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.transition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.TwoState
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.animation.twoStateProgressTransition
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.modifiers.either
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.map
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.theme.compose.Elevation
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.theme.CORNER_SMALL
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import org.beatonma.commons.theme.compose.theme.systemui.imeOrNavigationBarsPadding
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding

enum class FabBottomSheetState : TwoState<FabBottomSheetState> {
    Fab,
    BottomSheet,
    ;

    override fun toggle() = when (this) {
        Fab -> BottomSheet
        BottomSheet -> Fab
    }
}

@Composable
fun rememberFabBottomSheetState() = remember { mutableStateOf(FabBottomSheetState.Fab) }

/**
 * Displays a FAB which expands into a bottom sheet dialog for comment authoring.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FabBottomSheet(
    modifier: Modifier = Modifier,
    uiState: MutableState<FabBottomSheetState> = rememberFabBottomSheetState(),
    surfaceColor: Color? = null,
    contentColor: Color? = null,
    transition: TransitionDefinition<FabBottomSheetState> = rememberFabBottomSheetTransition(),
    transitionState: TransitionState = transition(transition, toState = uiState.value),
    onDismiss: ActionBlock = {},
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit,
) {
    val progress = transitionState[progressKey]
    val resolvedSurfaceColor = surfaceColor ?: getSurfaceColor(progress)

    FabBottomSheetLayout(
        uiState,
        progress,
        fabContent,
        bottomSheetContent,
        modifier,
        surfaceColor = resolvedSurfaceColor,
        contentColor = contentColor ?: contentColorFor(resolvedSurfaceColor),
        onDismiss = onDismiss,
    )
}

/**
 * Displays a FAB which expands into a bottom sheet dialog for comment authoring.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableFabBottomSheet(
    modifier: Modifier = Modifier,
    uiState: MutableState<FabBottomSheetState> = rememberFabBottomSheetState(),
    swipeState: SwipeableState<FabBottomSheetState> = rememberSwipeableState(
        initialValue = uiState.value,
        animationSpec = CommonsSpring(),
    ),
    surfaceColor: Color? = null,
    contentColor: Color? = null,
    swipeAnchors: Map<Float, FabBottomSheetState> = mapOf(
        0F to FabBottomSheetState.Fab,
        500F to FabBottomSheetState.BottomSheet,
    ),
    onDismiss: ActionBlock = {},
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit,
) {
    val offset = swipeState.offset.value
    val progress = (if (offset.isNaN()) 0F else offset).map(0F, 500F, 0F, 1F)

    val targetState = uiState.value
    if (targetState != swipeState.value) {
        swipeState.animateTo(targetState, onEnd = { _, finalState ->
            uiState.update(finalState)
        })
    }

    val resolvedSurfaceColor = surfaceColor ?: getSurfaceColor(progress)

    FabBottomSheetLayout(
        uiState,
        progress,
        fabContent,
        bottomSheetContent,
        modifier.swipeable(
            state = swipeState,
            anchors = swipeAnchors,
            orientation = Orientation.Vertical,
            reverseDirection = true,
        ),
        surfaceColor = resolvedSurfaceColor,
        contentColor = contentColor ?: contentColorFor(resolvedSurfaceColor),
        onDismiss = onDismiss,
    )
}

@Composable
fun FabText(
    text: String,
    progress: Float,
) {
    Text(
        text,
        Modifier
            .padding(Padding.ExtendedFabContent)
            .alpha(progress.reversed().progressIn(0.8F, 1.0F)),
        style = typography.button,
    )
}

@Composable
fun BottomSheetContent(
    progress: Float,
    handleImeInsets: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        Modifier
            .either(
                condition = handleImeInsets,
                whenTrue = { imeOrNavigationBarsPadding(scale = progress) },
                whenFalse = { navigationBarsPadding(scale = progress) },
            )
            .wrapContentSize(
                horizontalProgress = progress.progressIn(0F, 0.4F),
                verticalProgress = progress.progressIn(0F, 0.8F)
            )
            .alpha(progress.progressIn(0.8F, 1F)),
        content = content,
    )
}

@Composable
fun BottomSheetText(
    progress: Float,
    handleImeInsets: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    BottomSheetContent(progress, handleImeInsets) {
        CardText(content = content)
    }
}

@Composable
private fun FabBottomSheetLayout(
    uiState: MutableState<FabBottomSheetState>,
    progress: Float,
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit,
    modifier: Modifier,
    surfaceColor: Color,
    contentColor: Color,
    onDismiss: ActionBlock,
) {
    val targetState = uiState.value

    ModalScrim(
        alignment = Alignment.BottomEnd,
        alpha = progress,
        onClickAction = {
            uiState.update(FabBottomSheetState.Fab)
            onDismiss()
        }
    ) {
        val surfaceShape = getSurfaceShape(progress)

        Surface(
            modifier
                .padding(progress.lerpBetween(Padding.Fab, Padding.Zero))
                .imeOrNavigationBarsPadding(scale = progress.reversed()) // The bottom sheet should extend behind navigation bar as it expands.
                .clickable {
                    if (targetState == FabBottomSheetState.Fab) {
                        uiState.update(FabBottomSheetState.BottomSheet)
                    }
                }
                .shadow(Elevation.ModalSurface, surfaceShape),
            shape = surfaceShape,
            color = surfaceColor,
            contentColor = contentColor,
        ) {
            Box {
                if (progress != 1F) {
                    fabContent(progress)
                }
                if (progress != 0F) {
                    bottomSheetContent(progress)
                }
            }
        }
    }
}

@Composable
fun rememberFabBottomSheetTransition() = remember {
    twoStateProgressTransition(FabBottomSheetState.Fab, FabBottomSheetState.BottomSheet)
}

private fun getSurfaceShape(progress: Float): Shape {
    val eased = progress.withEasing(LinearOutSlowInEasing)
    val upperCornerSize = eased.lerpBetween(24, CORNER_SMALL).dp
    val lowerCornerSize = eased.lerpBetween(24, 0).dp
    return RoundedCornerShape(upperCornerSize, upperCornerSize, lowerCornerSize, lowerCornerSize)
}

@Composable
private fun getSurfaceColor(progress: Float) =
    progress.progressIn(0F, 0.4F).lerpBetween(colors.primary, colors.surface)
