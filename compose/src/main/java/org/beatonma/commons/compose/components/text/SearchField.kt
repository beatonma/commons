package org.beatonma.commons.compose.components.text

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.components.text.Hint

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchField(
    hint: String,
    modifier: Modifier = Modifier,
    query: MutableState<String> = rememberText(),
    onQueryChange: (String) -> Unit = {},
    onSubmit: (ImeAction, String) -> Unit = { _, _ -> },
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    activeColor: Color = MaterialTheme.colors.primary,
    errorColor: Color = MaterialTheme.colors.error,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    SearchFieldLayout(
        hint = hint,
        modifier = modifier,
        query = query.value,
        onQueryChange = { queryText ->
            query.value = queryText
            onQueryChange(queryText)
        },
        onSubmit = onSubmit,
        textStyle = textStyle,
        maxLines = maxLines,
        isError = isError,
        singleLine = singleLine,
        enabled = enabled,
        label = label,
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        activeColor = activeColor,
        errorColor = errorColor,
    )
}

@Composable
fun SearchField(
    @StringRes hint: Int,
    modifier: Modifier = Modifier,
    query: MutableState<String> = rememberText(),
    onQueryChange: (String) -> Unit = {},
    onSubmit: (ImeAction, String) -> Unit = { _, _ -> },
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    activeColor: Color = MaterialTheme.colors.primary,
    errorColor: Color = MaterialTheme.colors.error,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    SearchField(
        hint = stringResource(hint),
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        onSubmit = onSubmit,
        enabled = enabled,
        label = label,
        leadingIcon = leadingIcon,
        maxLines = maxLines,
        singleLine = singleLine,
        isError = isError,
        visualTransformation = visualTransformation,
        activeColor = activeColor,
        errorColor = errorColor,
        textStyle = textStyle,
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SearchFieldLayout(
    hint: String,
    modifier: Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSubmit: (ImeAction, String) -> Unit,
    enabled: Boolean,
    label: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    maxLines: Int,
    singleLine: Boolean,
    isError: Boolean,
    visualTransformation: VisualTransformation,
    activeColor: Color,
    errorColor: Color,
    textStyle: TextStyle,
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        textStyle = textStyle,
        placeholder = { Hint(hint) },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn() + expandIn(Alignment.Center),
                exit = fadeOut() + shrinkOut(Alignment.Center),
            ) {
                IconButton(
                    onClick = { onQueryChange("") },
                    Modifier.testTag("clear_icon")
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = stringResource(R.string.content_description_clear_search_query),
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSubmit(ImeAction.Search, query)
            }
        ),
        maxLines = maxLines,
        isError = isError,
        singleLine = singleLine,
        enabled = enabled,
        label = label,
        leadingIcon = leadingIcon,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.textFieldColors(
            textColor = activeColor,
            unfocusedIndicatorColor = activeColor.copy(alpha = TextFieldDefaults.UnfocusedIndicatorLineOpacity),
            errorCursorColor = errorColor,
            errorLabelColor = errorColor,
            placeholderColor = activeColor.copy(ContentAlpha.medium),
        ),
    )
}

@Composable @Preview
fun SearchTextFieldPreview() {
    Column {
        SearchField(
            hint = "Search for..."
        )

        Spacer(Modifier.height(64.dp))

        SearchField(
            hint = "Search for..."
        )
    }
}
