package org.beatonma.commons.theme.compose.padding

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.theme.compose.theme.CommonsSpring

@Composable
fun animatePaddingAsState(
    targetValue: Padding,
    animationSpec: AnimationSpec<Padding> = CommonsSpring(),
    finishedListener: ((Padding) -> Unit)? = null
) = animateValueAsState(
    targetValue = targetValue,
    typeConverter = PaddingVectorConverter,
    animationSpec = animationSpec,
    finishedListener = finishedListener
)

@Composable
fun <S> Transition<S>.animatePadding(
    transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Padding> = {
        CommonsSpring()
    },
    label: String = "PaddingAnimation",
    targetValueByState: @Composable (state: S) -> Padding,
): State<Padding> = animateValue(
    PaddingVectorConverter, transitionSpec, label, targetValueByState
)

private val PaddingVectorConverter: TwoWayConverter<Padding, AnimationVector4D> =
    TwoWayConverter(
        convertToVector = {
            AnimationVector4D(
                it.start.value, it.top.value, it.end.value, it.bottom.value
            )
        },
        convertFromVector = {
            Padding(it.v1.dp, it.v2.dp, it.v3.dp, it.v4.dp)
        }
    )

@Composable @Preview
fun PaddingAnimationPreview() {
    var targetPadding by remember { mutableStateOf(Padding.Zero) }
    val padding by animatePaddingAsState(targetPadding)

    Box(
        Modifier
            .clickable(
                indication = null,
                interactionSource = remember(::MutableInteractionSource)
            ) {
                targetPadding = listOf(
                    Padding.Image,
                    Padding.Zero,
                    Padding.Screen,
                    Padding.VerticalListItemLarge
                ).random()
            }
            .size(256.dp)
            .background(Color.Green)
            .padding(padding)
            .background(Color.Red)
        ,
    )
}
