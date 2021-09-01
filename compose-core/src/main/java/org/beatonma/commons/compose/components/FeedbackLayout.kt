package org.beatonma.commons.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.zIndex
import org.beatonma.commons.compose.Layer
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansionAsState
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.systemui.imeOrNavigationBarsPadding
import org.beatonma.commons.compose.systemui.statusBarsPadding
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.themed.themedElevation
import org.beatonma.commons.themed.themedPadding

typealias FeedbackProvider = MutableState<AnnotatedString?>

@Composable
fun rememberFeedbackProvider(default: AnnotatedString? = null) =
    remember { mutableStateOf(default) }

val LocalFeedbackMessage: ProvidableCompositionLocal<MutableState<AnnotatedString?>> =
    compositionLocalOf { mutableStateOf(null) }

fun MutableState<AnnotatedString?>.clear() {
    value = null
}

private val EmptyText = AnnotatedString("")

@Composable
fun ShowFeedback(message: AnnotatedString?) {
    LocalFeedbackMessage.current.value = message
}

@Composable
fun ShowFeedback(message: String?) {
    if (message == null) LocalFeedbackMessage.current.clear()
    else ShowFeedback(message = AnnotatedString(message))
}

/**
 * A container that provides snackbar-like text feedback to the user via AmbientFeedbackMessage
 */
@Composable
fun FeedbackLayout(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopCenter,
    message: AnnotatedString? = LocalFeedbackMessage.current.value,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        content()

        FeedbackMessage(
            message = message,
            alignment = alignment,
            modifier = Modifier
                .zIndex(Layer.AlwaysOnTopSurface)
                .fillMaxWidth()
                .align(alignment),
        )
    }
}

@Composable
private fun FeedbackMessage(
    message: AnnotatedString?,
    alignment: Alignment,
    modifier: Modifier,
) {
    val state = if (message.isNullOrBlank()) {
        ExpandCollapseState.Collapsed
    } else {
        ExpandCollapseState.Expanded
    }

    FeedbackMessageLayout(
        message = message,
        state = state,
        alignment = alignment,
        modifier = modifier
    )
}

@Composable
private fun FeedbackMessageLayout(
    message: AnnotatedString?,
    state: ExpandCollapseState,
    alignment: Alignment,
    modifier: Modifier,
) {
    val progress by state.animateExpansionAsState()
    val sizeProgress = progress.progressIn(0F, .6F)
    val alphaProgress = progress.progressIn(.7F, 1F)

    if (progress > 0F) {
        Surface(
            modifier
                .wrapContentHeight(sizeProgress)
                .testTag(TestTag.FeedbackSurface),
            elevation = themedElevation.ModalSurface,
        ) {
            Text(
                message ?: EmptyText,
                modifier = Modifier
                    .wrapContentHeight(sizeProgress)
                    .padding(themedPadding.Snackbar)
                    .onlyWhen(alignment.isAtTop) {
                        statusBarsPadding(scale = sizeProgress)
                    }
                    .onlyWhen(alignment.isAtBottom) {
                        imeOrNavigationBarsPadding(scale = sizeProgress)
                    }
                    .alpha(alphaProgress)
                    .testTag(TestTag.FeedbackMessage)
            )
        }
    }
}


private val Alignment.isAtTop: Boolean
    get() = this in listOf(
        Alignment.Top,
        Alignment.TopStart,
        Alignment.TopCenter,
        Alignment.TopEnd
    )

private val Alignment.isAtBottom: Boolean
    get() = this in listOf(
        Alignment.Bottom,
        Alignment.BottomStart,
        Alignment.BottomCenter,
        Alignment.BottomEnd
    )
