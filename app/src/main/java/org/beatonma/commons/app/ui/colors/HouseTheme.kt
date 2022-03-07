package org.beatonma.commons.app.ui.colors

import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.core.House
import org.beatonma.commons.theme.house
import org.beatonma.commons.theme.textPrimaryLight


@Composable
fun houseTheme(
    surface: Color,
    onSurface: Color = colors.textPrimaryLight
) = SurfaceTheme(surface, onSurface)

@Composable
fun House.theme(): SurfaceTheme = when(this) {
    House.commons -> houseTheme(colors.house.Commons)
    House.lords -> houseTheme(colors.house.Lords)
    House.unassigned -> houseTheme(colors.house.Parliament)
}
