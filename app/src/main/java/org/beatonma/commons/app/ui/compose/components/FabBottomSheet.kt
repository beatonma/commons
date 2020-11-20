package org.beatonma.commons.app.ui.compose.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.modifiers.either
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
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding

enum class FabBottomSheetState {
    Fab,
    BottomSheet,
}

/**
 * Displays a FAB which expands into a bottom sheet dialog for comment authoring.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FabBottomSheet(
    uiState: MutableState<FabBottomSheetState> = rememberFabBottomSheetState(),
    transition: TransitionDefinition<FabBottomSheetState> = rememberFabBottomSheetTransition(),
    transitionState: TransitionState = transition(transition, toState = uiState.value),
    fabContent: @Composable (progress: Float) -> Unit,
    bottomSheetContent: @Composable (progress: Float) -> Unit,
) {
    val progress = transitionState[progressKey]

    val targetState = uiState.value

    ModalScrim(
        alignment = Alignment.BottomEnd,
        alpha = progress,
        onClickAction = {
            uiState.update(FabBottomSheetState.Fab)
        }
    ) {
        val surfaceShape = getFabBottomsheetSurfaceShape(progress)

        Surface(
            Modifier
                .padding(progress.lerpBetween(Padding.Fab, Padding.Zero))
                .navigationBarsPadding(scale = progress.reversed()) // The bottom sheet should extend behind navigation bar as it expands.
                .either(
                    condition = targetState == FabBottomSheetState.Fab,
                    whenTrue = {
                        clickable { uiState.update(FabBottomSheetState.BottomSheet) }
                    },
                    whenFalse = {
                        clickable { }
                    }
                )
                .drawShadow(Elevation.ModalSurface, surfaceShape),
            shape = surfaceShape,
            color = progress.progressIn(0F, 0.4F)
                .lerpBetween(colors.primary, colors.surface),
        ) {
            Box {
                fabContent(progress)
                bottomSheetContent(progress)
            }
        }
    }
}

/**
 * Displays a FAB which expands into a bottom sheet dialog for comment authoring.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableFabBottomSheet(
    uiState: MutableState<FabBottomSheetState> = rememberFabBottomSheetState(),
    swipeState: SwipeableState<FabBottomSheetState> = rememberSwipeableState(
        initialValue = uiState.value,
        animationSpec = CommonsSpring(),
    ),
    swipeAnchors: Map<Float, FabBottomSheetState> = mapOf(
        0F to FabBottomSheetState.Fab,
        500F to FabBottomSheetState.BottomSheet
    ),
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
    val swipeable = Modifier.swipeable(
        state = swipeState,
//            enabled = swipeState.value == FabBottomSheetState.BottomSheet,
        anchors = swipeAnchors,
        orientation = Orientation.Vertical,
        reverseDirection = true,
    )

    ModalScrim(
        alignment = Alignment.BottomEnd,
        alpha = progress,
        onClickAction = {
            uiState.update(FabBottomSheetState.Fab)
        }
    ) {
        val surfaceShape = getFabBottomsheetSurfaceShape(progress)

        Surface(
            Modifier
                .padding(progress.lerpBetween(Padding.Fab, Padding.Zero))
                .navigationBarsPadding(scale = progress.reversed()) // The bottom sheet should extend behind navigation bar as it expands.
                .then(swipeable)
                .either(
                    condition = targetState == FabBottomSheetState.Fab,
                    whenTrue = {
                        clickable { uiState.update(FabBottomSheetState.BottomSheet) }
                    },
                    whenFalse = {
                        clickable { }
                    }
                )
                .drawShadow(Elevation.ModalSurface, surfaceShape),
            shape = surfaceShape,
            color = progress.progressIn(0F, 0.4F).lerpBetween(colors.primary, colors.surface),
        ) {
            Box {
                fabContent(progress)
                bottomSheetContent(progress)
            }
        }
    }
}

@Composable
fun FabText(
    text: String,
    progress: Float,
) {
    if (progress == 1F) {
        return
    }

    Text(
        text,
        Modifier
            .padding(Padding.ExtendedFabContent)
            .drawOpacity(progress.reversed().progressIn(0.8F, 1.0F)),
        color = colors.onSecondary,
        style = typography.button,
    )
}

private fun getFabBottomsheetSurfaceShape(progress: Float): Shape {
    val eased = progress.withEasing(LinearOutSlowInEasing)
    val upperCornerSize = eased.lerpBetween(24, CORNER_SMALL).dp
    val lowerCornerSize = eased.lerpBetween(24, 0).dp
    return RoundedCornerShape(upperCornerSize, upperCornerSize, lowerCornerSize, lowerCornerSize)
}

@Composable
fun rememberFabBottomSheetState() = remember { mutableStateOf(FabBottomSheetState.Fab) }

@Composable
private fun rememberFabBottomSheetTransition() = remember {
    transitionDefinition<FabBottomSheetState> {
        state(FabBottomSheetState.Fab) {
            this[progressKey] = 0F
        }
        state(FabBottomSheetState.BottomSheet) {
            this[progressKey] = 1F
        }

        transition(
            FabBottomSheetState.Fab to FabBottomSheetState.BottomSheet,
            FabBottomSheetState.BottomSheet to FabBottomSheetState.Fab,
        ) {
            progressKey using CommonsSpring()
        }
    }
}
