package org.beatonma.compose.themepreview

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.ToggleableState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val FillMaxWidth = Modifier.fillMaxWidth()

@Composable
internal fun WidgetsPreview() {
    Actions()
    TextFields()
    ProgressIndicators()
    Selections()
    Tabs()
    Lists()

    FullWidthSample("Snackbar") {
        Snackbar(
            action = {
                TextButton(onClick = DefaultOnClick) {
                    Text("Action", color = SnackbarConstants.defaultActionPrimaryColor)
                }
            }
        ) {
            Text("Snackbar")
        }
    }
}

@Composable
private fun FullWidthSample(
    name: String,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Sample(name, FillMaxWidth, modifier, content)
}

@Composable
private fun Sample(
    name: String,
    columnModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Column(
        columnModifier.padding(16.dp),
    ) {
        Text(
            name,
            Modifier.padding(vertical = 16.dp),
            style = MaterialTheme.typography.caption
        )
        content(modifier)
    }
}

@Composable
private fun Actions() {
    ScrollableRow {
        Sample("Button") {
            Button(onClick = DefaultOnClick) {
                Text("Button")
            }
        }
        Sample("OutlinedButton") {
            OutlinedButton(onClick = DefaultOnClick) {
                Text("Button")
            }
        }
        Sample("TextButton") {
            TextButton(onClick = DefaultOnClick) {
                Text("Button")
            }
        }
        Sample("IconButton") {
            IconButton(onClick = DefaultOnClick) {
                DefaultIcon()
            }
        }
        Sample("FloatingActionButton") {
            FloatingActionButton(onClick = DefaultOnClick) {
                DefaultIcon()
            }
        }
    }
}

@Composable
private fun TextFields() {
    ScrollableRow {
        val textFieldValue = remember { mutableStateOf("") }
        Sample("TextField") {
            TextField(
                textFieldValue.value,
                onValueChange = { value -> textFieldValue.value = value },
                { Text("TextField") },

                backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.4F),
            )
        }

        Sample("OutlinedTextField") {
            OutlinedTextField(
                textFieldValue.value,
                onValueChange = { value -> textFieldValue.value = value },
                { Text("OutlineTextField") })
        }
    }
}

@Composable
private fun ProgressIndicators() {
    ScrollableRow {
        Sample("CircularProgressIndicator") {
            CircularProgressIndicator()
        }

        Sample("LinearProgressIndicator") {
            LinearProgressIndicator()
        }
    }
}

@Composable
private fun Selections() {
    ScrollableRow {
        Sample("Switch") {
            val state = remember { mutableStateOf(false) }
            Switch(checked = state.value, onCheckedChange = { state.value = it })
        }

        Sample("RadioButton") {
            val selectedRadio = remember { mutableStateOf(0) }
            Row {
                for (i in 0..3) {
                    RadioButton(selected = i == selectedRadio.value,
                        onClick = { selectedRadio.value = i })
                }
            }
        }

        Sample("CheckBox") {
            Row {
                val first = remember { mutableStateOf(ToggleableState.On) }
                Checkbox(checked = first.value == ToggleableState.On,
                    onCheckedChange = { first.value = first.value.next() })

                val second = remember { mutableStateOf(ToggleableState.Off) }
                Checkbox(checked = second.value == ToggleableState.On,
                    onCheckedChange = { second.value = second.value.next() })

                Checkbox(checked = true, onCheckedChange = {}, enabled = false)
            }
        }

        Sample("TriStateCheckbox") {
            val triState = remember { mutableStateOf(ToggleableState.Indeterminate) }
            TriStateCheckbox(state = triState.value,
                onClick = { triState.value = triState.value.next(tristate = true) })
        }
    }

    val sliderValue = remember { mutableStateOf(0F) }
    FullWidthSample("Slider") {
        Slider(
            value = sliderValue.value,
            onValueChange = { f -> sliderValue.value = f },
            valueRange = 0F..1F,
            steps = 10,
        )
    }
}

@Composable
private fun Lists() {
    FullWidthSample("ListItem") {
        ListItem(
            Modifier.clickable(onClick = DefaultOnClick),
            icon = { DefaultIcon() },
            overlineText = { Text("Overline Text") },
            secondaryText = { Text("Secondary Text") },
            trailing = { Text("Trailing") }
        ) {
            Text("Primary Text")
        }
    }
}

@Composable
private fun Tabs() {
    val selectedTab = remember { mutableStateOf(0) }
    val tabs = listOf("One", "Two", "Three")
    FullWidthSample("TabRow") {
        ScrollableTabRow(selectedTab.value) {
            tabs.forEachIndexed { i, text ->
                Tab(
                    selected = selectedTab.value == i,
                    onClick = { selectedTab.value = i },
                ) {
                    Text(text)
                }
            }
        }
    }
}

@Composable
private fun DefaultIcon() {
    Icon(Icons.Default.Android)
}

private val DefaultOnClick = {

}

private fun ToggleableState.next(tristate: Boolean = false): ToggleableState {
    return if (tristate) {
        when (this) {
            ToggleableState.Indeterminate -> ToggleableState.Off
            ToggleableState.On -> ToggleableState.Indeterminate
            ToggleableState.Off -> ToggleableState.On
        }
    }
    else ToggleableState(this == ToggleableState.Off)
}

