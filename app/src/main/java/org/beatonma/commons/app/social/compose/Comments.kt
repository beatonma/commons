package org.beatonma.commons.app.social.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.ambient.withEmphasisHigh
import org.beatonma.commons.compose.ambient.withEmphasisMedium
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.LengthTextFieldValidator
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.components.ValidatedLengthTextField
import org.beatonma.commons.compose.modifiers.either
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.Elevation
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.pdp
import org.beatonma.commons.theme.compose.plus
import org.beatonma.commons.theme.compose.theme.CORNER_SMALL
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

/**
 * Displays a FAB which expands into a bottom sheet dialog for comment authoring.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateCommentUi(
    uiState: MutableState<SocialUiState> = AmbientSocialUiState.current,
    progress: Float = AmbientSocialUiTransition.current[ExpandedComposeCommentProgress],
) {
    val surfaceShape = getCommentUiSurfaceShape(progress)

    ModalScrim(
        alignment = Alignment.BottomEnd,
        alpha = progress,
        onClickAction = {
            uiState.update(SocialUiState.Expanded)

        }
    ) {
        Surface(
            Modifier
                .padding(progress.lerpBetween(Padding.Fab, Padding.Zero))
                .navigationBarsPadding(scale = progress.reversed()) // The bottom sheet should extend behind navigation bar as it expands.
                .either(
                    condition = progress == 0F,
                    whenTrue = {
                        clickable(onClick = AmbientSocialActions.current.onCreateCommentClick)
                    },
                    whenFalse = {
                        clickable { }
                    }
                )
                .drawShadow(Elevation.ModalSurface, surfaceShape),
            shape = surfaceShape,
            color = progress.progressIn(0F, 0.4F).lerpBetween(colors.secondary, colors.surface),
        ) {
            Box {
                CreateCommentButtonContent(progress)
                CreateCommentSheetContent(progress)
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

@Composable
private fun CreateCommentSheetContent(
    progress: Float,
    commentText: MutableState<String> = remember { mutableStateOf("") },
) {
    if (progress == 0F) {
        return
    }

    CardText(
        Modifier
            .wrapContentSize(horizontalProgress = progress.progressIn(0F, 0.4F),
                verticalProgress = progress.progressIn(0F, 0.8F))
            .drawOpacity(progress.progressIn(0.8F, 1F))
            .imeOrNavigationBarsPadding(scale = progress)
    ) {

        Column {
            Text(stringResource(R.string.social_compose_comment),
                modifier = Modifier.padding(Padding.VerticalListItem),
                style = typography.h6
            )

            ValidatedLengthTextField(
                value = commentText.value,
                validator = LengthTextFieldValidator(
                    BuildConfig.SOCIAL_COMMENT_MIN_LENGTH,
                    BuildConfig.SOCIAL_COMMENT_MAX_LENGTH
                ),
                onValueChange = { value, isValid ->
                    commentText.update(value)

                    if (isValid) {
                        // TODO
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(64.dp, 220.dp)
                    .animateContentSize()
            )

            OutlinedButton(
                onClick = AmbientSocialActions.current.onCommentSubmitClick,
                modifier = Modifier.align(Alignment.End).padding(Padding.CardButton),
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
