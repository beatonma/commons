package org.beatonma.commons.app.social

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.signin.LocalUserToken
import org.beatonma.commons.app.signin.NullUserToken
import org.beatonma.commons.app.signin.SignInFabUi
import org.beatonma.commons.app.ui.compose.components.CommonsOutlinedButton
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.fabbottomsheet.BottomSheetText
import org.beatonma.commons.compose.components.fabbottomsheet.FabBottomSheet
import org.beatonma.commons.compose.components.fabbottomsheet.FabBottomSheetState
import org.beatonma.commons.compose.components.fabbottomsheet.FabText
import org.beatonma.commons.compose.components.text.Hint
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.components.text.TextValidationFeedback
import org.beatonma.commons.compose.components.text.TextValidationResult
import org.beatonma.commons.compose.components.text.TextValidationRules
import org.beatonma.commons.compose.components.text.ValidatedTextField
import org.beatonma.commons.compose.util.RequestFocusWhen
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding


/**
 * Displays a FAB which expands into a bottom sheet dialog for comment authoring.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateCommentUi(
    userToken: UserToken = LocalUserToken.current,
    uiState: MutableState<SocialUiState> = LocalSocialUiState.current,
) {
    if (userToken == NullUserToken) {
        // User can't create a new comment until they are signed in.
        SignInFabUi()
        return
    }

    val focusRequester = remember(::FocusRequester)

    val fabBottomSheetState = remember(uiState.value) {
        mutableStateOf(
            when (uiState.value) {
                SocialUiState.ComposeComment -> FabBottomSheetState.BottomSheet
                else -> FabBottomSheetState.Fab
            }
        )
    }

    FabBottomSheet(
        fabClickLabel = stringResource(R.string.social_compose_comment),
        state = fabBottomSheetState,
        fabContent = { progress ->
            CreateCommentButtonContent(progress)
        },
        bottomSheetContent = { progress ->
            CreateCommentSheetContent(userToken, progress, focusRequester)
        }
    )
}

@Composable
private fun CreateCommentButtonContent(progress: Float) =
    FabText(stringResource(R.string.social_new_comment), progress)

@Composable
private fun CreateCommentSheetContent(
    userToken: UserToken,
    progress: Float,
    focusRequester: FocusRequester,
    commentText: MutableState<String> = rememberText(),
    commentValidator: TextValidationRules = LocalSocialCommentValidator.current,
) {
    BottomSheetText(progress) {
        val commentIsValid = rememberBoolean(false)
        val feedback = rememberCommentValidationFeedback()

        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(Padding.VerticalListItem),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ResourceText(
                    R.string.social_compose_comment,
                    style = typography.h6
                )

                Username(userToken.username)
            }

            ValidatedTextField(
                commentText.value, commentValidator,
                placeholder = {
                    Hint(R.string.social_compose_comment_hint)
                },
                onValueChange = { value, validationResult ->
                    commentText.value = value
                    commentIsValid.value = validationResult == TextValidationResult.OK
                    feedback.getFeedback(validationResult)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(64.dp, 220.dp)
                    .animateContentSize()
                    .focusRequester(focusRequester)
            )

            val socialActions = LocalSocialActions.current
            CommonsOutlinedButton(
                onClick = { socialActions.onCommentSubmitClick(commentText.value) },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(Padding.CardButton)
                    .testTag(TestTag.Submit)
                ,
                enabled = commentIsValid.value,
            ) {
                ResourceText(R.string.social_comment_post)
            }
        }
    }

    RequestFocusWhen(focusRequester, progress == 1F)
}

@Composable
private fun rememberCommentValidationFeedback(): TextValidationFeedback {
    val tooShort = AnnotatedString(
        stringResource(R.string.social_comment_invalid_length,
            BuildConfig.SOCIAL_COMMENT_MIN_LENGTH,
            BuildConfig.SOCIAL_COMMENT_MAX_LENGTH
        ))

    return remember {
        TextValidationFeedback(
            ok = null,
            tooShort = tooShort,
            tooLong = null,
            formatError = null,
        )
    }
}
