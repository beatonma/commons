package org.beatonma.commons.compose.components

import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.beatonma.commons.theme.compose.Padding

private const val ALPHA = 0.1F

@Composable
fun HorizontalSeparator(
    color: Color = AmbientContentColor.current.copy(alpha = ALPHA),
    modifier: Modifier = Modifier,
) {

    Box(
        Modifier.fillMaxWidth(),
        alignment = Alignment.Center,
    ) {
        Separator(
            color = color,
            modifier = Modifier
                .padding(Padding.HorizontalSeparator)
                .preferredHeight(1.dp)
                .fillMaxWidth(0.25F)
                .then(modifier)
        )
    }
}

@Composable
fun VerticalSeparator(
    color: Color = AmbientContentColor.current.copy(alpha = ALPHA),
    modifier: Modifier = Modifier,
) {
    Box(
        Modifier.fillMaxHeight(),
        alignment = Alignment.Center,
    ) {
        Separator(
            color = color,
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(0.6F)
                .padding(Padding.VerticalSeparator)
                .then(modifier)
        )
    }
}

@Composable
private fun Separator(color: Color, modifier: Modifier = Modifier) {
    Spacer(modifier.background(color))
}
