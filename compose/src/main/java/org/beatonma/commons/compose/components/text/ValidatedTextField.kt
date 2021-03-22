package org.beatonma.commons.compose.components.text

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.FeedbackProvider
import org.beatonma.commons.compose.components.LocalFeedbackMessage
import org.beatonma.commons.compose.components.clear
import org.beatonma.commons.compose.components.rememberFeedbackProvider
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

data class TextValidationFeedback(
    val ok: AnnotatedString?,
    val tooShort: AnnotatedString?,
    val tooLong: AnnotatedString?,
    val formatError: AnnotatedString?,
) {
    fun getFeedback(result: TextValidationResult): AnnotatedString? = when (result) {
        TextValidationResult.OK -> ok
        TextValidationResult.TOO_SHORT -> tooShort
        TextValidationResult.TOO_LONG -> tooLong
        TextValidationResult.FORMAT_ERROR -> formatError
    }
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

    ValidatedTextFieldLayout(
        value = value,
        onValueChange = { newValue, result ->
            feedbackProvider.value = onValueChange(newValue, result)
        },
        validationResult = validationResult.value,
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
        var textFieldValue by remember {
            mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
        }

        TextField(
            value = textFieldValue.copy(text = value),
            onValueChange = { value ->
                textFieldValue = value
                val result = validationRules.validate(value.text)
                onValueChange(value.text, result)
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
                Modifier,
                errorColor = colors.labelColor(
                    enabled = enabled,
                    error = true,
                    interactionSource = interactionSource,
                ).value
            )
        }

        if (internalFeedback) {
            FeedbackCounterLayout(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .animateContentSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                FeedbackText(feedbackProvider.value)
                counterText()
            }
        } else {
            counterText()
        }

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
        Spacer(Modifier.testTag("feedback_text__null_message"))
    } else {
        animation.Crossfade(message) {
            Text(
                message,
                Modifier.testTag("feedback_text"),
                style = typography.caption
            )
        }
    }
}

@Composable
private fun CounterText(
    validationRules: TextValidationRules,
    textLength: Int,
    modifier: Modifier,
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
        maxLines = 1,
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


/**
 * A row for 2 items which prioritises the second one. e.g. An icon at the
 * end of the row will take whatever space it requires, and text before it
 * can use whatever space is left.
 */
@Composable
private fun FeedbackCounterLayout(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = @Suppress("RedundantSamConstructor") MeasurePolicy { measurables, constraints ->
            check(measurables.size == 2)

            val lastWidth = measurables[1].maxIntrinsicWidth(0)
            val lastPlaceable = measurables[1].measure(
                constraints.copy(
                    minWidth = lastWidth,
                    maxWidth = lastWidth
                )
            )

            val firstWidth = constraints.maxWidth - lastPlaceable.width
            val firstConstraints = constraints.copy(
                minWidth = firstWidth,
                maxWidth = firstWidth
            )
            val firstPlaceable = measurables[0].measure(firstConstraints)

            val width: Int = constraints.maxWidth
            val height: Int = maxOf(lastPlaceable.height, firstPlaceable.height)

            val xPositions = IntArray(2) { 0 }
            with(horizontalArrangement) {
                this@MeasurePolicy.arrange(
                    width,
                    intArrayOf(firstPlaceable.width, lastPlaceable.width),
                    layoutDirection,
                    xPositions
                )
            }

            layout(width, height) {
                firstPlaceable.placeRelative(
                    xPositions[0],
                    verticalAlignment.align(firstPlaceable.height, height)
                )
                lastPlaceable.placeRelative(
                    xPositions[1],
                    verticalAlignment.align(lastPlaceable.height, height)
                )
            }
        },
    )
}
