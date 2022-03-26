package org.beatonma.commons.compose.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.themed.buttons
import org.beatonma.commons.themed.padding

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = shapes.small,
    colors: ButtonColors = buttons.surfaceButtonColors(),
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    border: BorderStroke? = null,
) {
    Button(
        shape = shape,
        modifier = modifier,
        colors = colors,
        elevation = elevation,
        border = border,
        onClick = onClick,
    ) {
        Box(
            Modifier.padding(padding.HorizontalListItem)
        ) {
            icon()
        }

        content()
    }
}
