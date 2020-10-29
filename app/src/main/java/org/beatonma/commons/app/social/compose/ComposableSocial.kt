package org.beatonma.commons.app.social.compose

import androidx.compose.animation.AnimatedFloatModel
import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.TransitionState
import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.InternalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.R
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.compose.animation.progress
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.switchEqual
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.lerp
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.theme.compose.IconDimen
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.Whitespace
import org.beatonma.commons.theme.compose.horizontalTextListPadding

private fun Dp.lerp(other: Dp, progress: Float) = Dp(value.lerp(other.value, progress))
private interface IconStyle {
    val size: Dp
    val padding: Dp
}

private object SmallIconStyle : IconStyle {
    override val size: Dp = IconDimen.Small
    override val padding: Dp = Whitespace.Icon.Small.around
}

private object LargeIconStyle : IconStyle {
    override val size: Dp = IconDimen.Large
    override val padding: Dp = Whitespace.Icon.Large.around
}

private fun iconStyle(size: Dp, padding: Dp) = object : IconStyle {
    override val size: Dp = size
    override val padding: Dp = padding
}

private fun IconStyle.lerp(other: IconStyle, progress: Float) =
    iconStyle(
        size = size.lerp(other.size, progress),
        padding = padding.lerp(other.padding, progress)
    )

/**
 * Fully animated transition from collapsed to expanded states
 */
@Composable
fun SocialContentView(
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock = SocialActionsAmbient.current.onVoteUpClick,
    onVoteDownClick: ActionBlock = SocialActionsAmbient.current.onVoteDownClick,
    onCommentIconClick: ActionBlock = SocialActionsAmbient.current.onExpandedCommentIconClick,
    onCommentClick: (SocialComment) -> Unit = SocialActionsAmbient.current.onCommentClick,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    transitionState: TransitionState,
    tint: Color = AmbientContentColor.current,
) {
    val progress = transitionState.progress

    SocialContentView(
        modifier,
        onVoteUpClick, onVoteDownClick, onCommentIconClick, onCommentClick, tint,
        state, progress
    )
}

/**
 * Fully animated transition from collapsed to expanded states
 */
@Composable
fun SocialContentView(
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
    tint: Color = AmbientContentColor.current,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    transitionProgress: Float,
) {
    val expandAction = { state.update(State.EXPANDED) }

    val socialContent = SocialAmbient.current
    val opacity = transitionProgress.progressIn(0.8F, 1F)

    Column(modifier) {
        Text(
            socialContent.title,
            style = typography.h4,
            modifier = Modifier
                .showWhenExpanded(state.value, opacity)
                .align(Alignment.CenterHorizontally)
        )

        SocialIcons(
            modifier = Modifier
                .onlyWhen(transitionProgress == 0F) {
                    clickable(onClick = expandAction)
                }
                .padding(
                    horizontal = 0.dp.lerp(32.dp, transitionProgress),
                    vertical = 0.dp.lerp(16.dp, transitionProgress)
                ),
            iconStyle = SmallIconStyle.lerp(LargeIconStyle, transitionProgress),
            arrangement = Arrangement.SpaceEvenly,
            tint = tint,
            onCommentIconClick = if (transitionProgress == 0F) expandAction else onCommentIconClick,
            onVoteUpIconClick = if (transitionProgress == 0F) expandAction else onVoteUpClick,
            onVoteDownIconClick = if (transitionProgress == 0F) expandAction else onVoteDownClick,
        )

        CommentList(
            socialContent.comments,
            Modifier
                .showWhenExpanded(state.value, opacity),
            onClick = onCommentClick,
        )
    }
}

@OptIn(InternalLayoutApi::class)
@Composable
private fun SocialIcons(
    socialContent: SocialContent = SocialAmbient.current,
    iconStyle: IconStyle,
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
            tint.lerp(MaterialTheme.colors.secondary, voteSelection[0].value)
        val downvoteTint =
            tint.lerp(MaterialTheme.colors.secondary, voteSelection[1].value)

        CounterIcon(
            Modifier,
            socialContent.commentCount,
            Icons.Default.Comment,
            tint,
            iconStyle,
            onCommentIconClick
        )

        CounterIcon(
            Modifier,
            socialContent.ayeVotes,
            Icons.Default.ThumbUp,
            upvoteTint,
            iconStyle,
            onVoteUpIconClick
        )

        CounterIcon(
            Modifier,
            socialContent.noVotes,
            Icons.Default.ThumbDown,
            downvoteTint,
            iconStyle,
            onVoteDownIconClick
        )
    }
}

@Composable
private fun CounterIcon(
    modifier: Modifier = Modifier,
    count: Int,
    icon: VectorAsset,
    tint: Color,
    style: IconStyle,
    onClick: ActionBlock,
) {
    CounterIcon(
        modifier,
        style.size,
        style.padding,
        count, icon, tint, onClick
    )
}

@Composable
private fun CounterIcon(
    modifier: Modifier = Modifier,
    size: Dp,
    padding: Dp,
    count: Int,
    icon: VectorAsset,
    tint: Color,
    onClick: ActionBlock,
) {
    Row(
        Modifier
            .clickable(onClick = onClick)
            .then(modifier)
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon,
            tint = tint,
            modifier = Modifier.size(size).padding(end = Whitespace.Icon.Small.around))
        Text("$count")
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
        Column(modifier) {
            comments.forEach { comment ->
                Comment(
                    comment,
                    itemModifier,
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
            .padding(Padding.VerticalListItem)
    ) {
        withEmphasisHigh {
            Text(comment.text)
        }

        withEmphasisMedium {
            Row {
                Text(comment.username,
                    style = typography.caption,
                    color = colors.secondary,
                    modifier = Modifier.horizontalTextListPadding()
                )
                Text(comment.created.formatted(), style = typography.caption)
            }
        }
    }
}

@Composable
private fun Modifier.showWhenExpanded(
    state: State,
    progress: Float,
): Modifier =
    switchEqual(state,
        State.EXPANDED to { wrapContentHeight(progress) },
        State.COLLAPSED to { height(0.dp) }
    )
        .drawOpacity(progress)
