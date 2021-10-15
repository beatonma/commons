package org.beatonma.commons.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.themed.ThemedElevation

object CommonsElevation : ThemedElevation {
    override val ModalSurface = 6.dp
    override val Toolbar: Dp = 8.dp
    override val Card = 4.dp
}
