package org.beatonma.commons.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.themed.Padding
import org.beatonma.commons.themed.themedElevation
import org.beatonma.commons.themed.themedPadding

@Composable
fun Card(
    modifier: Modifier = Modifier,
    shape: Shape = shapes.medium,
    backgroundColor: Color = colors.surface,
    contentColor: Color = colors.onSurface,
    border: BorderStroke? = null,
    elevation: Dp = themedElevation.Card,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(bottom = elevation)
            .then(modifier)
            .shadow(elevation, shape),
        shape = shape,
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        border = border,
        content = content
    )
}

@Composable
inline fun CardText(
    modifier: Modifier = Modifier,
    padding: Padding = themedPadding.Card,
    crossinline content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.padding(padding), content = content)
}

@Composable
inline fun BottomCardText(
    modifier: Modifier = Modifier,
    crossinline content: @Composable BoxScope.() -> Unit,
) {
    CardText(modifier, themedPadding.Card.copy(bottom = 0.dp), content)
}
