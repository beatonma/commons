package org.beatonma.commons.app.social

import androidx.compose.animation.AnimatedFloatModel
import androidx.compose.animation.asDisposableClock
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.util.ComposableBlock
import org.beatonma.commons.compose.util.lerp
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.triangle
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.Size
import kotlin.math.roundToInt

@Composable
internal fun SocialIcons(
    modifier: Modifier = Modifier,
    socialContent: SocialContent = AmbientSocialContent.current,
    inactiveTint: Color = AmbientContentColor.current.copy(alpha = ContentAlpha.medium),
    activeTint: Color = AmbientContentColor.current.copy(alpha = ContentAlpha.high),
    arrangement: Arrangement.Horizontal,
    onCommentIconClick: ActionBlock,
    onVoteUpIconClick: ActionBlock,
    onVoteDownIconClick: ActionBlock,
) {
    val clock = AmbientAnimationClock.current.asDisposableClock()
    val voteSelection = remember {
        SocialVoteType.values().map {
            AnimatedFloatModel(if (socialContent.userVote == it) 1F else 0F, clock)
        }
    }

    onCommit(socialContent) {
        voteSelection.forEachIndexed { index, fraction ->
            val target = if (index == socialContent.userVote?.ordinal) 1F else 0F
            if (fraction.targetValue != target) {
                fraction.animateTo(target)
            }
        }
    }

    Row(
        modifier,
        horizontalArrangement = arrangement,
    ) {
        val upvoteTint = voteSelection[0].value.lerpBetween(inactiveTint, activeTint)
        val downvoteTint = voteSelection[1].value.lerpBetween(inactiveTint, activeTint)

        CounterIcon(
            Modifier,
            socialContent.commentCount,
            Icons.Default.Comment,
            activeTint,
            onClick = onCommentIconClick
        )

        CounterIcon(
            Modifier,
            socialContent.ayeVotes,
            Icons.Default.ThumbUp,
            upvoteTint,
            onClick = onVoteUpIconClick
        )

        CounterIcon(
            Modifier,
            socialContent.noVotes,
            Icons.Default.ThumbDown,
            downvoteTint,
            onClick = onVoteDownIconClick
        )
    }
}

@Composable
private fun CounterIcon(
    modifier: Modifier = Modifier,
    count: Int,
    icon: ImageVector,
    tint: Color,
    style: IconStyle = AmbientIconStyle.current,
    onClick: ActionBlock,
) {
    CounterIcon(
        modifier,
        style.size,
        style.padding,
        count, icon, tint, onClick
    )
}

/**
 * Icon and counter text layout changes depending on transition progress from row-like to column-like.
 */
@Composable
private fun CounterIcon(
    modifier: Modifier,
    size: Dp,
    padding: PaddingValues,
    count: Int,
    icon: ImageVector,
    tint: Color,
    onClick: ActionBlock,
) {
    val progress = AmbientCollapseExpandProgress.current

    val content: ComposableBlock = {
        Icon(
            icon,
            tint = tint,
            modifier = Modifier
                .size(size)
                .layoutId("icon")
        )

        // Negative count indicates loading state. Show a placeholder instead of zero votes until loading done.
        val countText = if (count < 0) "-" else "$count"
        Text(
            countText,
            Modifier.layoutId("text")
        )
    }

    Layout(
        content,
        Modifier
            .clip(shapes.small)
            .clickable(onClick = onClick)
            .then(modifier)
            .padding(padding),
    ) { measurables, constraints ->
        val iconTextSpace = 4.dp.toIntPx() // Space between icon and text
        val avoidOffset =
            ((iconTextSpace * 5F).triangle(progress)).roundToInt() // Extra offset to avoid icon/text collision during animation
        val verticalSpace = progress.lerpBetween(0, iconTextSpace) + avoidOffset
        val horizontalSpace = progress.lerpBetween(iconTextSpace, 0) + avoidOffset

        val iconPlaceable = measurables.find { it.layoutId == "icon" }!!.measure(constraints)
        val textPlaceable = measurables.find { it.layoutId == "text" }!!.measure(constraints)

        val textStart = progress.lerpBetween(iconPlaceable.width + horizontalSpace, 0)
        val textTop = progress.lerpBetween(0, iconPlaceable.height + verticalSpace)

        val width = maxOf(iconPlaceable.width, textStart + textPlaceable.width)
        val height = maxOf(iconPlaceable.height, textTop + textPlaceable.height)

        layout(width, height) {
            iconPlaceable.placeRelative(
                progress.lerpBetween(0, (width - iconPlaceable.width) / 2),
                progress.lerpBetween((height - iconPlaceable.height) / 2, 0)
            )
            textPlaceable.placeRelative(
                progress.lerpBetween(textStart + horizontalSpace,
                    (width - textPlaceable.width) / 2),
                progress.lerpBetween((height - textPlaceable.height) / 2, textTop + verticalSpace)
            )
        }
    }
}

internal interface IconStyle {
    val size: Dp
    val padding: PaddingValues
}

internal object SmallIconStyle : IconStyle {
    override val size: Dp = Size.IconSmall
    override val padding = Padding.IconSmall
}

internal object LargeIconStyle : IconStyle {
    override val size: Dp = Size.IconLarge
    override val padding = Padding.IconLarge
}

internal fun IconStyle.lerp(other: IconStyle, progress: Float): IconStyle =
    object : IconStyle {
        override val size: Dp = progress.lerpBetween(this@lerp.size, other.size)
        override val padding = this@lerp.padding.lerp(other.padding, progress)
    }