package org.beatonma.commons.compose.padding

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.unit.dp
import org.beatonma.commons.themed.Padding
import org.beatonma.commons.themed.themedAnimation

@Composable
fun animatePaddingAsState(
    targetValue: Padding,
    animationSpec: AnimationSpec<Padding> = themedAnimation.spec(),
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
        themedAnimation.spec()
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
