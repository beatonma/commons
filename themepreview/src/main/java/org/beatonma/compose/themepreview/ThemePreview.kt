package org.beatonma.compose.themepreview

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

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

/**
 * To preview your own theme:
 *
 *  @Composable
 *  @Preview
 *  fun MyThemePreview() {
 *      ThemePreview(theme = { isDark, content -> MyTheme(isDark, content) })
 *  }
 */
@Composable
fun ThemePreview(
    theme: @Composable (
        isDark: Boolean,
        content: @Composable () -> Unit,
    ) -> Unit,
) {
    val initIsDark = isSystemInDarkTheme()
    var isDark by savedInstanceState { initIsDark }
    val crossfadeAnimSpec = remember { TweenSpec<Float>(durationMillis = 1000) }
    val (currentScreen, onScreenChanged) = savedInstanceState { Screen.Typography }

    Crossfade(current = isDark, animation = crossfadeAnimSpec) {
        theme(isDark) {
            ThemePreview(currentScreen, onScreenChanged, onToggleTheme = { isDark = !isDark })
        }
    }
}

@Composable
private fun ThemePreview(
    currentScreen: Screen,
    onScreenChanged: (Screen) -> Unit,
    onToggleTheme: () -> Unit,
) {
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
                itemCount = 4,
                animSpec = animSpec,
            ) { animProgress ->
                Screen.values().forEach { screen ->
                    SectionIcon(
                        screen,
                        animProgress[screen.ordinal].value,
                        onScreenChanged
                    )
                }
                Spacer(Modifier)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onToggleTheme, shape = MaterialTheme.shapes.small) {
                Icon(Icons.Default.InvertColors)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
    )
}

@Composable
private fun BodyContent(content: @Composable () -> Unit) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.background) {
        content()
    }
}

@Composable
private fun Section(screen: Screen) {
    val scrollState = rememberScrollState(0F)
    val scrollProgress = scrollState.value / scrollState.maxValue

    Stack {
        ScrollableColumn(
            Modifier.fillMaxWidth(),
            scrollState = scrollState,
        ) {
            SectionHeader(screen.title, scrollProgress)
            screen.content()
            Spacer(Modifier.height(160.dp))
        }
    }
}

@Composable
private fun SectionHeader(text: String, progress: Float, modifier: Modifier = Modifier) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Text(
            text,
            Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.h3,
        )
    }
}
