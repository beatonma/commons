package org.beatonma.commons.compose.components.button

import androidx.compose.foundation.BorderStroke
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
import org.beatonma.commons.themed.animation
import org.beatonma.commons.themed.buttons

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
    val border = BorderStroke(borderStroke, colors.onSurface.copy(alpha = .7f))
    val onClick = { onSelectChanged(!selected) }

    if (icon == null) {
        Button(
            onClick = onClick,
            shape = shape,
            modifier = modifier,
            colors = buttonColors,
            elevation = elevation,
            border = border,
        ) {
            content()
        }
    } else {
        ButtonWithIcon(
            onClick = onClick,
            modifier = modifier,
            icon = icon,
            content = content,
            shape = shape,
            colors = buttonColors,
            elevation = elevation,
            border = border,
        )
    }
}
