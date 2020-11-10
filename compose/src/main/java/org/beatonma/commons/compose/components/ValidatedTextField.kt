package org.beatonma.commons.compose.components

import androidx.compose.foundation.AmbientTextStyle
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.ContainerAlpha
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography

fun interface TextFieldValidator<T : TextValidationMetrics> {
    fun validate(value: String, out: T): T
}

sealed class TextValidationMetrics(var isValid: Boolean)
class TextLengthValidationMetrics(var value: String, var length: Int, isValid: Boolean) :
    TextValidationMetrics(isValid)

data class LengthTextFieldValidator(
    val minLength: Int = 0,
    val maxLength: Int,
) : TextFieldValidator<TextLengthValidationMetrics> {
    override fun validate(
        value: String,
        out: TextLengthValidationMetrics,
    ): TextLengthValidationMetrics {
        val trimmed = value.trim()
        val length = trimmed.length

        out.value = trimmed
        out.length = length
        out.isValid = length in minLength..maxLength

        return out
    }
}

@Composable
fun ValidatedLengthTextField(
    value: String,
    validator: LengthTextFieldValidator,
    onValueChange: (String, valid: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AmbientTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isErrorValue: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Unspecified,
    onImeActionPerformed: (ImeAction, SoftwareKeyboardController?) -> Unit = { _, _ -> },
    onTextInputStarted: (SoftwareKeyboardController) -> Unit = {},
    interactionState: InteractionState = remember { InteractionState() },
    activeColor: Color = colors.primary,
    inactiveColor: Color = colors.onSurface,
    errorColor: Color = colors.error,
    backgroundColor: Color = colors.onSurface.copy(alpha = ContainerAlpha),
    shape: Shape = shapes.small.copy(bottomLeft = ZeroCornerSize, bottomRight = ZeroCornerSize),
) {
    val valueMetrics =
        remember { validator.validate(value, TextLengthValidationMetrics("", 0, false)) }

    Column(modifier) {
        TextField(
            value = value,
            onValueChange = { value ->
                validator.validate(value, valueMetrics)
                onValueChange(valueMetrics.value, valueMetrics.isValid)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isErrorValue = isErrorValue,
            visualTransformation = visualTransformation,
            keyboardType = keyboardType,
            imeAction = imeAction,
            onImeActionPerformed = onImeActionPerformed,
            onTextInputStarted = onTextInputStarted,
            interactionState = interactionState,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            errorColor = errorColor,
            backgroundColor = backgroundColor,
            shape = shape
        )

        CounterText(
            valueMetrics, validator,
            Modifier.align(Alignment.End).padding(top = 4.dp),
            inactiveColor, errorColor
        )
    }
}

@Composable
private fun CounterText(
    valueMetrics: TextLengthValidationMetrics,
    validator: LengthTextFieldValidator,
    modifier: Modifier = Modifier,
    inactiveColor: Color,
    errorColor: Color,
) {
    val warningStyle = remember { SpanStyle(fontWeight = FontWeight.Bold) }
    val defaultStyle = remember { SpanStyle() }

    val counterText = mutableStateOf(
        AnnotatedString.Builder().apply {
            val showWarning = !valueMetrics.isValid && valueMetrics.length > 0
            withStyle(if (showWarning) warningStyle else defaultStyle) {
                withStyle(SpanStyle(color = if (valueMetrics.isValid) inactiveColor else errorColor)) {
                    append("${valueMetrics.length}")
                }
                append("/${validator.maxLength}")
            }
        }.toAnnotatedString()
    )

    Text(
        counterText.value,
        modifier,
        style = typography.caption,
    )
}