package org.beatonma.commons.compose.components.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.beatonma.commons.compose.animation.AutoCollapse
import org.beatonma.commons.themed.themedButtons

enum class ConfirmationState {
    /**
     * Touching the button will only change the state of the button - no side effects will happen.
     */
    Safe,

    /**
     * Touching the button will trigger the side effect!
     */
    AwaitingConfirmation,

    /**
     * The action has been confirmed and side effects have been triggered.
     */
    Confirmed,
    ;
}

@Composable
fun rememberConfirmationState(default: ConfirmationState = ConfirmationState.Safe) =
    remember { mutableStateOf(default) }

data class DoubleConfirmationButtonColors @OptIn(ExperimentalMaterialApi::class) internal constructor(
    val safeColors: ButtonColors,
    val awaitingConfirmationColors: ButtonColors,
    val confirmedColor: ButtonColors,
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun doubleConfirmationColors(
    safeColors: ButtonColors = themedButtons.outlineButtonColors(),
    awaitingConfirmationColors: ButtonColors = themedButtons.warningButtonColors(),
    confirmedColor: ButtonColors = themedButtons.outlineButtonColors(),
) = DoubleConfirmationButtonColors(safeColors, awaitingConfirmationColors, confirmedColor)

@Composable
fun DoubleConfirmationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember(::MutableInteractionSource),
    elevation: ButtonElevation? = null,
    shape: Shape = shapes.small,
    border: BorderStroke? = ButtonDefaults.outlinedBorder,
    colors: DoubleConfirmationButtonColors = doubleConfirmationColors(),
    autoCollapse: Long = AutoCollapse.Default,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    safeContent: @Composable () -> Unit,
    awaitingConfirmationContent: @Composable () -> Unit,
    confirmedContent: @Composable () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    var state by rememberConfirmationState()

    DoubleConfirmationButton(
        onClick,
        state,
        { state = it },
        modifier,
        enabled,
        interactionSource,
        elevation,
        shape,
        border,
        colors,
        autoCollapse,
        contentPadding,
        safeContent,
        awaitingConfirmationContent,
        confirmedContent,
        coroutineScope,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DoubleConfirmationButton(
    onClick: () -> Unit,
    state: ConfirmationState,
    onStateChange: (ConfirmationState) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember(::MutableInteractionSource),
    elevation: ButtonElevation? = null,
    shape: Shape = shapes.small,
    border: BorderStroke? = ButtonDefaults.outlinedBorder,
    colors: DoubleConfirmationButtonColors = doubleConfirmationColors(),
    autoCollapse: Long = AutoCollapse.Default,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    safeContent: @Composable () -> Unit,
    awaitingConfirmationContent: @Composable () -> Unit,
    confirmedContent: @Composable () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val transition = updateTransition(state, label = "confirmation-state")

    val contentColor by transition.animateColor(label = "content-color") {
        when (it) {
            ConfirmationState.Safe -> colors.safeColors.contentColor(enabled)
            ConfirmationState.AwaitingConfirmation -> colors.awaitingConfirmationColors.contentColor(
                enabled)
            ConfirmationState.Confirmed -> colors.confirmedColor.contentColor(enabled)
        }.value
    }

    val backgroundColor by transition.animateColor(label = "background-color") {
        when (it) {
            ConfirmationState.Safe -> colors.safeColors.backgroundColor(enabled)
            ConfirmationState.AwaitingConfirmation -> colors.awaitingConfirmationColors.backgroundColor(
                enabled)
            ConfirmationState.Confirmed -> colors.confirmedColor.backgroundColor(enabled)
        }.value
    }

    val filteredOnClick: () -> Unit = {
        when (state) {
            ConfirmationState.Safe -> {
                onStateChange(ConfirmationState.AwaitingConfirmation)
                coroutineScope.launch {
                    delay(autoCollapse)
                    onStateChange(ConfirmationState.Safe)
                }
            }

            ConfirmationState.AwaitingConfirmation -> {
                onStateChange(ConfirmationState.Confirmed)
                onClick()
            }

            ConfirmationState.Confirmed -> Unit
        }
    }

    LaunchedEffect(state) {
        if (state == ConfirmationState.Confirmed) {
            coroutineScope.cancel()
        }
    }

    Button(
        onClick = filteredOnClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = themedButtons.buttonColors(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        ),
        contentPadding = contentPadding,
        content = {
            Box(Modifier.animateContentSize()) {
                this@Button.AnimatedVisibility(state == ConfirmationState.Safe) {
                    safeContent()
                }

                this@Button.AnimatedVisibility(state == ConfirmationState.AwaitingConfirmation) {
                    awaitingConfirmationContent()
                }

                this@Button.AnimatedVisibility(state == ConfirmationState.Confirmed) {
                    confirmedContent()
                }
            }
        }
    )
}
