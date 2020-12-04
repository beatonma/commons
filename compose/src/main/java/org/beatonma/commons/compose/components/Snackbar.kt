package org.beatonma.commons.compose.components

import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.zIndex
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseTransition
import org.beatonma.commons.compose.modifiers.either
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.theme.compose.Elevation
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.theme.systemui.imeOrNavigationBarsPadding
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

val AmbientFeedbackMessage: ProvidableAmbient<MutableState<String>> =
    ambientOf { mutableStateOf("") }

/**
 * A container that provides snackbar-like text feedback to the user via AmbientFeedbackMessage
 */
@Composable
fun FeedbackLayout(
    alignment: Alignment = Alignment.TopCenter,
    content: @Composable () -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        content()

        FeedbackMessage(
            modifier = Modifier
                .zIndex(Layer.AlwaysOnTopSurface)
                .fillMaxWidth()
                .align(alignment),
            alignment = alignment,
        )
    }
}

@Composable
private fun FeedbackMessage(
    modifier: Modifier = Modifier,
    message: String = AmbientFeedbackMessage.current.value,
    alignment: Alignment,
    state: MutableState<ExpandCollapseState> = rememberExpandCollapseState(),
    transition: TransitionDefinition<ExpandCollapseState> = rememberExpandCollapseTransition(),
    transitionState: TransitionState = transition(transition, toState = state.value),
) {
    state.value =
        if (message.isBlank()) ExpandCollapseState.Collapsed else ExpandCollapseState.Expanded
    val progress = transitionState[progressKey]

    if (progress > 0F) {
        Surface(
            modifier.wrapContentHeight(progress),
            elevation = Elevation.ModalSurface,
        ) {
            Text(
                message,
                modifier = Modifier
                    .padding(progress.lerpBetween(Padding.Zero, Padding.Snackbar))
                    .either(alignment == Alignment.TopCenter,
                        whenTrue = { statusBarsPadding(scale = progress) },
                        whenFalse = { imeOrNavigationBarsPadding(scale = progress) }
                    )
                    .alpha(progress)
            )
        }
    }
}
