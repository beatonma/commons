package org.beatonma.commons.theme

import androidx.compose.ui.unit.dp
import org.beatonma.commons.themed.ThemedPadding
import org.beatonma.commons.themed.paddingValues


internal object CommonsPadding : ThemedPadding {
    override val Zero = paddingValues(0.dp)
    override val Card = paddingValues(all = 12.dp, bottom = 16.dp)

    // Standard inset from screen edges
    override val ScreenHorizontal = paddingValues(horizontal = 12.dp)
    override val Screen = paddingValues(16.dp, horizontal = 12.dp)

    override val Spacer = paddingValues(bottom = 16.dp)

    override val VerticalListItem = paddingValues(bottom = 8.dp)
    override val VerticalListItemLarge = paddingValues(vertical = 8.dp)
    override val HorizontalListItem = paddingValues(end = 8.dp)

    override val HorizontalSeparator = paddingValues(vertical = 8.dp)
    override val VerticalSeparator = paddingValues(horizontal = 8.dp)

    override val IconSmall = paddingValues(8.dp)
    override val IconLarge = paddingValues(16.dp)

    override val Tag = paddingValues(horizontal = 8.dp, vertical = 4.dp)

    override val Image = paddingValues(8.dp)

    override val Fab = Screen
    override val FabContent = paddingValues(16.dp)
    override val ExtendedFabContent = paddingValues(horizontal = 20.dp, vertical = 16.dp)

    override val Snackbar = paddingValues(16.dp)

    override val CardButton = paddingValues(top = 24.dp)
    override val EndOfContent = paddingValues(bottom = 220.dp)
    override val EndOfContentHorizontal = paddingValues(end = 220.dp)
}
