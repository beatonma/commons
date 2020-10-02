package org.beatonma.commons.app.social

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.contentColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
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

private val state: State = State.COLLAPSED
private val SocialAmbient = ambientOf<SocialContent> { error("No social content registered") }

@Composable
fun SocialContentView(
    socialContent: SocialContent,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
) {
    provideSocialContent(socialContent) {
        when (state) {
            State.COLLAPSED -> CompactSocial { }
            State.EXPANDED -> ExpandedSocial(
                onVoteUpClick = {},
                onVoteDownClick = {},
                onCommentClick = {}
            )
            else -> Unit
        }
    }
}

@Composable
fun CompactSocial(
    socialContent: SocialContent = SocialAmbient.current,
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
    socialContent: SocialContent = SocialAmbient.current,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
) {
    ExpandedSocial(
        socialContent.title,
        socialContent.comments,
        onVoteUpClick = onVoteUpClick,
        onVoteDownClick = onVoteDownClick,
        onCommentClick = onCommentClick
    )
}

@Composable
fun ExpandedSocial(
    targetName: String,
    comments: List<SocialComment>,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
) {
    Column {
        Text(targetName,
            style = typography.h4,
            modifier = Modifier.align(Alignment.CenterHorizontally))

        SocialIcons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            iconStyle = LargeIconStyle,
            arrangement = Arrangement.SpaceEvenly,
            onCommentIconClick = {},
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
    SocialIcons(
        socialContent.commentCount,
        socialContent.ayeVotes,
        socialContent.noVotes,
        iconStyle, modifier, tint, arrangement,
        onCommentIconClick, onVoteUpIconClick, onVoteDownIconClick
    )
}

@OptIn(InternalLayoutApi::class)
@Composable
private fun SocialIcons(
    commentCount: Int,
    upvoteCount: Int,
    downvoteCount: Int,
    iconStyle: IconStyle,
    modifier: Modifier = Modifier,
    tint: Color = contentColor(),
    arrangement: Arrangement.Horizontal,
    onCommentIconClick: ActionBlock,
    onVoteUpIconClick: ActionBlock,
    onVoteDownIconClick: ActionBlock,
) {
    Row(
        modifier,
        horizontalArrangement = arrangement,
    ) {
        CounterIcon(commentCount, Icons.Default.Comment, tint, iconStyle, onCommentIconClick)
        CounterIcon(upvoteCount, Icons.Default.ThumbUp, tint, iconStyle, onVoteUpIconClick)
        CounterIcon(downvoteCount, Icons.Default.ThumbDown, tint, iconStyle, onVoteDownIconClick)
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

@Composable
internal fun provideSocialContent(socialContent: SocialContent, content: @Composable () -> Unit) =
    Providers(SocialAmbient provides socialContent, children = content)
