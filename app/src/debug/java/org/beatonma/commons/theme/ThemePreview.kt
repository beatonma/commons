package org.beatonma.commons.theme

import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.theme.compose.theme.CommonsTheme

const val lorem =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
            "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
            "ullamco laboris nisi ut aliquip ex ea commodo consequat."

enum class Screen(val title: String, val icon: VectorAsset, val content: @Composable () -> Unit) {
    Typography("Typography", Icons.Default.TextFields, { TypographyPreview() }),
    Colors("Colors", Icons.Default.ColorLens, { ColorsPreview() }),
    Widgets("Widgets", Icons.Default.Widgets, { WidgetsPreview() }),
    ;
}

internal val BottomBarHeight = 56.dp

@Composable
@Preview
fun CommonsThemePreview() {
    CommonsTheme(darkTheme = isSystemInDarkTheme()) {
        ThemePreview()
    }
}

@Composable
fun ThemePreview() {
    val (currentScreen, onScreenChanged) = savedInstanceState { Screen.Typography }

    val animSpec = remember {
        SpringSpec<Float>(
            stiffness = 800f,
            dampingRatio = 0.8f,
        )
    }

    Scaffold(
        bodyContent = {
            BodyContent {
                Section(currentScreen)
            }
        },
        bottomBar = {
            BottomBar(
                selectedIndex = currentScreen.ordinal,
                itemCount = 3,
                animSpec = animSpec,
            ) { animProgress ->
                Screen.values().forEach { screen ->
                    SectionIcon(
                        screen,
                        animProgress[screen.ordinal].value,
                        onScreenChanged
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { TODO("Toggle dark theme") }) {
                Icon(Icons.Default.InvertColors)
            }
        }
    )
}

@Composable
private fun BodyContent(content: @Composable () -> Unit) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.background) {
        ScrollableColumn(
            Modifier.fillMaxWidth(),
        ) {
            content()
        }
    }
}

@Composable
private fun Section(screen: Screen) {
    SectionHeader(screen.title)
    screen.content()
    Spacer(Modifier.height(64.dp))
}

@Composable
private fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Text(
            text,
            modifier.padding(horizontal = 16.dp, vertical = 32.dp),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.h3,
        )
    }
}
