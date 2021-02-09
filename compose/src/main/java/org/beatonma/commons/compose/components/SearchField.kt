package org.beatonma.commons.compose.components

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.theme.compose.components.Hint

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    hint: String,
    onQueryChange: (String) -> Unit = {},
    onSubmit: (ImeAction, String) -> Unit = { _, _ -> },
    query: MutableState<String> = rememberText(),
    maxLines: Int = 1,
    activeColor: Color = MaterialTheme.colors.primary,
    inactiveColor: Color = MaterialTheme.colors.onSurface,
    errorColor: Color = MaterialTheme.colors.error,
    textStyle: TextStyle = AmbientTextStyle.current,
) {
    TextField(
        value = query.value,
        onValueChange = { queryText ->
            query.update(queryText)
            onQueryChange(queryText)
        },
        modifier = modifier,
        textStyle = textStyle,
        placeholder = { Hint(hint) },
        trailingIcon = {
            if (query.value.isNotEmpty()) {
                IconButton(onClick = { query.update("") }) {
                    Icon(Icons.Default.Clear)
                }
            }
        },
        onImeActionPerformed = { imeAction, controller ->
            controller?.hideSoftwareKeyboard()
            onSubmit(imeAction, query.value)
        },
        maxLines = maxLines,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        errorColor = errorColor,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    @StringRes hint: Int,
    onQueryChange: (String) -> Unit = {},
    onSubmit: (ImeAction, String) -> Unit = { _, _ -> },
    query: MutableState<String> = rememberText(),
    maxLines: Int = 1,
    activeColor: Color = MaterialTheme.colors.primary,
    inactiveColor: Color = MaterialTheme.colors.onSurface,
    errorColor: Color = MaterialTheme.colors.error,
    textStyle: TextStyle = AmbientTextStyle.current,
) {
    SearchTextField(
        modifier,
        hint = stringResource(hint),
        onQueryChange,
        onSubmit,
        query,
        maxLines,
        activeColor,
        inactiveColor,
        errorColor,
        textStyle
    )
}
