package org.beatonma.commons.app.ui.screens.social

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.accessibility.contentDescription
import org.beatonma.commons.compose.ambient.WithContentAlpha
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.AnimatedItemVisibility
import org.beatonma.commons.compose.components.text.DateTime
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.util.withAnnotatedStyle
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.padding.EndOfContent
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding

@Composable
internal fun CommentList(
    comments: List<SocialComment>,
    modifier: Modifier,
    expandProgress: Float,
    onClick: (SocialComment) -> Unit,
) {
    if (comments.isEmpty()) {
        NoComments(modifier)
    } else {
        if (expandProgress < 0.7F) return

        LazyColumn(Modifier.testTag(SocialTestTag.CommentsList)) {
            itemsIndexed(comments) { i, comment ->
                animation.AnimatedItemVisibility(position = i, horizontal = false) {
                    Comment(
                        comment,
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
    onClick: (SocialComment) -> Unit,
) {
    val contentDescription = comment.contentDescription

    Column(
        Modifier
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentDescription
            }
            .clickable { onClick(comment) }
            .fillMaxWidth()
            .padding(Padding.VerticalListItem + Padding.ScreenHorizontal)
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
                    modifier = Modifier.padding(Padding.HorizontalListItem)
                )
                DateTime(comment.created)
            }
        }
    }
}
