package org.beatonma.commons.app.social.compose

import androidx.compose.animation.AnimatedFloatModel
import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.AnimationClockObservable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TransitionState
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.contentColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.compose.modifiers.*
import org.beatonma.commons.compose.util.lerp
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.theme.compose.IconDimen
import org.beatonma.commons.theme.compose.Whitespace
import org.beatonma.commons.theme.compose.horizontalTextListPadding
import org.beatonma.commons.theme.compose.theme.CommonsTween

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

internal var SocialAmbient = ambientOf { EmptySocialContent }

//@Composable
//fun SocialContentView(
//    socialContent: SocialContent,
//    modifier: Modifier = Modifier,
//    onVoteUpClick: ActionBlock,
//    onVoteDownClick: ActionBlock,
//    onExpandedCommentIconClick: ActionBlock,
//    onCommentClick: (SocialComment) -> Unit,
//) {
//    Providers(SocialAmbient provides socialContent) {
//        SocialContentView(modifier,
//            onVoteUpClick,
//            onVoteDownClick,
//            onExpandedCommentIconClick,
//            onCommentClick)
//    }
//}

@Composable
private fun Modifier.showWhenExpanded(state: State, progress: Float, animationSpec: AnimationSpec<IntSize>): Modifier =
    switchEqual(state,
        State.EXPANDED to { wrapContentHeight() },
        State.COLLAPSED to { height(0.dp) }
    )
        .drawOpacity(progress)
        .colorize()


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
    tint: Color = colors.onSurface,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    animationSpec: AnimationSpec<IntSize> = CommonsTween(),
    transitionState: TransitionState,
    clock: AnimationClockObservable = AnimationClockAmbient.current.asDisposableClock(),
) {
    val progress = transitionState[progressKey]

    SocialContentView(
        modifier,
        onVoteUpClick, onVoteDownClick, onCommentIconClick, onCommentClick, tint,
        state, animationSpec, progress, clock
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
    tint: Color = colors.onSurface,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    animationSpec: AnimationSpec<IntSize> = remember { CommonsTween() },
    transitionProgress: Float,
    clock: AnimationClockObservable = AnimationClockAmbient.current.asDisposableClock(),
) {
    val expandAction = { state.update(State.EXPANDED) }

    val socialContent = SocialAmbient.current
    val opacity = transitionProgress.progressIn(0.8F, 1F)

    Column(modifier) {
        Text(
            socialContent.title,
            style = typography.h4,
            modifier = Modifier
                .showWhenExpanded(state.value, opacity, animationSpec)
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
                )
                .colorize(),
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
                .showWhenExpanded(state.value, opacity, animationSpec)
                ,
            onClick = onCommentClick,
        )
    }
}


@Composable
fun SocialContentView(
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
) {
    when (state.value) {
        State.COLLAPSED -> CompactSocial(modifier) { state.update(State.EXPANDED) }
        State.EXPANDED -> ExpandedSocial(
            modifier,
            onVoteUpClick = onVoteUpClick,
            onVoteDownClick = onVoteDownClick,
            onCommentIconClick = onCommentIconClick,
            onCommentClick = onCommentClick,
        )
        else -> Unit
    }
}

@Composable
fun CompactSocial(
    modifier: Modifier = Modifier,
    tint: Color = colors.onSurface,
    onClick: ActionBlock,
) {
    SocialIcons(
        modifier = modifier.clickable(onClick = onClick),
        iconStyle = SmallIconStyle,
        tint = tint,
        arrangement = Arrangement.Start,
        onCommentIconClick = onClick,
        onVoteUpIconClick = onClick,
        onVoteDownIconClick = onClick
    )
}

@Composable
fun ExpandedSocial(
    modifier: Modifier = Modifier,
    socialContent: SocialContent = SocialAmbient.current,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
) {
    ExpandedSocial(
        socialContent.title,
        socialContent.comments,
        modifier = modifier,
        onVoteUpClick = onVoteUpClick,
        onVoteDownClick = onVoteDownClick,
        onCommentIconClick = onCommentIconClick,
        onCommentClick = onCommentClick,
    )
}

@Composable
fun ExpandedSocial(
    targetName: String,
    comments: List<SocialComment>,
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
) {
    Column(modifier) {
        Text(targetName,
            style = typography.h4,
            modifier = Modifier.align(Alignment.CenterHorizontally))

        SocialIcons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            iconStyle = LargeIconStyle,
            arrangement = Arrangement.SpaceEvenly,
            onCommentIconClick = onCommentIconClick,
            onVoteUpIconClick = onVoteUpClick,
            onVoteDownIconClick = onVoteDownClick,
        )

        CommentList(comments, Modifier.fillMaxWidth(), onClick = onCommentClick)
    }
}

@OptIn(InternalLayoutApi::class)
@Composable
private fun SocialIcons(
    socialContent: SocialContent = SocialAmbient.current,
    iconStyle: IconStyle,
    modifier: Modifier = Modifier,
    tint: Color = contentColor(),
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
            colors.onSurface.lerp(MaterialTheme.colors.secondary, voteSelection[0].value)
        val downvoteTint =
            colors.onSurface.lerp(MaterialTheme.colors.secondary, voteSelection[1].value)

        CounterIcon(
            Modifier,
            socialContent.commentCount,
            Icons.Default.Comment,
            tint,
            iconStyle,
            onCommentIconClick)

        CounterIcon(
            Modifier,
            socialContent.ayeVotes,
            Icons.Default.ThumbUp,
            upvoteTint,
            iconStyle,
            onVoteUpIconClick)

        CounterIcon(
            Modifier,
            socialContent.noVotes,
            Icons.Default.ThumbDown,
            downvoteTint,
            iconStyle,
            onVoteDownIconClick)
    }
}

@OptIn(InternalLayoutApi::class)
@Composable
private fun SocialIcons(
    socialContent: SocialContent = SocialAmbient.current,
    modifier: Modifier = Modifier,
    tint: Color = contentColor(),
    arrangement: Arrangement.Horizontal,
    iconModifier: Modifier = Modifier,
    iconSize: Dp,
    iconPadding: Dp,
    onCommentIconClick: ActionBlock,
    onVoteUpIconClick: ActionBlock,
    onVoteDownIconClick: ActionBlock,
) {
    val clock = AnimationClockAmbient.current
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
            colors.onSurface.lerp(MaterialTheme.colors.secondary, voteSelection[0].value)
        val downvoteTint =
            colors.onSurface.lerp(MaterialTheme.colors.secondary, voteSelection[1].value)

        CounterIcon(
            iconModifier,
            iconSize,
            iconPadding,
            socialContent.commentCount,
            Icons.Default.Comment,
            tint,
            onCommentIconClick)

        CounterIcon(
            iconModifier,
            iconSize,
            iconPadding,
            socialContent.ayeVotes,
            Icons.Default.ThumbUp,
            upvoteTint,
            onVoteUpIconClick)

        CounterIcon(
            iconModifier,
            iconSize,
            iconPadding,
            socialContent.noVotes,
            Icons.Default.ThumbDown,
            downvoteTint,
            onVoteDownIconClick)
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
//    Row(
//        Modifier.clickable(onClick = onClick).then(style.modifier),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Icon(icon,
//            tint = tint,
//            modifier = Modifier.size(style.size).padding(end = Whitespace.Icon.Small.around))
//        Text("$count")
//    }
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
    LazyColumnFor(comments, modifier) { comment ->
        Comment(
            comment,
            itemModifier,
            onClick,
        )
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
            .padding(Whitespace.List.Vertical.between)
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
