package org.beatonma.commons.app.social.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.signin.compose.AmbientUserToken
import org.beatonma.commons.app.signin.compose.NullUserToken
import org.beatonma.commons.app.signin.compose.SignInUi
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.ui.compose.components.FabBottomSheet
import org.beatonma.commons.app.ui.compose.components.FabBottomSheetState
import org.beatonma.commons.app.ui.compose.components.FabText
import org.beatonma.commons.app.ui.compose.components.OutlineButton
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.Hint
import org.beatonma.commons.compose.components.LengthTextFieldValidator
import org.beatonma.commons.compose.components.ValidatedLengthTextField
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.compose.util.withAnnotatedStyle
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.endOfContent
import org.beatonma.commons.theme.compose.pdp
import org.beatonma.commons.theme.compose.plus
import org.beatonma.commons.theme.compose.theme.systemui.imeOrNavigationBarsPadding

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
        if (AmbientCollapseExpandProgress.current < 0.7F) {
            return
        }

        val progress = AmbientCollapseExpandProgress.current
            .progressIn(0.75F, 1F)
            .withEasing(FastOutSlowInEasing)

        LazyColumnForIndexed(comments, modifier) { i, comment ->
            Comment(
                comment,
                itemModifier.padding(bottom = (4F * i).lerp(0F, progress).pdp),
                onClick,
            )

            if (i == comments.size - 1) {
                Spacer(Modifier.endOfContent())
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
            style = typography.h6, textAlign = TextAlign.Center
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
            .padding(Padding.VerticalListItem + Padding.ScreenHorizontal)
    ) {
        withEmphasisHigh {
            Text(comment.text.withAnnotatedStyle())
        }

        withEmphasisMedium {
            Row {
                Username(
                    comment.username,
                    style = typography.caption,
                    modifier = Modifier.padding(Padding.HorizontalListItem)
                )
                Text(comment.created.formatted(), style = typography.caption)
            }
        }
    }
}

/**
 * Displays a FAB which expands into a bottom sheet dialog for comment authoring.
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalFocus::class)
@Composable
fun CreateCommentUi(
    userToken: UserToken = AmbientUserToken.current,
    uiState: MutableState<SocialUiState> = AmbientSocialUiState.current,
) {
    if (userToken == NullUserToken) {
        // User can't create a new comment until they are signed in.
        SignInUi()
        return
    }

    val focusRequester = remember { FocusRequester() }

    val fabBottomSheetState = remember { mutableStateOf(FabBottomSheetState.Fab) }.apply {
        value = when (uiState.value) {
            SocialUiState.ComposeComment -> FabBottomSheetState.BottomSheet
            else -> FabBottomSheetState.Fab
        }
    }

    FabBottomSheet(
        fabBottomSheetState,
        fabContent = { progress ->
            CreateCommentButtonContent(progress)
        },
        bottomSheetContent = { progress ->
            CreateCommentSheetContent(progress, focusRequester)
        }
    )
}

@Composable
private fun CreateCommentButtonContent(
    progress: Float,
) {
    FabText(stringResource(R.string.social_new_comment), progress)
}

@OptIn(ExperimentalFocus::class)
@Composable
private fun CreateCommentSheetContent(
    progress: Float,
    focusRequester: FocusRequester,
    commentText: MutableState<String> = remember { mutableStateOf("") },
    commentValidator: LengthTextFieldValidator = AmbientSocialCommentValidator.current,
) {
    if (progress == 0F) {
        return
    }

    val user = AmbientUserToken.current
    val commentIsValid = remember { mutableStateOf(false) }

    if (progress == 1F) {
        // Focus on text field and bring up the IME.
        focusRequester.requestFocus()
    }

    CardText(
        Modifier
            .wrapContentSize(
                horizontalProgress = progress.progressIn(0F, 0.4F),
                verticalProgress = progress.progressIn(0F, 0.8F)
            )
            .drawOpacity(progress.progressIn(0.8F, 1F))
            .imeOrNavigationBarsPadding(scale = progress)
    ) {

        Column {
            Row(
                Modifier.fillMaxWidth()
                    .padding(Padding.VerticalListItem),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.social_compose_comment),
                    style = typography.h6
                )

                Username(user.username)
            }

            ValidatedLengthTextField(
                value = commentText.value,
                validator = commentValidator,
                placeholder = {
                    Hint(stringResource(R.string.social_compose_comment_hint))
                },
                onValueChange = { value, isValid ->
                    commentText.update(value)
                    commentIsValid.update(isValid)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(64.dp, 220.dp)
                    .animateContentSize()
                    .focusRequester(focusRequester)
            )

            val socialActions = AmbientSocialActions.current
            OutlineButton(
                onClick = { socialActions.onCommentSubmitClick(commentText.value) },
                modifier = Modifier.align(Alignment.End).padding(Padding.CardButton),
                enabled = commentIsValid.value,
            ) {
                Text(stringResource(R.string.social_comment_post))
            }
        }
    }
}
