package org.beatonma.commons.app.signin.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.social.compose.Username
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.TextValidationResult
import org.beatonma.commons.compose.components.TextValidationRules
import org.beatonma.commons.compose.components.ValidatedTextField
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.data.core.room.entities.user.UserToken

internal enum class State {
    ReadOnly,
    Editable,
    AwaitingResult,
    ;
}

@Composable
internal fun EditableUsername(
    userToken: UserToken,
    state: MutableState<State> = remember { mutableStateOf(State.ReadOnly) },
) {
    when (state.value) {
        State.ReadOnly -> ReadOnlyUsernameLayout(userToken = userToken, state = state)
        State.Editable -> EditableUsernameLayout(userToken = userToken, state = state)
        State.AwaitingResult -> TODO()
    }

    Text(state.value.name, style = typography.h6)
}

@Composable
private fun ReadOnlyUsernameLayout(
    userToken: UserToken,
    state: MutableState<State>,
) {
    val makeEditable = { state.update(State.Editable) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = makeEditable),
    ) {
        Username(userToken.username, style = typography.h4)

        IconButton(onClick = makeEditable) {
            Icon(Icons.Default.Edit)
        }
    }
}

@Composable
private fun EditableUsernameLayout(
    userToken: UserToken,
    state: MutableState<State>,
    validationMessages: ValidationMessages = rememberValidationMessages(),
    actions: UserProfileActions = AmbientUserProfileActions.current,
) {
    val text = rememberText(userToken.username)
    val keyboardOptions = remember {
        KeyboardOptions(
            keyboardType = KeyboardType.Text,
        )
    }
    val validationRules = rememberUsernameValidator()
    val validationResultMessage = rememberText()

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        ValidatedTextField(
            text, validationRules,
            onValueChange = { _, validationResult ->
                validationResultMessage.update(validationMessages.getMessage(validationResult))
            },
            validationResultMessage = validationResultMessage,
            maxLines = 1,
            modifier = Modifier.weight(10F),
            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(color = AmbientContentColor.current),
        )

        IconButton(onClick = { actions.signInActions.renameAccount(text.value) }) {
            Icon(Icons.Default.Done)
        }

        IconButton(onClick = { state.update(State.ReadOnly) }) {
            Icon(Icons.Default.Undo)
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
