package org.beatonma.commons.app.signin

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Undo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.social.compose.Username
import org.beatonma.commons.app.ui.compose.components.LoadingIcon
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.components.AmbientFeedbackMessage
import org.beatonma.commons.compose.components.Hint
import org.beatonma.commons.compose.components.TextValidationResult
import org.beatonma.commons.compose.components.TextValidationRules
import org.beatonma.commons.compose.components.ValidatedTextField
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.theme.compose.theme.CommonsSpring

internal enum class EditableState {
    ReadOnly,
    Editable,
    AwaitingResult,
    ;
}

internal val readOnlyVisibility = FloatPropKey()
internal val editableVisibility = FloatPropKey()
internal val awaitingResultVisibility = FloatPropKey()

@OptIn(ExperimentalFocus::class)
@Composable
internal fun EditableUsername(
    userToken: UserToken,
    state: MutableState<EditableState> = remember { mutableStateOf(EditableState.ReadOnly) },
    transitionDef: TransitionDefinition<EditableState> = rememberEditableStateTransition(),
    transition: TransitionState = transition(transitionDef, state.value),
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    val coroutineScope = rememberCoroutineScope()
    when (state.value) {
        EditableState.ReadOnly -> ReadOnlyUsernameLayout(userToken,
            state,
            Modifier.alpha(transition[readOnlyVisibility]))

        EditableState.Editable -> EditableUsernameLayout(
            userToken,
            state,
            coroutineScope = coroutineScope,
            modifier = Modifier.alpha(transition[editableVisibility]),
            focusRequester = focusRequester,
        )

        EditableState.AwaitingResult -> AwaitingResultLayout(userToken,
            Modifier.alpha(transition[awaitingResultVisibility]))
    }
}

@Composable
private fun ReadOnlyUsernameLayout(
    userToken: UserToken,
    state: MutableState<EditableState>,
    modifier: Modifier = Modifier,
) {
    val makeEditable = { state.update(EditableState.Editable) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(onClick = makeEditable),
    ) {
        Username(userToken.username, style = typography.h4)

        IconButton(onClick = makeEditable) {
            Icon(Icons.Default.Edit)
        }
    }
}

@Composable
private fun AwaitingResultLayout(
    userToken: UserToken,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Username(userToken.username, style = typography.h4,
            modifier = Modifier.alpha(ContentAlpha.disabled)
        )

        LoadingIcon()
    }
}

@OptIn(ExperimentalFocus::class, InternalCoroutinesApi::class)
@Composable
private fun EditableUsernameLayout(
    userToken: UserToken,
    state: MutableState<EditableState>,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
    validationMessages: ValidationMessages = rememberValidationMessages(),
    submitAction: suspend (UserToken, String) -> RenameResult = AmbientUserProfileActions.current.userAccountActions.renameAccount,
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    val text = rememberText(userToken.username)
    val keyboardOptions = remember { KeyboardOptions(keyboardType = KeyboardType.Text) }
    val validationRules = rememberUsernameValidator()
    val validationResultMessage = AmbientFeedbackMessage.current

    fun renameAction() {
        state.update(EditableState.AwaitingResult)

        coroutineScope.launch {
            val result = submitAction(userToken, text.value)
            val pendingState = when (result) {
                RenameResult.ACCEPTED,
                RenameResult.NO_CHANGE,
                -> EditableState.ReadOnly

                else -> EditableState.Editable
            }

            val message = validationMessages.getMessage(result)

            withContext(Dispatchers.Main) {
                state.update(pendingState)
                validationResultMessage.update(message)
            }
        }
    }

    EditableUsernameLayout(
        text,
        validationRules,
        validationResultMessage,
        validationMessages,
        modifier,
        focusRequester,
        keyboardOptions,
        AmbientContentColor,
        state,
        ::renameAction)
}

@OptIn(ExperimentalFocus::class)
@Composable
private fun EditableUsernameLayout(
    text: MutableState<String>,
    validationRules: TextValidationRules,
    validationResultMessage: MutableState<String>,
    validationMessages: ValidationMessages,
    modifier: Modifier,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions,
    AmbientContentColor: ProvidableAmbient<Color>,
    state: MutableState<EditableState>,
    onSubmitName: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        ValidatedTextField(
            text, validationRules,
            onValueChange = { _, validationResult ->
                validationResultMessage.update(
                    validationMessages.getMessage(validationResult)
                )
            },
            validationResultMessage = validationResultMessage,
            placeholder = {
                Hint(stringResource(
                    R.string.account_username_validation_too_short,
                    BuildConfig.ACCOUNT_USERNAME_MIN_LENGTH))
            },
            maxLines = 1,
            modifier = Modifier
                .weight(10F)
                .focusRequester(focusRequester),
            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(color = AmbientContentColor.current),
        )

        IconButton(onClick = onSubmitName) {
            Icon(Icons.Default.Done)
        }

        IconButton(onClick = { state.update(EditableState.ReadOnly) }) {
            Icon(Icons.Default.Undo)
        }

        if (state.value == EditableState.Editable) {
            focusRequester.requestFocus()
        }
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
        pattern = BuildConfig.ACCOUNT_USERNAME_PATTERN.toRegex(),
    )
}

@Composable
private fun rememberValidationMessages(): ValidationMessages {
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
private class ValidationMessages(
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

@Composable
internal fun rememberEditableStateTransition(
    animSpec: AnimationSpec<Float> = CommonsSpring(),
): TransitionDefinition<EditableState> =
    remember {
        transitionDefinition {
            state(EditableState.ReadOnly) {
                this[readOnlyVisibility] = 1F
                this[editableVisibility] = 0F
                this[awaitingResultVisibility] = 0F
            }
            state(EditableState.Editable) {
                this[readOnlyVisibility] = 0F
                this[editableVisibility] = 1F
                this[awaitingResultVisibility] = 0F
            }
            state(EditableState.AwaitingResult) {
                this[readOnlyVisibility] = 0F
                this[editableVisibility] = 0F
                this[awaitingResultVisibility] = 1F
            }

            transition(
                EditableState.ReadOnly to EditableState.Editable,

                EditableState.Editable to EditableState.ReadOnly,
                EditableState.Editable to EditableState.AwaitingResult,

                EditableState.AwaitingResult to EditableState.Editable,
                EditableState.AwaitingResult to EditableState.ReadOnly,
            ) {
                progressKey using animSpec
            }
        }
    }
