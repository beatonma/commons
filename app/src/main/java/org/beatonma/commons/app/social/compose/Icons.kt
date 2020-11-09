package org.beatonma.commons.app.social.compose

import androidx.compose.animation.AnimatedFloatModel
import androidx.compose.animation.asDisposableClock
import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.InternalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.layout.id
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.util.ComposableBlock
import org.beatonma.commons.compose.util.lerp
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.triangle
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.theme.positive
import kotlin.math.roundToInt

@OptIn(InternalLayoutApi::class)
@Composable
internal fun SocialIcons(
    socialContent: SocialContent = AmbientSocialContent.current,
    modifier: Modifier = Modifier,
    tint: Color = AmbientContentColor.current,
    arrangement: Arrangement.Horizontal,
    onCommentIconClick: ActionBlock,
    onVoteUpIconClick: ActionBlock,
    onVoteDownIconClick: ActionBlock,
) {
    val clock = AnimationClockAmbient.current.asDisposableClock()
    val voteSelection = remember {
        SocialVoteType.values().map {
            AnimatedFloatModel(if (socialContent.userVote == it) 1F else 0F, clock)
        }
    }

    onCommit(socialContent.userVote) {
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
        val upvoteTint =
            tint.lerp(colors.positive, voteSelection[0].value)
        val downvoteTint =
            tint.lerp(colors.positive, voteSelection[1].value)

        CounterIcon(
            Modifier,
            socialContent.commentCount,
            Icons.Default.Comment,
            tint,
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
    icon: VectorAsset,
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
    icon: VectorAsset,
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
        Text(
            "$count",
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
            +((iconTextSpace * 5F).triangle(progress)).roundToInt() // Extra offset to avoid icon/text collision during animation
        val verticalSpace = 0.lerp(iconTextSpace, progress) + avoidOffset
        val horizontalSpace = iconTextSpace.lerp(0, progress) + avoidOffset

        val iconPlaceable = measurables.find { it.id == "icon" }!!.measure(constraints)
        val textPlaceable = measurables.find { it.id == "text" }!!.measure(constraints)

        val textStart = (iconPlaceable.width + horizontalSpace).lerp(0, progress)
        val textTop = 0.lerp(iconPlaceable.height + verticalSpace, progress)

        val width = maxOf(iconPlaceable.width, textStart + textPlaceable.width)
        val height = maxOf(iconPlaceable.height, textTop + textPlaceable.height)

        layout(width, height) {
            iconPlaceable.placeRelative(
                0.lerp((width - iconPlaceable.width) / 2, progress),
                ((height - iconPlaceable.height) / 2).lerp(0, progress)
            )
            textPlaceable.placeRelative(
                (textStart + horizontalSpace).lerp((width - textPlaceable.width) / 2, progress),
                ((height - textPlaceable.height) / 2).lerp(textTop + verticalSpace, progress)
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
        override val size: Dp = this@lerp.size.lerp(other.size, progress)
        override val padding = this@lerp.padding.lerp(other.padding, progress)
    }
