package org.beatonma.commons.theme.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

object Padding {
    val Zero = paddingValues(0.dp)
    val Card = paddingValues(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 16.dp)

    // Standard inset from screen edges
    val ScreenHorizontal = paddingValues(start = 12.dp, end = 12.dp)
    val Screen = paddingValues(16.dp)

    val Spacer = paddingValues(bottom = 16.dp)

    val VerticalListItem = paddingValues(bottom = 8.dp)
    val VerticalListItemLarge = paddingValues(top = 8.dp, bottom = 8.dp)
    val HorizontalListItem = paddingValues(end = 12.dp)
    val GridListItem = VerticalListItem + HorizontalListItem

    val HorizontalSeparator = paddingValues(vertical = 8.dp)
    val VerticalSeparator = paddingValues(horizontal = 8.dp)

    // For rows of Weblink, EmailLink, PhoneLink
    val LinkItem = paddingValues(end = 4.dp)
    val Links = paddingValues(vertical = 8.dp)

    val IconSmall = paddingValues(8.dp)
    val IconLarge = paddingValues(16.dp)

    val Tag = paddingValues(horizontal = 8.dp, vertical = 4.dp)

    val Image = paddingValues(8.dp)

    val Fab = Screen
    val FabContent = paddingValues(16.dp)
    val ExtendedFabContent = paddingValues(horizontal = 20.dp, vertical = 16.dp)

    val Snackbar = paddingValues(16.dp)

    val CardButton = paddingValues(top = 24.dp)
    val EndOfContent = paddingValues(bottom = 160.dp)
}


@Suppress("NOTHING_TO_INLINE")
private inline fun paddingValues(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
    start: Dp = horizontal,
    top: Dp = vertical,
    end: Dp = horizontal,
    bottom: Dp = vertical,
) = PaddingValues(start, top, end, bottom)

operator fun PaddingValues.plus(other: PaddingValues) = PaddingValues(
    start = start + other.start,
    top = top + other.top,
    end = end + other.end,
    bottom = bottom + other.bottom,
)

/**
 * Choose the largest values for start/top/end/bottom from the provided values.
 */
fun maxPaddingOf(first: PaddingValues, second: PaddingValues) = PaddingValues(
    start = max(first.start, second.start),
    top = max(first.top, second.top),
    end = max(first.end, second.end),
    bottom = max(first.bottom, second.bottom),
)

/**
 * Replace .dp with .pdp (paddingDp) to ensure non-negative values. Useful for animating padding values.
 */
val Float.pdp: Dp get() = this.coerceAtLeast(0F).dp

/**
 * Replace .dp with .pdp (paddingDp) to ensure non-negative values. Useful for animating padding values.
 */
val Int.pdp: Dp get() = this.coerceAtLeast(0).dp

@Composable
fun EndOfContent() = Spacer(Modifier.padding(Padding.EndOfContent))
fun Modifier.endOfContent() = padding(Padding.EndOfContent)
