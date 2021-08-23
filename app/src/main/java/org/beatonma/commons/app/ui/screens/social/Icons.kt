package org.beatonma.commons.app.ui.screens.social

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.accessibility.contentDescription
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.withNotNull
import org.beatonma.commons.compose.util.ComposableBlock
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.triangle
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import kotlin.math.roundToInt

@Composable
internal fun SocialIcons(
    socialContent: SocialContent,
    state: SocialUiState,
    theme: SocialTheme,
    actions: SocialActions,
    modifier: Modifier,
    expandAction: () -> Unit,
    collapseAction: () -> Unit,
) {
    val progress by state.animateExpansionAsState()

    val iconStyle = progress.lerpBetween(SmallIconStyle, LargeIconStyle)

    val inactiveTint = theme.inactiveColor(progress)
    val activeTint = theme.activeColor(progress)

    val voteColors =
        SocialVoteType.values().map {
            if (progress == 1F) {
                animation.animateColorAsState(if (socialContent.userVote == it) activeTint else inactiveTint)
            }
            else {
                derivedStateOf { inactiveTint }
            }
        }

    val contentDescription = socialContent.contentDescription
    val expandActionContentDescription = stringResource(R.string.content_description_social_action_open)

    SocialIconsLayout(
        progress,
        modifier = modifier
            .onlyWhen(state == SocialUiState.Collapsed) {
                this
                    .semantics(mergeDescendants = true) {
                        this.contentDescription = contentDescription
                    }
                    .clickable(
                        onClick = expandAction,
                        onClickLabel = expandActionContentDescription
                    )
            },
        socialContent = socialContent,
        inactiveTint = inactiveTint,
        voteColors = voteColors,
        iconStyle = iconStyle,
        arrangement = Arrangement.SpaceEvenly,
        onCommentIconClick = collapseAction,
        onVoteUpIconClick = actions.onVoteUpClick,
        onVoteDownIconClick = actions.onVoteDownClick,
    )
}

@Composable
private fun SocialIconsLayout(
    expandProgress: Float,
    modifier: Modifier,
    socialContent: SocialContent,
    inactiveTint: Color,
    voteColors: List<State<Color>>,
    iconStyle: IconStyle,
    arrangement: Arrangement.Horizontal,
    onCommentIconClick: () -> Unit,
    onVoteUpIconClick: () -> Unit,
    onVoteDownIconClick: () -> Unit,
) {
    Row(
        modifier.testTag(SocialTestTag.Icons),
        horizontalArrangement = arrangement,
    ) {
        CounterIconLayout(
            expandProgress,
            contentDescription = stringResource(
                R.string.content_description_social_comment_count,
                socialContent.commentCount
            ),
            size = iconStyle.size,
            padding = iconStyle.padding,
            count = socialContent.commentCount,
            icon = Icons.Default.Comment,
            tint = inactiveTint,
            onClick = when (expandProgress) {
                1F -> onCommentIconClick
                else -> null
            },
            onClickLabel = null,
        )

        CounterIconLayout(
            expandProgress,
            contentDescription = stringResource(
                R.string.content_description_social_votes_count_for,
                socialContent.ayeVotes
            ),
            size = iconStyle.size,
            padding = iconStyle.padding,
            count = socialContent.ayeVotes,
            icon = Icons.Default.ThumbUp,
            tint = voteColors[0].value,
            onClick = when (expandProgress) {
                1F -> onVoteUpIconClick
                else -> null
            },
            onClickLabel = stringResource(
                R.string.content_description_social_action_vote_for,
                socialContent.title
            )
        )

        CounterIconLayout(
            expandProgress,
            contentDescription = stringResource(
                R.string.content_description_social_votes_count_against,
                socialContent.noVotes
            ),
            size = iconStyle.size,
            padding = iconStyle.padding,
            count = socialContent.noVotes,
            icon = Icons.Default.ThumbDown,
            tint = voteColors[1].value,
            onClick = when (expandProgress) {
                1F -> onVoteDownIconClick
                else -> null
            },
            onClickLabel = stringResource(
                R.string.content_description_social_action_vote_against,
                socialContent.title
            )
        )
    }
}

/**
 * Icon and counter text layout changes depending on transition progress from row-like to column-like.
 */
@Composable
private fun CounterIconLayout(
    expandProgress: Float,
    contentDescription: String,
    size: Dp,
    padding: Padding,
    count: Int,
    icon: ImageVector,
    tint: Color,
    onClick: (() -> Unit)?,
    onClickLabel: String?,
) {
    val content: ComposableBlock = {
        Icon(
            icon,
            contentDescription = null, // Set by parent layout
            tint = tint,
            modifier = Modifier
                .size(size)
                .layoutId(LayoutId.Icon)
        )

        // Negative count indicates loading state. Show a placeholder instead of zero votes until loading done.
        val countText = if (count < 0) "-" else "$count"
        Text(
            countText,
            Modifier.layoutId(LayoutId.Text)
        )
    }

    Layout(
        content,
        Modifier
            .clip(shapes.small)
            .padding(padding)
            .withNotNull(onClick) {
                clickable(onClick = it, onClickLabel = onClickLabel)
            }
            .clearAndSetSemantics {
                this.contentDescription = contentDescription
            }
        ,
    ) { measurables, constraints ->
        val iconTextSpace = 4.dp.roundToPx() // Space between icon and text
        val avoidOffset =
            ((iconTextSpace * 5F).triangle(expandProgress)).roundToInt() // Extra offset to avoid icon/text collision during animation
        val verticalSpace = expandProgress.lerpBetween(0, iconTextSpace) + avoidOffset
        val horizontalSpace = expandProgress.lerpBetween(iconTextSpace, 0) + avoidOffset

        val iconPlaceable = measurables.find { it.layoutId == LayoutId.Icon }!!.measure(constraints)
        val textPlaceable = measurables.find { it.layoutId == LayoutId.Text }!!.measure(constraints)

        val textStart = expandProgress.lerpBetween(iconPlaceable.width + horizontalSpace, 0)
        val textTop = expandProgress.lerpBetween(0, iconPlaceable.height + verticalSpace)

        val width = maxOf(iconPlaceable.width, textStart + textPlaceable.width)
        val height = maxOf(iconPlaceable.height, textTop + textPlaceable.height)

        layout(width, height) {
            iconPlaceable.placeRelative(
                expandProgress.lerpBetween(0, (width - iconPlaceable.width) / 2),
                expandProgress.lerpBetween((height - iconPlaceable.height) / 2, 0)
            )
            textPlaceable.placeRelative(
                expandProgress.lerpBetween(textStart + horizontalSpace,
                    (width - textPlaceable.width) / 2),
                expandProgress.lerpBetween((height - textPlaceable.height) / 2, textTop + verticalSpace)
            )
        }
    }
}

private class IconStyle(val size: Dp, val padding: Padding)

private val SmallIconStyle = IconStyle(Size.IconSmall, Padding.IconSmall)
private val LargeIconStyle = IconStyle(Size.IconLarge, Padding.IconLarge,)

private fun Float.lerpBetween(start: IconStyle, end: IconStyle): IconStyle =
    IconStyle(
        lerpBetween(start.size, end.size),
        lerpBetween(start.padding, end.padding)
    )

private enum class LayoutId {
    Icon,
    Text,
    ;
}
