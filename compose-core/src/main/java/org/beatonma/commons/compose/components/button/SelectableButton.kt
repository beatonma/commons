package org.beatonma.commons.compose.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.themed.animation
import org.beatonma.commons.themed.buttons
import org.beatonma.commons.themed.padding

@Composable
fun SelectableButton(
    selected: Boolean,
    onSelectChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = shapes.small,
    selectedColors: ButtonColors = buttons.accentButtonColors(),
    unselectedColors: ButtonColors = buttons.outlineButtonColors(),
    icon: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val backgroundColor by animation.animateColorAsState(targetValue = (if (selected) selectedColors.backgroundColor(
        true) else unselectedColors.backgroundColor(true)).value)
    val contentColor by animation.animateColorAsState(targetValue = (if (selected) selectedColors.contentColor(
        true) else unselectedColors.contentColor(true)).value)

    val buttonColors = buttons.buttonColors(
        contentColor = contentColor,
        backgroundColor = backgroundColor,
    )

    val borderStroke by animation.animateDpAsState(if (selected) 0.dp else 2.dp)
    val elevation = ButtonDefaults.elevation(if (selected) 2.dp else 0.dp)

    Button(
        shape = shape,
        modifier = modifier,
        colors = buttonColors,
        elevation = elevation,
        border = BorderStroke(borderStroke, colors.onSurface.copy(alpha = .7f)),
        onClick = { onSelectChanged(!selected) },
    ) {
        if (icon != null) {
            Box(
                Modifier.padding(padding.HorizontalListItem)
            ) {
                icon()
            }
        }
        content()
    }
}
