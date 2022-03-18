package org.beatonma.commons.compose.components.text

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.UiIcon

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    onSubmit: (ImeAction, String) -> Unit = { _, _ -> },
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    textStyle: TextStyle = LocalTextStyle.current,
) {
    SearchFieldLayout(
        hint = hint,
        modifier = modifier,
        query = query,
        onQueryChange = { queryText ->
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
        colors = colors,
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
    colors: TextFieldColors,
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
                enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
                exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.Center),
            ) {
                IconButton(
                    onClick = { onQueryChange("") },
                    Modifier.testTag(TestTag.Clear)
                ) {
                    Icon(
                        UiIcon.Clear,
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
        colors = colors,
    )
}
