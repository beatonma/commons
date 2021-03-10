package org.beatonma.commons.compose.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.commons.theme.compose.theme.withSquareBottom

enum class TextValidationResult {
    OK,
    TOO_SHORT,
    TOO_LONG,
    FORMAT_ERROR,
    ;

    val isOk get() = this == OK
    val isError get() = this != OK
}

data class TextValidationRules(
    val minLength: Int = 0,
    val maxLength: Int = Integer.MAX_VALUE,
    val regex: Regex = ".*".toRegex(),
) {
    init {
        require(maxLength >= minLength)
    }

    fun validate(value: String): TextValidationResult = when {
        !regex.matches(value) -> TextValidationResult.FORMAT_ERROR
        value.length < minLength -> TextValidationResult.TOO_SHORT
        value.length > maxLength -> TextValidationResult.TOO_LONG
        else -> TextValidationResult.OK
    }
}

@Composable
fun rememberValidationRules(
    minLength: Int = 0,
    maxLength: Int = Integer.MAX_VALUE,
    regex: Regex = ".*".toRegex(),
) = remember { TextValidationRules(minLength, maxLength, regex) }

/**
 * @param onValueChange     Returns an AnnotatedString (suitable for display) which describes any
 *                          validation issues and how to fix them.
 *
 * @param internalFeedback  If true, validation messages will be displayed as part of the widget.
 *                          If false, validation messages should be displayed by some other means.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ValidatedTextField(
    value: String,
    validationRules: TextValidationRules,
    onValueChange: (newValue: String, result: TextValidationResult) -> AnnotatedString?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isErrorValue: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = remember { KeyboardActions() },
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember(::MutableInteractionSource),
    shape: Shape = shapes.small.withSquareBottom(),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    internalFeedback: Boolean = true,
    feedbackProvider: FeedbackProvider = if (internalFeedback) rememberFeedbackProvider() else LocalFeedbackMessage.current,
) {
    val validationResult = remember { mutableStateOf(TextValidationResult.OK) }
    val validationResultMessage: MutableState<AnnotatedString?> = remember { mutableStateOf(null) }

    ValidatedTextFieldLayout(
        value = value,
        onValueChange = { newValue, result ->
            validationResultMessage.value = onValueChange(newValue, result)
        },
        validationResult = validationResult.value,
        validationResultMessage = validationResultMessage.value,
        validationRules = validationRules,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isErrorValue = isErrorValue,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
        internalFeedback = internalFeedback,
        feedbackProvider = feedbackProvider,
    )
}

@Composable
private fun ValidatedTextFieldLayout(
    value: String,
    onValueChange: (text: String, valid: TextValidationResult) -> Unit,
    validationResult: TextValidationResult,
    validationResultMessage: AnnotatedString?,
    validationRules: TextValidationRules,
    modifier: Modifier,
    enabled: Boolean,
    readOnly: Boolean,
    textStyle: TextStyle,
    label: @Composable (() -> Unit)?,
    placeholder: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    isErrorValue: Boolean,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    singleLine: Boolean,
    maxLines: Int,
    interactionSource: MutableInteractionSource,
    shape: Shape,
    colors: TextFieldColors,
    internalFeedback: Boolean,
    feedbackProvider: FeedbackProvider,
) {
    Column(modifier) {
        TextField(
            value = value,
            onValueChange = { value ->
                val result = validationRules.validate(value)
                onValueChange(value, result)

            },
            enabled = enabled,
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("validated_text"),
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isErrorValue || validationResult.isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
        )

        val counterText: @Composable () -> Unit = {
            CounterText(
                validationRules, value.length,
                Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp),
                errorColor = colors.labelColor(
                    enabled = enabled,
                    error = true,
                    interactionSource = interactionSource,
                ).value
            )
        }
        if (internalFeedback) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FeedbackText(message = validationResultMessage)
                counterText()
            }
        }
        else {
            counterText()
        }

        ShowFeedback(validationResultMessage)

        DisposableEffect(key1 = true) {
            onDispose {
                feedbackProvider.clear()
            }
        }
    }
}

@Composable
private fun FeedbackText(
    message: AnnotatedString?,
) {
    if (message == null) {
        Spacer(Modifier)
    }
    else {
        Text(message, Modifier.testTag("feedback_text"), style = typography.caption)
    }
}

@Composable
private fun CounterText(
    validationRules: TextValidationRules,
    textLength: Int,
    modifier: Modifier = Modifier,
    errorColor: Color,
) {
    val warningStyle = remember { SpanStyle(fontWeight = FontWeight.Bold) }
    val errorStyle = remember { SpanStyle(color = errorColor) }
    val defaultStyle = remember { SpanStyle() }

    val counterText =
        buildAnnotatedString {
            val showWarning =
                textLength < validationRules.minLength || textLength > validationRules.maxLength
            withStyle(if (showWarning) warningStyle else defaultStyle) {
                withStyle(if (showWarning) errorStyle else defaultStyle) {
                    append("$textLength")
                }
                if (validationRules.maxLength != Integer.MAX_VALUE) {
                    append("/${validationRules.maxLength}")
                }
            }
        }

    Text(
        counterText,
        modifier.testTag("counter_text"),
        style = typography.caption,
    )
}


@Preview
@Composable
fun ValidatedTextFieldPreview() {
    val text = rememberText()
    val rules = rememberValidationRules(
        minLength = 3,
        maxLength = 10,
        regex = "[a-z]+".toRegex()
    )

    CommonsTheme {
        Surface {
            ValidatedTextField(
                value = text.value,
                validationRules = rules,
                onValueChange = { newValue, valid ->
                    text.value = newValue
                    return@ValidatedTextField AnnotatedString(valid.name)
                },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
