package org.beatonma.commons.app.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Dot(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp,
    shape: Shape = CircleShape,
) {
    Spacer(
        modifier
            .size(size)
            .background(color, shape)
    )
}
