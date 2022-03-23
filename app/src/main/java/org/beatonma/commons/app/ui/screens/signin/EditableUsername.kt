package org.beatonma.commons.app.ui.screens.signin

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.LoadingIcon
import org.beatonma.commons.app.ui.components.image.ClickableIcon
import org.beatonma.commons.app.ui.screens.social.Username
import org.beatonma.commons.compose.animation.AnimatedVisibility
import org.beatonma.commons.compose.components.FeedbackProvider
import org.beatonma.commons.compose.components.rememberFeedbackProvider
import org.beatonma.commons.compose.components.text.ComponentTitle
import org.beatonma.commons.compose.components.text.Hint
import org.beatonma.commons.compose.components.text.TextValidationResult
import org.beatonma.commons.compose.components.text.TextValidationRules
import org.beatonma.commons.compose.components.text.ValidatedTextField
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.util.RequestFocusWhen
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.util.testTag
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.preview.InAppPreview
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.theme.AppIcon
import org.beatonma.commons.themed.animation
import org.beatonma.commons.themed.padding

internal enum class EditableState {
    ReadOnly,
    Editable,
    AwaitingResult,
    ;
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun EditableUsername(
    userToken: UserToken,
    onSubmitRename: suspend (UserToken, String) -> RenameResult,
    modifier: Modifier = Modifier,
    state: MutableState<EditableState> = remember { mutableStateOf(EditableState.ReadOnly) },
    validationMessages: ValidationMessages = rememberValidationMessages(),
    focusRequester: FocusRequester = remember(::FocusRequester),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Crossfade(targetState = state.value, animationSpec = animation.spec()) { editableState ->
        when (editableState) {
            EditableState.ReadOnly ->
                ReadOnlyUsernameLayout(
                    userToken = userToken,
                    makeEditable = { state.value = EditableState.Editable },
                    modifier = modifier,
                )

            EditableState.Editable ->
                EditableUsernameLayout(
                    userToken,
                    onStateChange = { value -> state.value = value },
                    validationMessages = validationMessages,
                    coroutineScope = coroutineScope,
                    modifier = modifier,
                    focusRequester = focusRequester,
                    submitAction = onSubmitRename,
                )

            EditableState.AwaitingResult ->
                AwaitingResultLayout(
                    userToken = userToken,
                    modifier = modifier
                )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ReadOnlyUsernameLayout(
    userToken: UserToken,
    makeEditable: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentDescription = stringResource(R.string.content_description_edit_username)
    animation.AnimatedVisibility(visible = true, initiallyVisible = false, expand = false) {
        val content: @Composable () -> Unit = {
            Username(
                userToken.username,
                style = typography.h4,
                modifier = Modifier
                    .testTag(UserAccountTestTag.UsernameReadOnly)
            )

            ClickableIcon(
                AppIcon.Edit,
                contentDescription = contentDescription,
                onClick = makeEditable,
                testTag = UserAccountTestTag.UsernameEdit,
            )
        }

        // A row that measures the icon first, then constrains the text to whatever space is left.
        Layout(
            modifier = modifier.testTag(EditableState.ReadOnly),
            content = content
        ) { measurables, constraints ->
            check(measurables.size == 2)

            val iconPlaceable = measurables[1].measure(constraints)

            val textConstraints = constraints.copy(
                maxWidth = constraints.maxWidth - iconPlaceable.width
            )
            val textPlaceable = measurables[0].measure(textConstraints)

            val width: Int = iconPlaceable.width + textPlaceable.width
            val height: Int = maxOf(iconPlaceable.height, textPlaceable.height)

            layout(width, height) {
                textPlaceable.placeRelative(0, 0)
                iconPlaceable.placeRelative(textPlaceable.width, 0)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AwaitingResultLayout(
    userToken: UserToken,
    modifier: Modifier = Modifier,
) {
    animation.AnimatedVisibility(visible = true, initiallyVisible = false, expand = false) {
        Row(
            modifier = modifier.testTag(EditableState.AwaitingResult),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Username(
                userToken.username, style = typography.h4,
                modifier = Modifier.alpha(ContentAlpha.disabled)
            )

            LoadingIcon()
        }
    }
}

@OptIn(InternalCoroutinesApi::class, ExperimentalAnimationApi::class)
@Composable
private fun EditableUsernameLayout(
    userToken: UserToken,
    onStateChange: (EditableState) -> Unit,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    validationMessages: ValidationMessages,
    submitAction: suspend (UserToken, String) -> RenameResult,
    focusRequester: FocusRequester,
) {
    var text by rememberText(userToken.username)
    val keyboardOptions = remember { KeyboardOptions(keyboardType = KeyboardType.Text) }
    val validationRules = rememberUsernameValidator()
    val renameResultFeedback = rememberFeedbackProvider()

    fun renameAction() {
        onStateChange(EditableState.AwaitingResult)

        coroutineScope.launch {
            val result = submitAction(userToken, text)
            val resultState = when (result) {
                RenameResult.ACCEPTED,
                RenameResult.NO_CHANGE,
                -> EditableState.ReadOnly

                else -> EditableState.Editable
            }

            withContext(Dispatchers.Main) {
                renameResultFeedback.value = AnnotatedString(validationMessages.getMessage(result))
                onStateChange(resultState)
            }
        }
    }

    animation.AnimatedVisibility(visible = true, initiallyVisible = false, expand = false) {
        EditableUsernameLayout(
            text = text,
            onTextChange = { text = it },
            validationRules = validationRules,
            validationMessages = validationMessages,
            modifier = modifier,
            focusRequester = focusRequester,
            keyboardOptions = keyboardOptions,
            feedbackProvider = renameResultFeedback,
            onStateChange = onStateChange,
            onSubmitName = ::renameAction
        )
    }
}

@Composable
private fun EditableUsernameLayout(
    text: String,
    onTextChange: (String) -> Unit,
    validationRules: TextValidationRules,
    validationMessages: ValidationMessages,
    modifier: Modifier,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions,
    feedbackProvider: FeedbackProvider,
    onStateChange: (EditableState) -> Unit,
    onSubmitName: () -> Unit,
) {
    Column(
        modifier = modifier
            .testTag(EditableState.Editable),

    ) {
        ComponentTitle(
            stringResource(R.string.account_username_hint),
            Modifier.padding(padding.VerticalListItem),
            autoPadding = false,
        )

        ValidatedTextField(
            text,
            validationRules,
            onValueChange = { value, validationResult ->
                onTextChange(value)
                AnnotatedString(validationMessages.getMessage(validationResult))
            },
            placeholder = { Hint(R.string.account_username_hint) },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .focusTarget()
                .focusRequester(focusRequester),
            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(color = LocalContentColor.current),
            feedbackProvider = feedbackProvider,
            trailingIcon = {
                Row {
                    ClickableIcon(
                        AppIcon.Check,
                        contentDescription = stringResource(R.string.account_submit_new_username),
                        onClick = onSubmitName,
                        testTag = UserAccountTestTag.UsernameRequestRename
                    )

                    ClickableIcon(
                        AppIcon.Undo,
                        contentDescription = stringResource(R.string.content_description_edit_username),
                        testTag = UserAccountTestTag.UsernameCancelRename,
                    ) { onStateChange(EditableState.ReadOnly) }
                }
            }
        )

        RequestFocusWhen(focusRequester, true)
    }
}

/**
 * Username must:
 * - be valid length
 * - be made up of [ letters | digits | -dashes- | .dots. | _underscores_ ]
 * - first and last characters must be letters or digits
 */
@Composable
private fun rememberUsernameValidator() = remember {
    TextValidationRules(
        minLength = BuildConfig.ACCOUNT_USERNAME_MIN_LENGTH,
        maxLength = BuildConfig.ACCOUNT_USERNAME_MAX_LENGTH,
        regex = BuildConfig.ACCOUNT_USERNAME_PATTERN.toRegex(),
    )
}

@VisibleForTesting
@Composable
internal fun rememberValidationMessages(): ValidationMessages {
    val tooLong = stringResource(
        R.string.account_username_validation_too_long,
        BuildConfig.ACCOUNT_USERNAME_MAX_LENGTH
    )
    val tooShort = stringResource(
        R.string.account_username_validation_too_short,
        BuildConfig.ACCOUNT_USERNAME_MIN_LENGTH
    )
    val formatError = stringResource(R.string.account_username_validation_format_error)
    val error = stringResource(R.string.account_username_validation_try_again)
    val deniedByServer = stringResource(R.string.account_username_validation_denied_by_server)

    return remember {
        ValidationMessages(
            tooLong = tooLong,
            tooShort = tooShort,
            formatError = formatError,
            ok = "",
            error = error,
            deniedByServer = deniedByServer,
        )
    }
}

/**
 * @param formatError       The username contains invalid characters or has valid characters
 *                          in illegal positions
 *
 * @param deniedByServer    The server processed the request but refused to change the username
 *                          because it is reserved, blocked, or already taken.
 *
 * @param error             Server error or network error - trying again may work.
 */
@VisibleForTesting
internal class ValidationMessages(
    val tooLong: String,
    val tooShort: String,
    val formatError: String,
    val ok: String,
    val error: String,
    val deniedByServer: String,
) {
    fun getMessage(result: TextValidationResult) = when (result) {
        TextValidationResult.TOO_LONG -> tooLong
        TextValidationResult.TOO_SHORT -> tooShort
        TextValidationResult.FORMAT_ERROR -> formatError
        TextValidationResult.OK -> ok
    }

    fun getMessage(result: RenameResult) = when (result) {
        RenameResult.ACCEPTED, RenameResult.NO_CHANGE -> ok
        RenameResult.TOO_LONG -> tooLong
        RenameResult.TOO_SHORT -> tooShort
        RenameResult.BAD_START_OR_END -> formatError

        RenameResult.SERVER_DENIED -> deniedByServer

        RenameResult.SERVER_ERROR,
        RenameResult.SERVER_BAD_REQUEST,
        RenameResult.ERROR,
        -> error

        else -> error
    }
}

@Composable @Preview
fun EditableUsernamePreview() {
    LocalUserAccountActions = staticCompositionLocalOf { UserAccountActions() }

    InAppPreview {
        EditableUsername(
            SampleUserToken,
            onSubmitRename = UserAccountActions().renameAccount
        )
    }
}
