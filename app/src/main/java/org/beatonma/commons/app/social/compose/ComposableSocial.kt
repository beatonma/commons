package org.beatonma.commons.app.social.compose

import androidx.compose.animation.AnimatedFloatModel
import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TransitionState
import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.InternalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.layout.id
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.R
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.compose.animation.progress
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillWidth
import org.beatonma.commons.compose.util.ComposableBlock
import org.beatonma.commons.compose.util.lerp
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.triangle
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.pdp
import org.beatonma.commons.theme.compose.plus
import org.beatonma.commons.theme.compose.theme.positive
import kotlin.math.roundToInt

private val AmbientProgress = ambientOf { 0F }
private val AmbientIconStyle: ProvidableAmbient<IconStyle> = ambientOf { SmallIconStyle }

/**
 * Fully animated transition from collapsed to expanded states
 */
@Composable
fun SocialContentView(
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock = AmbientSocialActions.current.onVoteUpClick,
    onVoteDownClick: ActionBlock = AmbientSocialActions.current.onVoteDownClick,
    onCommentIconClick: ActionBlock = AmbientSocialActions.current.onExpandedCommentIconClick,
    onCommentClick: (SocialComment) -> Unit = AmbientSocialActions.current.onCommentClick,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    transitionState: TransitionState,
    expandAction: ActionBlock = { state.update(State.EXPANDED) },
) {
    val progress = transitionState.progress

    val theme = AmbientSocialTheme.current
    val foregroundColor = theme.collapsedOnBackground.lerp(
        theme.expandedOnBackground,
        progress.progressIn(0.5F, 0.8F)
    )

    Providers(
        AmbientContentColor provides foregroundColor,
        AmbientIconStyle provides SmallIconStyle.lerp(LargeIconStyle, progress),
        AmbientProgress provides progress,
    ) {
        SocialContentView(
            AmbientSocialContent.current,
            modifier,
            onVoteUpClick, onVoteDownClick, onCommentIconClick, onCommentClick,
            state, tint = foregroundColor, expandAction = expandAction
        )
    }
}

/**
 * Fully animated transition from collapsed to expanded states
 */
@Composable
private fun SocialContentView(
    socialContent: SocialContent,
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    tint: Color = AmbientContentColor.current,
    progress: Float = AmbientProgress.current,
    expandAction: ActionBlock = { state.update(State.EXPANDED) },
) {
    val expandedContentVisibility = progress.progressIn(0.8F, 1F).withEasing(
        if (state.value == State.EXPANDED) LinearOutSlowInEasing else FastOutLinearInEasing
    )

    Column(modifier) {
        if (expandedContentVisibility != 0F) {
            Text(
                socialContent.title,
                style = typography.h4,
                modifier = Modifier
                    .wrapContentHeight(expandedContentVisibility)
                    .drawOpacity(expandedContentVisibility)
                    .align(Alignment.CenterHorizontally)
            )
        }

        SocialIcons(
            modifier = Modifier
                .onlyWhen(progress == 0F) {
                    clickable(onClick = expandAction)
                }
                .padding(
                    horizontal = 0F.lerp(32F, progress).pdp,
                    vertical = 0F.lerp(16F, progress).pdp
                )
                .wrapContentOrFillWidth(progress),
            arrangement = Arrangement.SpaceEvenly,
            tint = tint,
            onCommentIconClick = if (progress == 0F) expandAction else onCommentIconClick,
            onVoteUpIconClick = if (progress == 0F) expandAction else onVoteUpClick,
            onVoteDownIconClick = if (progress == 0F) expandAction else onVoteDownClick,
        )

        if (expandedContentVisibility != 0F) {
            CommentList(
                socialContent.comments,
                Modifier
                    .wrapContentHeight(progress.progressIn(0.6F, 0.8F))
                    .drawOpacity(expandedContentVisibility),
                onClick = onCommentClick,
            )
        }
    }
}

@OptIn(InternalLayoutApi::class)
@Composable
private fun SocialIcons(
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
    val progress = AmbientProgress.current

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

@Composable
private fun CommentList(
    comments: List<SocialComment>,
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    onClick: (SocialComment) -> Unit,
) {
    if (comments.isEmpty()) {
        NoComments(modifier)
    }
    else {
        // TODO laziness removed due to upstream vertical scrolling but this should definitely be lazy.
//    LazyColumnFor(comments, modifier) { comment ->
        val progress = AmbientProgress.current.progressIn(0.75F, 1F).withEasing(FastOutSlowInEasing)
        Column(modifier) {
            comments.fastForEachIndexed { i, comment ->
                Comment(
                    comment,
                    itemModifier.padding(bottom = (4F * i).lerp(0F, progress).pdp),
                    onClick,
                )
            }
        }
    }
}

@Composable
private fun NoComments(modifier: Modifier = Modifier) {
    withEmphasisHigh {
        Text(
            stringResource(R.string.social_comment_no_comments),
            modifier.fillMaxWidth(),
            style = typography.h6, textAlign = TextAlign.Center)
    }
}

@Composable
private fun Comment(
    comment: SocialComment,
    modifier: Modifier = Modifier,
    onClick: (SocialComment) -> Unit,
) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable { onClick(comment) }
            .padding(Padding.VerticalListItem + Padding.ScreenHorizontal)
    ) {
        withEmphasisHigh {
            Text(comment.text)
        }

        withEmphasisMedium {
            Row {
                Text(comment.username,
                    style = typography.caption,
                    color = colors.secondary,
                    modifier = Modifier.padding(Padding.HorizontalListItem)
                )
                Text(comment.created.formatted(), style = typography.caption)
            }
        }
    }
}

private interface IconStyle {
    val size: Dp
    val padding: PaddingValues
}

private object SmallIconStyle : IconStyle {
    override val size: Dp = Size.IconSmall
    override val padding = Padding.IconSmall
}

private object LargeIconStyle : IconStyle {
    override val size: Dp = Size.IconLarge
    override val padding = Padding.IconLarge
}

private fun IconStyle.lerp(other: IconStyle, progress: Float): IconStyle =
    object : IconStyle {
        override val size: Dp = this@lerp.size.lerp(other.size, progress)
        override val padding = this@lerp.padding.lerp(other.padding, progress)
    }
