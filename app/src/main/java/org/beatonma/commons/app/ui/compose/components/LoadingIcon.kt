package org.beatonma.commons.app.ui.compose.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HdrWeak
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.theme.compose.theme.CommonsRepeatable

private val rotationKey = FloatPropKey("Track rotation of LoadingIcon")

@Composable
fun LoadingIcon(
    modifier: Modifier = Modifier,
    onClick: ActionBlock? = null,
    tint: Color = AmbientContentColor.current,
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
    val iconModifier = modifier.rotate(transition[rotationKey])

    if (onClick == null) {
        Icon(Icons.Default.HdrWeak, tint = tint, modifier = iconModifier)
    }
    else {
        IconButton(onClick = onClick) {
            Icon(Icons.Default.HdrWeak, tint = tint, modifier = iconModifier)
        }
    }

    state.value = true // Immediately change state value to start the animation.
}
