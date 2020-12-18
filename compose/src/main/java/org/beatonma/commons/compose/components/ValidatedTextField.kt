package org.beatonma.commons.compose.components

import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.ContainerAlpha
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.onDispose
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.util.update
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
    val pattern: Regex = ".*".toRegex(),
) {
    fun validate(value: String): TextValidationResult = when {
        !pattern.matches(value) -> TextValidationResult.FORMAT_ERROR
        value.length < minLength -> TextValidationResult.TOO_SHORT
        value.length > maxLength -> TextValidationResult.TOO_LONG
        else -> TextValidationResult.OK
    }
}

@Composable
fun ValidatedTextField(
    valueState: MutableState<String>,
    validationRules: TextValidationRules,
    onValueChange: (String, validationResult: TextValidationResult) -> Unit,
    modifier: Modifier = Modifier,
    validationResultMessage: MutableState<String> = rememberText(),
    textStyle: TextStyle = AmbientTextStyle.current.copy(color = AmbientContentColor.current),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isErrorValue: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE,
    onImeActionPerformed: (ImeAction, SoftwareKeyboardController?) -> Unit = { _, _ -> },
    onTextInputStarted: (SoftwareKeyboardController) -> Unit = {},
    interactionState: InteractionState = remember { InteractionState() },
    activeColor: Color = colors.primary,
    inactiveColor: Color = colors.onSurface,
    errorColor: Color = colors.error,
    backgroundColor: Color = colors.onSurface.copy(alpha = ContainerAlpha),
    shape: Shape = shapes.small.withSquareBottom(),
) {
    ValidatedTextField(
        value = valueState.value,
        validationRules = validationRules,
        onValueChange = { value, validationResult ->
            valueState.value = value
            onValueChange(value, validationResult)
        },
        modifier,
        validationResultMessage,
        textStyle,
        label,
        placeholder,
        leadingIcon,
        trailingIcon,
        isErrorValue,
        visualTransformation,
        keyboardOptions,
        maxLines,
        onImeActionPerformed,
        onTextInputStarted,
        interactionState,
        activeColor,
        inactiveColor,
        errorColor,
        backgroundColor,
        shape,
    )
}

@Composable
fun ValidatedTextField(
    value: String,
    validationRules: TextValidationRules,
    onValueChange: (String, valid: TextValidationResult) -> Unit,
    modifier: Modifier = Modifier,
    validationResultMessage: MutableState<String> = rememberText(),
    textStyle: TextStyle = AmbientTextStyle.current.copy(color = AmbientContentColor.current),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isErrorValue: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE,
    onImeActionPerformed: (ImeAction, SoftwareKeyboardController?) -> Unit = { _, _ -> },
    onTextInputStarted: (SoftwareKeyboardController) -> Unit = {},
    interactionState: InteractionState = remember { InteractionState() },
    activeColor: Color = colors.primary,
    inactiveColor: Color = colors.onSurface,
    errorColor: Color = colors.error,
    backgroundColor: Color = colors.onSurface.copy(alpha = ContainerAlpha),
    shape: Shape = shapes.small.withSquareBottom(),
    feedbackProvider: MutableState<String> = AmbientFeedbackMessage.current,
) {
    val validationResult = remember { mutableStateOf(TextValidationResult.OK) }

    Column(modifier) {
        TextField(
            value = value,
            onValueChange = { value ->
                val result = validationRules.validate(value)
                validationResult.update(result)
                onValueChange(value, result)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isErrorValue = isErrorValue || validationResult.value.isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            maxLines = maxLines,
            onImeActionPerformed = onImeActionPerformed,
            onTextInputStarted = onTextInputStarted,
            interactionState = interactionState,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            errorColor = errorColor,
            backgroundColor = backgroundColor,
            shape = shape,
        )

        CounterText(
            validationRules, value.length,
            Modifier.align(Alignment.End).padding(top = 4.dp),
            errorColor = errorColor
        )

        feedbackProvider.update(validationResultMessage.value)

        onDispose {
            feedbackProvider.update("")
        }
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

    val counterText = mutableStateOf(
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
    )

    Text(
        counterText.value,
        modifier,
        style = typography.caption,
    )
}
