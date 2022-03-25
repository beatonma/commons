package org.beatonma.commons.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.themed.padding
import org.beatonma.commons.themed.size


@Composable
fun ListItem(
    text: String,
    modifier: Modifier = Modifier,
    secondaryText: String? = null,
    overlineText: String? = null,
    underlineText: String? = null,
    trailing: String? = null,
    icon: (@Composable () -> Unit)? = null,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    ListItem(
        text = { Text(text) },
        modifier = modifier,
        secondaryText = secondaryText?.let { { Text(secondaryText) } },
        overlineText = overlineText?.let { { Text(overlineText) } },
        underlineText = underlineText?.let { { Text(underlineText) } },
        trailing = trailing?.let { { Text(trailing) } },
        icon = icon,
        alignment = alignment,
    )
}

@Composable
fun ListItem(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    secondaryText: (@Composable () -> Unit)? = null,
    overlineText: (@Composable () -> Unit)? = null,
    underlineText: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    alignment: Alignment.Vertical = Alignment.CenterVertically,
) {
    CompositionLocalProvider(
        LocalContentColor provides colors.onSurface,
    ) {
        Row(
            modifier
                .background(colors.surface)
                .padding(padding.ScreenHorizontal)
                .padding(vertical = 8.dp),
            verticalAlignment = alignment,
        ) {
            if (icon != null) {
                Box(
                    Modifier
                        .size(size.IconLarge)
                        .padding(end = 16.dp)
                        .clip(shapes.small)
                ) {
                    icon()
                }
            }

            ReverseRow {
                Column(Modifier.fillMaxWidth()) {
                    if (overlineText != null) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.medium,
                            LocalTextStyle provides typography.overline,
                        ) {
                            overlineText()
                        }
                    }

                    text()

                    if (secondaryText != null) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.medium,
                            LocalTextStyle provides typography.body2,
                        ) {
                            secondaryText()
                        }
                    }

                    if (underlineText != null) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.medium,
                            LocalTextStyle provides typography.caption,
                        ) {
                            underlineText()
                        }
                    }
                }

                if (trailing != null) {
                    Box(Modifier.padding(start = 8.dp)) {
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.medium,
                            LocalTextStyle provides typography.caption,
                        ) {
                            trailing()
                        }
                    }
                }
            }
        }
    }
}
