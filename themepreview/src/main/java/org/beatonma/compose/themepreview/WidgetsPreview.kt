package org.beatonma.compose.themepreview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Slider
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.Switch
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TriStateCheckbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.input.TextFieldValue

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
                    Text("Action", color = SnackbarDefaults.primaryActionColor)
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
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Column(
        columnModifier.padding(ContentPadding),
    ) {
        Text(
            name,
            Modifier.padding(vertical = ContentPadding),
            style = MaterialTheme.typography.caption
        )
        content(modifier)
    }
}


private fun LazyListScope.SampleItem(
    name: String,
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    item {
        Sample(name, modifier, columnModifier, content)
    }
}

@Composable
private fun Actions() {
    LazyRow {
        SampleItem("Button") {
            Button(onClick = DefaultOnClick) {
                Text("Button")
            }
        }
        SampleItem("OutlinedButton") {
            OutlinedButton(onClick = DefaultOnClick) {
                Text("Button")
            }
        }
        SampleItem("TextButton") {
            TextButton(onClick = DefaultOnClick) {
                Text("Button")
            }
        }
        SampleItem("IconButton") {
            IconButton(onClick = DefaultOnClick) {
                DefaultIcon()
            }
        }
        SampleItem("FloatingActionButton") {
            FloatingActionButton(onClick = DefaultOnClick) {
                DefaultIcon()
            }
        }
    }
}

@Composable
private fun TextFields() {
    val textFieldValue = remember { mutableStateOf("") }
    LazyRow {
        SampleItem("TextField") {
            TextField(
                textFieldValue.value,
                onValueChange = { value -> textFieldValue.value = value },
                label = { Text("TextField") },
            )
        }

        SampleItem("OutlinedTextField") {
            OutlinedTextField(
                textFieldValue.value,
                onValueChange = { value -> textFieldValue.value = value },
                label = { Text("OutlineTextField") })
        }
    }
}

@Composable
private fun ProgressIndicators() {
    LazyRow {
        SampleItem("CircularProgressIndicator") {
            CircularProgressIndicator()
        }

        SampleItem("LinearProgressIndicator") {
            LinearProgressIndicator()
        }
    }
}

@Composable
private fun Selections() {
    LazyRow {
        SampleItem("Switch") {
            val state = remember { mutableStateOf(false) }
            Switch(checked = state.value, onCheckedChange = { state.value = it })
        }

        SampleItem("RadioButton") {
            val selectedRadio = remember { mutableStateOf(0) }
            Row {
                for (i in 0..3) {
                    RadioButton(selected = i == selectedRadio.value,
                        onClick = { selectedRadio.value = i })
                }
            }
        }

        SampleItem("CheckBox") {
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

        SampleItem("TriStateCheckbox") {
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

@OptIn(ExperimentalMaterialApi::class)
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
    Icon(Icons.Default.Android, contentDescription = "Default icon")
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

