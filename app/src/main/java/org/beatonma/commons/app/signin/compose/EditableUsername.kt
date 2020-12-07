package org.beatonma.commons.app.signin.compose

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.social.compose.Username
import org.beatonma.commons.app.ui.compose.components.LoadingIcon
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.progressKey
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

private val readOnlyAlpha = FloatPropKey()
private val editableAlpha = FloatPropKey()
private val awaitingResultAlpha = FloatPropKey()

@OptIn(ExperimentalFocus::class)
@Composable
internal fun EditableUsername(
    userToken: UserToken,
    state: MutableState<EditableState> = remember { mutableStateOf(EditableState.ReadOnly) },
    transitionDef: TransitionDefinition<EditableState> = rememberEditableStateTransition(),
    transition: TransitionState = transition(transitionDef, state.value),
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    when (state.value) {
        EditableState.ReadOnly -> ReadOnlyUsernameLayout(userToken,
            state,
            Modifier.alpha(transition[readOnlyAlpha]))

        EditableState.Editable -> EditableUsernameLayout(userToken,
            state,
            Modifier.alpha(transition[editableAlpha]),
            focusRequester = focusRequester)

        EditableState.AwaitingResult -> AwaitingResultLayout(userToken,
            Modifier.alpha(transition[awaitingResultAlpha]))
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

@OptIn(ExperimentalFocus::class)
@Composable
private fun EditableUsernameLayout(
    userToken: UserToken,
    state: MutableState<EditableState>,
    modifier: Modifier = Modifier,
    validationMessages: ValidationMessages = rememberValidationMessages(),
    actions: UserProfileActions = AmbientUserProfileActions.current,
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    val text = rememberText(userToken.username)
    val keyboardOptions = remember {
        KeyboardOptions(
            keyboardType = KeyboardType.Text,
        )
    }
    val validationRules = rememberUsernameValidator()
    val validationResultMessage = rememberText()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        ValidatedTextField(
            text, validationRules,
            onValueChange = { _, validationResult ->
                validationResultMessage.update(validationMessages.getMessage(validationResult))
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

        IconButton(
            onClick = {
                state.update(EditableState.AwaitingResult)
                actions.signInActions.renameAccount(text.value)
            }
        ) {
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

    return remember {
        ValidationMessages(
            tooLong = tooLong,
            tooShort = tooShort,
            formatError = formatError,
            ok = "",
        )
    }
}

private class ValidationMessages(
    val tooLong: String,
    val tooShort: String,
    val formatError: String,
    val ok: String,
) {
    fun getMessage(result: TextValidationResult) = when (result) {
        TextValidationResult.TOO_LONG -> tooLong
        TextValidationResult.TOO_SHORT -> tooShort
        TextValidationResult.FORMAT_ERROR -> formatError
        TextValidationResult.OK -> ok
    }
}

@Composable
private fun rememberEditableStateTransition(
    animSpec: AnimationSpec<Float> = CommonsSpring(),
): TransitionDefinition<EditableState> =
    remember {
        transitionDefinition {
            state(EditableState.ReadOnly) {
                this[readOnlyAlpha] = 1F
                this[editableAlpha] = 0F
                this[awaitingResultAlpha] = 0F
            }
            state(EditableState.Editable) {
                this[readOnlyAlpha] = 0F
                this[editableAlpha] = 1F
                this[awaitingResultAlpha] = 0F
            }
            state(EditableState.AwaitingResult) {
                this[readOnlyAlpha] = 0F
                this[editableAlpha] = 0F
                this[awaitingResultAlpha] = 1F
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
