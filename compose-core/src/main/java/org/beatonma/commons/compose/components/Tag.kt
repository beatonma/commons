package org.beatonma.commons.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.themed.padding

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = colors.primary,
    contentColor: Color = colors.onPrimary,
) {
    Tag(modifier, color, contentColor) {
        Text(text)
    }
}

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    color: Color = colors.primary,
    contentColor: Color = colors.onPrimary,
    content: @Composable BoxScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalTextStyle provides typography.caption,
    ) {
        Box(
            modifier
                .padding(padding.Tag)
                .clip(shapes.small)
                .background(color)
                .padding(padding.Tag),
            content = content,
        )
    }
}
