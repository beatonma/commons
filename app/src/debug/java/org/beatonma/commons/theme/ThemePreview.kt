package org.beatonma.commons.theme

import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.compose.themepreview.ThemePreview

@Composable
@Preview
fun CommonsThemePreview() {
    ThemePreview(theme = { isDark, content -> CommonsTheme(isDark, content) })
}
