package org.beatonma.compose.themepreview

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
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

internal const val lorem =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
            "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
            "ullamco laboris nisi ut aliquip ex ea commodo consequat."

internal val ContentPadding = 16.dp

interface BaseScreen {

    val title: String
    val icon: VectorAsset
    val content: @Composable () -> Unit
}

internal enum class Screen(
    override val title: String,
    override val icon: VectorAsset,
    override val content: @Composable () -> Unit,
) : BaseScreen {

    Typography("Typography", Icons.Default.TextFields, { TypographyPreview() }),
    Colors("Colors", Icons.Default.ColorLens, { ColorsPreview() }),
    Widgets("Widgets", Icons.Default.Widgets, { WidgetsPreview() }),
    Custom("Custom", Icons.Default.Android, { DefaultCustomPreview() }),
    ;
}

data class CustomPreviewScreen(
    override val title: String = "Custom app content",
    override val icon: VectorAsset = Icons.Default.Android,
    override val content: @Composable () -> Unit,
) : BaseScreen

/**
 * To preview your own theme:
 *
 *  @Composable
 *  @Preview
 *  fun MyThemePreview() {
 *      ThemePreview(
 *          theme = { isDark, content -> MyTheme(isDark, content) },
 *          customScreen = CustomPreviewScreen {
 *              // Custom @Composable content here, if you want!
 *          },
 *      )
 *  }
 */
@Composable
fun ThemePreview(
    theme: @Composable (
        isDark: Boolean,
        content: @Composable () -> Unit,
    ) -> Unit,

    customScreen: CustomPreviewScreen? = null,
) {
    val initIsDark = isSystemInDarkTheme()
    var isDark by savedInstanceState { initIsDark }
    val crossfadeAnimSpec = remember { TweenSpec<Float>(durationMillis = 1000) }
    val (currentScreen, onScreenChanged) = savedInstanceState { Screen.Custom }

    val screenOverride =
        if (currentScreen == Screen.Custom && customScreen != null) {
            customScreen
        }
        else null

    Crossfade(current = isDark, animation = crossfadeAnimSpec) {
        theme(isDark) {
            ThemePreview(currentScreen,
                screenOverride,
                onScreenChanged,
                onToggleTheme = { isDark = !isDark })
        }
    }
}

@Composable
private fun ThemePreview(
    currentScreen: Screen,
    screenOverride: BaseScreen?,
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
                Section(screenOverride ?: currentScreen)
            }
        },
        bottomBar = {
            BottomBar(
                selectedIndex = currentScreen.ordinal,
                itemCount = Screen.values().size + 1, // Allow space for FAB
                animSpec = animSpec,
            ) { animProgress ->
                Screen.values().forEach { screen ->
                    SectionIcon(
                        screen,
                        animProgress[screen.ordinal].value,
                        onScreenChanged
                    )
                }
                Spacer(Modifier)  // Create space for the FAB
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onToggleTheme, shape = shapes.small) {
                Icon(Icons.Default.InvertColors)
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
    )
}

@Composable
private fun BodyContent(content: @Composable () -> Unit) {
    Surface(Modifier.fillMaxWidth(), color = colors.background) {
        content()
    }
}

@Composable
private fun Section(screen: BaseScreen) {
    val scrollState = rememberScrollState()
    val scrollProgress = scrollState.value / scrollState.maxValue

    ScrollableColumn(
        Modifier.fillMaxWidth(),
        scrollState = scrollState,
    ) {
        SectionHeader(screen.title, scrollProgress)
        screen.content()
        Spacer(Modifier.height(160.dp))
    }
}

@Composable
private fun SectionHeader(text: String, progress: Float, modifier: Modifier = Modifier) {
    Surface(Modifier.fillMaxWidth(), color = colors.primary) {
        Text(
            text,
            Modifier.padding(horizontal = ContentPadding, vertical = 32.dp),
            fontFamily = FontFamily.Monospace,
            style = typography.h3,
        )
    }
}
