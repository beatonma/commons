package org.beatonma.commons.app.social

import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import org.beatonma.commons.compose.ambient.colors

@Composable
fun Username(
    username: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AmbientTextStyle.current,
) {
    Text(
        username,
        style = style,
        color = colors.primary,
        modifier = modifier
    )
}
