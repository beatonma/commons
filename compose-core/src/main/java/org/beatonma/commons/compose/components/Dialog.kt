package org.beatonma.commons.compose.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansionAsState
import org.beatonma.commons.compose.animation.isExpanded
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.withEasing
import org.beatonma.commons.compose.modifiers.consumePointerInput
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.shape.BottomSheetShape
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.themed.animation
import kotlin.math.roundToInt


private val LocalDialogState: ProvidableCompositionLocal<MutableState<ExpandCollapseState>> =
    compositionLocalOf { error("LocalDialogState has not been provided") }

private val LocalDialogContent: ProvidableCompositionLocal<MutableState<DialogContent?>> =
    compositionLocalOf { error("LocalDialogContent has not been provided") }

private typealias DialogContent = @Composable BoxScope.(progress: Float) -> Unit

private val MaxDialogWidthDp = 500


/**
 * Scaffold wrapper for a layout that wants to display a dialog.
 *
 * Dialog() can only be called from within a DialogScaffold.
 */
@Composable
fun DialogScaffold(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDialogState provides remember { mutableStateOf(ExpandCollapseState.Collapsed) },
        LocalDialogContent provides remember { mutableStateOf(null) },
    ) {
        val visible = LocalDialogState.current
        val dialogContent = LocalDialogContent.current.value

        DialogScaffold(
            visible.value,
            { visible.value = ExpandCollapseState.Collapsed },
            dialogContent,
            content,
        )
    }
}

/**
 * Display a modal dialog with the given content.
 *
 * Must be called within the tree of DialogScaffold.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Dialog(
    state: ExpandCollapseState,
    onStateChange: (ExpandCollapseState) -> Unit,
    swipeToDismiss: Boolean = true,
    alignment: Alignment = Alignment.Center,
    parentShape: CornerBasedShape = shapes.medium,
    content: DialogContent,
) {
    val dialogContent = LocalDialogContent.current
    val dialogState = LocalDialogState.current

    BackHandler(enabled = dialogState.isExpanded) {
        dialogState.value = ExpandCollapseState.Collapsed
    }

    LaunchedEffect(state) {
        dialogState.value = state
        dialogContent.value = { progress ->
            val swipeState = rememberSwipeableState(initialValue = state)

            BoxWithConstraints(Modifier.align(alignment)) {
                val height = constraints.maxHeight.toFloat()

                Card(
                    Modifier
                        .onlyWhen(swipeToDismiss) {
                            swipeable(
                                state = swipeState,
                                orientation = Orientation.Vertical,
                                anchors = mapOf(
                                    0f to ExpandCollapseState.Expanded,
                                    height to ExpandCollapseState.Collapsed,
                                ),
                            ).offset {
                                IntOffset(0, swipeState.offset.value.roundToInt())
                            }
                        }
                        .dialogHeight()
                        .dialogWidth()
                        .align(alignment)
                        .consumePointerInput(),
                    shape = getDialogShape(alignment, parentShape, progress),
                ) {
                    Box(
                        Modifier
                            // Enter/exit animation
                            .wrapContentHeight(
                                progress
                                    .progressIn(0f, .7f)
                                    .withEasing(FastOutSlowInEasing))
                            // Animate changes in content once the dialog is fully visible.
                            .onlyWhen(progress == 1f) {
                                animateContentSize(animation.spec())
                            }
                            .alpha(progress.progressIn(.6f, 1f))
                    ) {
                        content(progress)
                    }
                }
            }

            LaunchedEffect(swipeState.currentValue) {
                onStateChange(swipeState.currentValue)
            }
        }
    }

    LaunchedEffect(dialogState.value) {
        onStateChange(dialogState.value)
    }

    DisposableEffect(Unit) {
        onDispose {
            dialogContent.value = null
            dialogState.value = ExpandCollapseState.Collapsed
            onStateChange(ExpandCollapseState.Collapsed)
        }
    }
}

@Composable
private fun DialogScaffold(
    state: ExpandCollapseState,
    onClose: () -> Unit,
    dialogContent: DialogContent?,
    content: @Composable () -> Unit,
) {
    val progress by state.animateExpansionAsState(animation.stiffSpring())

    Box(
        Modifier.fillMaxSize()
    ) {
        content()

        if (progress > 0f && dialogContent != null) {
            ModalScrim(
                visible = state.isExpanded,
                onClickLabel = "DialogScaffold",
                onClickAction = onClose,
            ) {
                dialogContent(progress)
            }
        }
    }
}

@Composable
private fun getDialogShape(
    alignment: Alignment,
    parentShape: CornerBasedShape,
    progress: Float,
): CornerBasedShape {
    val expandedShape = when (alignment) {
        Alignment.Bottom,
        Alignment.BottomStart,
        Alignment.BottomCenter,
        Alignment.BottomEnd,
        -> BottomSheetShape

        else -> shapes.medium
    }
    if (parentShape == expandedShape) return parentShape

    val eased = progress.withEasing(LinearOutSlowInEasing)

    return eased.lerpBetween(parentShape, expandedShape)
}


private val DialogMaxWidth: Dp
    @Composable
    get() = with(LocalConfiguration.current) {
        val screenWidth = screenWidthDp
        if (screenWidth > MaxDialogWidthDp) {
            MaxDialogWidthDp.dp
        } else {
            Dp.Infinity
        }
    }

private val DialogMaxHeight: Dp
    @Composable
    get() = with(LocalConfiguration.current) {
        return (screenHeightDp * .6f).dp
    }


private fun Modifier.dialogWidth(): Modifier = composed {
    val maxWidth = DialogMaxWidth
    if (maxWidth == Dp.Infinity) {
        fillMaxWidth()
    } else {
        widthIn(0.dp, maxWidth)
    }
}

private fun Modifier.dialogHeight(): Modifier = composed {
    wrapContentHeight()
        .heightIn(0.dp, DialogMaxHeight)
}
