package org.beatonma.commons.app.social.compose

import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import org.beatonma.commons.compose.ambient.colors

@Composable
fun Username(
    username: String,
    style: TextStyle = AmbientTextStyle.current,
    modifier: Modifier = Modifier,
) {
    Text(
        username,
        style = style,
        color = colors.primary,
        modifier = modifier
    )
}
