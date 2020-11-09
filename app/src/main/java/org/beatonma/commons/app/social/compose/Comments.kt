package org.beatonma.commons.app.social.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.R
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.pdp
import org.beatonma.commons.theme.compose.plus
import org.beatonma.commons.theme.compose.theme.modalScrim
import org.beatonma.commons.theme.compose.theme.systemui.imePadding
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding

@Composable
internal fun CommentList(
    comments: List<SocialComment>,
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    onClick: (SocialComment) -> Unit,
) {
    if (comments.isEmpty()) {
        NoComments(modifier)
    }
    else {
        val progress = AmbientCollapseExpandProgress.current.progressIn(0.75F, 1F).withEasing(
            FastOutSlowInEasing)

        LazyColumnForIndexed(comments, modifier) { i, comment ->
            Comment(
                comment,
                itemModifier.padding(bottom = (4F * i).lerp(0F, progress).pdp),
                onClick,
            )
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateCommentUi(modifier: Modifier = Modifier) {
    val progress = AmbientSocialUiTransition.current[ExpandedComposeCommentProgress]
    val uiState = AmbientSocialUiState.current
    val createCommentText = mutableStateOf("")

    Box(Modifier.fillMaxSize()) {
        if (progress == 0F) {
            CreateCommentButton(
                Modifier.zIndex(Layer.Top)
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
            )
        }
        else {
            // Scrim
            Box(
                Modifier.fillMaxSize()
                    .zIndex(Layer.ModalScrim)
                    .background(colors.modalScrim)
                    .clickable(onClick = { uiState.update(SocialUiState.Expanded) })
                    .drawOpacity(progress)
            ) {

                Surface(
                    Modifier.zIndex(Layer.ModalSurface)
                        .align(Alignment.BottomCenter),
                    color = colors.surface
                ) {
                    Column {
                        TextField(
                            createCommentText.value,
                            onValueChange = { createCommentText.update(it) },
                            modifier = Modifier.fillMaxWidth()
                                .padding(Padding.Screen)
                                .imePadding()
                                .navigationBarsPadding(),

                            )

                        OutlinedButton(onClick = AmbientSocialActions.current.onCommentSubmitClick) {
                            Text(stringResource(R.string.social_comment_post))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateCommentButton(
    modifier: Modifier = Modifier,
    onClick: ActionBlock = AmbientSocialActions.current.onCreateCommentClick,
) {
    ExtendedFloatingActionButton(
        modifier = modifier.padding(Padding.Fab)
            .zIndex(Layer.ModalSurface),
        text = {
            Text(stringResource(id = R.string.social_new_comment))
        },
        onClick = onClick
    )
}
