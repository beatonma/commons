package org.beatonma.commons.compose.components.fabbottomsheet

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding


@Composable
fun FabText(
    text: String,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Text(
        text,
        modifier
            .padding(Padding.ExtendedFabContent)
            .alpha(
                progress
                    .reversed()
                    .progressIn(0.8F, 1.0F)
            ),
        style = typography.button,
    )
}
