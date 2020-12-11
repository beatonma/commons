package org.beatonma.commons.compose.components

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonConstants
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.animation.AutoCollapse
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.theme.compose.theme.CommonsButtons
import org.beatonma.commons.theme.compose.theme.CommonsSpring

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
    safeColors: ButtonColors = CommonsButtons.outlineButtonColors(),
    awaitingConfirmationColors: ButtonColors = CommonsButtons.warningButtonColors(),
    confirmedColor: ButtonColors = CommonsButtons.outlineButtonColors(),
) = DoubleConfirmationButtonColors(safeColors, awaitingConfirmationColors, confirmedColor)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoubleConfirmationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    confirmationState: MutableState<ConfirmationState> = remember { mutableStateOf(ConfirmationState.Safe) },
    interactionState: InteractionState = remember { InteractionState() },
    elevation: ButtonElevation? = null,
    shape: Shape = shapes.small,
    border: BorderStroke? = ButtonConstants.defaultOutlinedBorder,
    colors: DoubleConfirmationButtonColors = doubleConfirmationColors(),
    autoCollapse: Long = AutoCollapse.Default,
    contentPadding: PaddingValues = ButtonConstants.DefaultContentPadding,
    safeContent: @Composable (progress: Float) -> Unit,
    awaitingConfirmationContent: @Composable (progress: Float) -> Unit,
    confirmedContent: @Composable (progress: Float) -> Unit,
) {
    val transitionDef = rememberTransitionDefinition(colors)
    val transition = transition(transitionDef, toState = confirmationState.value)

    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            when (confirmationState.value) {
                ConfirmationState.Safe -> {
                    confirmationState.update(ConfirmationState.AwaitingConfirmation)
                    coroutineScope.launch {
                        delay(autoCollapse)
                        if (confirmationState.value == ConfirmationState.AwaitingConfirmation) {
                            withContext(Dispatchers.Main) {
                                confirmationState.update(ConfirmationState.Safe)
                            }
                        }
                    }
                }

                ConfirmationState.AwaitingConfirmation -> {
                    confirmationState.update(ConfirmationState.Confirmed)
                    onClick()
                }

                ConfirmationState.Confirmed -> Unit
            }
        },
        modifier = modifier,
        enabled = enabled,
        interactionState = interactionState,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = CommonsButtons.buttonColors(
            contentColor = transition[contentColorKey],
            backgroundColor = transition[backgroundColorKey]
        ),
        contentPadding = contentPadding,
        content = {
            Box(Modifier.animateContentSize()) {
                when (confirmationState.value) {
                    ConfirmationState.Safe -> {
                        safeContent(transition[firstProgress])
                    }

                    ConfirmationState.AwaitingConfirmation -> {
                        awaitingConfirmationContent(transition[firstProgress])
                    }

                    ConfirmationState.Confirmed -> {
                        confirmedContent(transition[secondProgress])
                    }
                }
            }
        }
    )
}

private val backgroundColorKey = ColorPropKey()
private val contentColorKey = ColorPropKey()
private val firstProgress = FloatPropKey()
private val secondProgress = FloatPropKey()

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun rememberTransitionDefinition(
    buttonColors: DoubleConfirmationButtonColors,
): TransitionDefinition<ConfirmationState> {
    return remember {
        transitionDefinition {
            state(ConfirmationState.Safe) {
                this[firstProgress] = 0F
                this[secondProgress] = 0F
                this[backgroundColorKey] = buttonColors.safeColors.backgroundColor(true)
                this[contentColorKey] = buttonColors.safeColors.contentColor(true)
            }

            state(ConfirmationState.AwaitingConfirmation) {
                this[firstProgress] = 1F
                this[secondProgress] = 0F
                this[backgroundColorKey] =
                    buttonColors.awaitingConfirmationColors.backgroundColor(true)
                this[contentColorKey] = buttonColors.awaitingConfirmationColors.contentColor(true)
            }

            state(ConfirmationState.Confirmed) {
                this[firstProgress] = 1F
                this[secondProgress] = 1F
                this[backgroundColorKey] = buttonColors.confirmedColor.backgroundColor(true)
                this[contentColorKey] = buttonColors.confirmedColor.contentColor(true)
            }

            transition(
                ConfirmationState.Safe to ConfirmationState.AwaitingConfirmation,
                ConfirmationState.AwaitingConfirmation to ConfirmationState.Safe,
                ConfirmationState.AwaitingConfirmation to ConfirmationState.Confirmed,
            ) {
                firstProgress using CommonsSpring()
                secondProgress using CommonsSpring()
                backgroundColorKey using CommonsSpring()
                contentColorKey using CommonsSpring()
            }
        }
    }
}
