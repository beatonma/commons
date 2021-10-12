package org.beatonma.commons.app.ui.components.charts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.animation.asFloatAnimationTarget
import org.beatonma.commons.themed.themedAnimation

@Composable
inline fun ChartKeyItem(
    icon: @Composable RowScope.() -> Unit,
    description: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        description()
    }
}

fun Modifier.selectionDecoration(
    selected: Boolean,
    thickness: Dp = 2.dp,
    color: Color? = null,
    cap: StrokeCap = StrokeCap.Round,
) = composed {
    val resolvedColor = color ?: LocalContentColor.current.copy(alpha = .75F)
    val animatedBackgroundColor by themedAnimation.animateColorAsState(
        if (selected) resolvedColor.copy(alpha = .15f) else Color.Transparent
    )
    val animatedWidth by themedAnimation.animateFloatAsState(selected.asFloatAnimationTarget)

    drawWithCache {
        val strokeWidth = thickness.toPx()
        val width = animatedWidth * size.width
        val halfWidth = width / 2f
        val y = size.height - (strokeWidth / 2f)
        val centerX = size.width / 2f

        onDrawWithContent {
            drawRect(animatedBackgroundColor)

            drawContent()

            if (animatedWidth > 0f) {
                drawLine(
                    resolvedColor,
                    start = Offset(centerX - halfWidth, y),
                    end = Offset(centerX + halfWidth, y),
                    strokeWidth = strokeWidth,
                    cap = cap,
                )
            }
        }
    }
}
