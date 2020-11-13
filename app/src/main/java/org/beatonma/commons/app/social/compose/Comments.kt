package org.beatonma.commons.app.social.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.signin.AmbientUserToken
import org.beatonma.commons.app.signin.NullUserToken
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.ui.compose.components.OutlineButton
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.Hint
import org.beatonma.commons.compose.components.LengthTextFieldValidator
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.components.ValidatedLengthTextField
import org.beatonma.commons.compose.modifiers.either
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.compose.util.withAnnotatedStyle
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.map
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.Elevation
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.endOfContent
import org.beatonma.commons.theme.compose.pdp
import org.beatonma.commons.theme.compose.plus
import org.beatonma.commons.theme.compose.theme.CORNER_SMALL
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import org.beatonma.commons.theme.compose.theme.systemui.imeOrNavigationBarsPadding
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
        // TODO Show sign-in instead of add comment fab
    }

    val swipeState = rememberSwipeableState(
        initialValue = uiState.value,
        animationSpec = CommonsSpring()
    )
    val swipeAnchors = mapOf(
        0F to SocialUiState.Expanded,
        500F to SocialUiState.ComposeComment
    )

    val offset = swipeState.offset.value
    val progress = (if (offset.isNaN()) 0F else offset).map(0F, 500F, 0F, 1F)

    val targetState = uiState.value
    val swipeable = Modifier.onlyWhen(targetState != SocialUiState.Collapsed) {
        if (targetState != swipeState.value) {
            swipeState.animateTo(targetState, onEnd = { _, finalState ->
                uiState.update(finalState)
            })
        }

        swipeable(
            state = swipeState,
            enabled = swipeState.value == SocialUiState.ComposeComment,

            anchors = swipeAnchors,
            orientation = Orientation.Vertical,
            reverseDirection = true,
        )
    }

    val focusRequester = remember { FocusRequester() }

    ModalScrim(
        alignment = Alignment.BottomEnd,
        alpha = progress,
        onClickAction = {
            uiState.update(SocialUiState.Expanded)
        }
    ) {
        val surfaceShape = getCommentUiSurfaceShape(progress)

        Surface(
            Modifier
                .padding(progress.lerpBetween(Padding.Fab, Padding.Zero))
                .navigationBarsPadding(scale = progress.reversed()) // The bottom sheet should extend behind navigation bar as it expands.
                .then(swipeable)
                .either(
                    condition = targetState == SocialUiState.Expanded,
                    whenTrue = {
                        clickable(onClick = AmbientSocialActions.current.onCreateCommentClick)
                    },
                    whenFalse = {
                        clickable { focusRequester.requestFocus() }
                    }
                )
                .drawShadow(Elevation.ModalSurface, surfaceShape),
            shape = surfaceShape,
            color = progress.progressIn(0F, 0.4F).lerpBetween(colors.secondary, colors.surface),
        ) {
            Box {
                CreateCommentButtonContent(progress)
                CreateCommentSheetContent(progress, focusRequester)
            }
        }
    }
}

@Composable
private fun CreateCommentButtonContent(
    progress: Float,
) {
    if (progress == 1F) {
        return
    }

    Text(
        stringResource(R.string.social_new_comment),
        Modifier
            .padding(Padding.ExtendedFabContent)
            .drawOpacity(progress.reversed().progressIn(0.8F, 1.0F)),
        color = colors.onSecondary,
        style = typography.button,
    )
}

@OptIn(ExperimentalFocus::class)
@Composable
private fun CreateCommentSheetContent(
    progress: Float,
    focusRequester: FocusRequester,
    commentText: MutableState<String> = remember { mutableStateOf("") },
    uiState: MutableState<SocialUiState> = AmbientSocialUiState.current,
) {
    if (progress == 0F) {
        return
    }

    val user = AmbientUserToken.current
    val commentValidator = remember {
        LengthTextFieldValidator(
            BuildConfig.SOCIAL_COMMENT_MIN_LENGTH,
            BuildConfig.SOCIAL_COMMENT_MAX_LENGTH
        )
    }
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

private fun getCommentUiSurfaceShape(progress: Float): Shape {
    val eased = progress.withEasing(LinearOutSlowInEasing)
    val upperCornerSize = eased.lerpBetween(24, CORNER_SMALL).dp
    val lowerCornerSize = eased.lerpBetween(24, 0).dp
    return RoundedCornerShape(upperCornerSize, upperCornerSize, lowerCornerSize, lowerCornerSize)
}
