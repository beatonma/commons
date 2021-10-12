package org.beatonma.commons.app.ui.screens.social

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.accessibility.contentDescription
import org.beatonma.commons.app.ui.components.DateTime
import org.beatonma.commons.compose.ambient.WithContentAlpha
import org.beatonma.commons.compose.animation.AnimatedItemVisibility
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.padding.EndOfContent
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.util.withAnnotatedStyle
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.CommonsPadding
import org.beatonma.commons.themed.themedAnimation

@Composable
internal fun CommentList(
    comments: List<SocialComment>,
    modifier: Modifier,
    onClick: (SocialComment) -> Unit,
) {
    if (comments.isEmpty()) {
        NoComments(modifier)
    } else {
        LazyColumn(Modifier.testTag(SocialTestTag.CommentsList)) {
            itemsIndexed(comments) { i, comment ->
                themedAnimation.AnimatedItemVisibility(position = i, horizontal = false) {
                    Comment(
                        comment,
                        modifier,
                        onClick,
                    )
                }
            }

            item {
                EndOfContent()
            }
        }
    }
}

internal fun LazyListScope.CommentList(
    comments: List<SocialComment>,
    modifier: Modifier,
    onClick: (SocialComment) -> Unit,
) {
    if (comments.isEmpty()) {
        item {
            NoComments(modifier)
        }
    } else {
        itemsIndexed(comments) { i, comment ->
            themedAnimation.AnimatedItemVisibility(position = i, horizontal = false) {
                Comment(
                    comment,
                    modifier,
                    onClick,
                )
            }
        }
    }
}

@Composable
private fun NoComments(modifier: Modifier) {
    ResourceText(
        R.string.social_comment_no_comments,
        modifier = modifier
            .fillMaxWidth()
            .testTag(SocialTestTag.CommentsEmpty),
        style = typography.h6,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun Comment(
    comment: SocialComment,
    modifier: Modifier,
    onClick: (SocialComment) -> Unit,
) {
    val contentDescription = comment.contentDescription

    Column(
        modifier
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentDescription
            }
            .clickable { onClick(comment) }
            .fillMaxWidth()
            .padding(CommonsPadding.VerticalListItem + CommonsPadding.ScreenHorizontal)
            .testTag(SocialTestTag.Comment)
    ) {
        WithContentAlpha(ContentAlpha.high) {
            Text(comment.text.withAnnotatedStyle())
        }

        WithContentAlpha(ContentAlpha.medium) {
            Row {
                Username(
                    comment.username,
                    style = typography.caption,
                    modifier = Modifier.padding(CommonsPadding.HorizontalListItem)
                )
                DateTime(comment.created)
            }
        }
    }
}
