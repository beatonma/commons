package org.beatonma.commons.app.social.compose

import androidx.compose.animation.AnimatedFloatModel
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
import androidx.compose.ui.WithConstraints
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.compose.util.lerp
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.theme.compose.IconDimen
import org.beatonma.commons.theme.compose.Whitespace
import org.beatonma.commons.theme.compose.horizontalTextListPadding

private interface IconStyle {
    val size: Dp
    val modifier: Modifier
}

private object SmallIconStyle : IconStyle {
    override val size: Dp = IconDimen.Small
    override val modifier: Modifier = Modifier.padding(Whitespace.Icon.Small.around)
}

private object LargeIconStyle : IconStyle {
    override val size: Dp = IconDimen.Large
    override val modifier: Modifier = Modifier.padding(Whitespace.Icon.Large.around)
}

internal var SocialAmbient = ambientOf<SocialContent> { error("No social content registered") }

@Composable
fun SocialContentView(
    socialContent: SocialContent,
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onExpandedCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
) {
    Providers(SocialAmbient provides socialContent) {
        SocialContentView(modifier,
            onVoteUpClick,
            onVoteDownClick,
            onExpandedCommentIconClick,
            onCommentClick)
    }
}

/**
 * Choose state depending on given constraints
 */
@Composable
fun AdaptiveSocialContent(
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onExpandedCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
    state: MutableState<State>,
//    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
) {
//    val transitionDef = remember {
//        transitionDefinition<State> {
//            state(State.COLLAPSED) {
//                this[progressKey] = 0F
//            }
//            state(State.EXPANDED) {
//                this[progressKey] = 1F
//            }
//            transition(State.COLLAPSED to State.EXPANDED) {
//                progressKey using tween(800)
//            }
//        }
//    }
//    val transitionState = transition(definition = transitionDef, toState = state.value)

    WithConstraints {
        val h = with(DensityAmbient.current) { constraints.maxHeight.toDp() }

        if (h < 60.dp) {
            CompactSocial(modifier) { state.component2()(State.EXPANDED) }
        }
        else {
            ExpandedSocial(
                modifier,
                onVoteUpClick = onVoteUpClick,
                onVoteDownClick = onVoteDownClick,
                onCommentIconClick = onExpandedCommentIconClick,
                onCommentClick = onCommentClick,
            )
        }
    }
//    }
}

@Composable
fun SocialContentView(
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onExpandedCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
    tint: Color = colors.onSurface,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    transitionState: TransitionState,
) {
    val progress = transitionState[progressKey]
    val expandAction = { state.update(State.EXPANDED) }
    when (progress) {
        0F -> {
            SocialIcons(
                modifier = modifier.clickable(onClick = expandAction),
                iconStyle = SmallIconStyle,
                tint = tint,
                arrangement = Arrangement.Start,
                onCommentIconClick = expandAction,
                onVoteUpIconClick = expandAction,
                onVoteDownIconClick = expandAction
            )
        }
    }
//    when (state.value) {
//        State.COLLAPSED -> CompactSocial(modifier) { state.component2()(State.EXPANDED) }
//        State.EXPANDED -> ExpandedSocial(
//            modifier,
//            onVoteUpClick = onVoteUpClick,
//            onVoteDownClick = onVoteDownClick,
//            onCommentIconClick = onExpandedCommentIconClick,
//            onCommentClick = onCommentClick,
//        )
//        else -> Unit
//    }
}

@Composable
fun SocialContentView(
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onExpandedCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
) {
    when (state.value) {
        State.COLLAPSED -> CompactSocial(modifier) { state.component2()(State.EXPANDED) }
        State.EXPANDED -> ExpandedSocial(
            modifier,
            onVoteUpClick = onVoteUpClick,
            onVoteDownClick = onVoteDownClick,
            onCommentIconClick = onExpandedCommentIconClick,
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

        CounterIcon(socialContent.commentCount,
            Icons.Default.Comment,
            tint,
            iconStyle,
            onCommentIconClick)
        CounterIcon(socialContent.ayeVotes,
            Icons.Default.ThumbUp,
            upvoteTint,
            iconStyle,
            onVoteUpIconClick)
        CounterIcon(socialContent.noVotes,
            Icons.Default.ThumbDown,
            downvoteTint,
            iconStyle,
            onVoteDownIconClick)
    }
}

@Composable
private fun CounterIcon(
    count: Int,
    icon: VectorAsset,
    tint: Color,
    style: IconStyle,
    onClick: ActionBlock,
) {
    Row(
        Modifier.clickable(onClick = onClick).then(style.modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon,
            tint = tint,
            modifier = Modifier.size(style.size).padding(end = Whitespace.Icon.Small.around))
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
