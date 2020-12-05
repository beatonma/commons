package org.beatonma.commons.app.ui.compose.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HdrWeak
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.theme.compose.theme.CommonsRepeatable

private val rotationKey = FloatPropKey()

@Composable
fun Loading(
    modifier: Modifier = Modifier,
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            "Loading",
            style = typography.h3,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LoadingIcon(
    modifier: Modifier = Modifier,
    onClick: ActionBlock = {},
    state: MutableState<Boolean> = rememberBoolean(false),
    animSpec: AnimationSpec<Float> = CommonsRepeatable(repeatMode = RepeatMode.Reverse,
        duration = 600),
) {
    val transitionDef = remember {
        transitionDefinition<Boolean> {
            state(false) {
                this[rotationKey] = -30F
            }
            state(true) {
                this[rotationKey] = 220F
            }

            transition(false to true, true to false) {
                rotationKey using animSpec
            }
        }
    }
    val transition = transition(definition = transitionDef, toState = state.value)

    IconButton(onClick = onClick) {
        Icon(Icons.Default.HdrWeak, modifier = modifier.rotate(transition[rotationKey]))
    }

    state.value = true // Immediately change state value to start the animation.
}
