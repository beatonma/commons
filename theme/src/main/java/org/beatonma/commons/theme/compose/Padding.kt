package org.beatonma.commons.theme.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Padding {
    val Card = PaddingValues(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 16.dp)

    // Standard inset from screen edges
    val ScreenHorizontal = PaddingValues(start = 12.dp, end = 12.dp)
    val Screen = PaddingValues(16.dp)

    val VerticalList = PaddingValues()
    val VerticalListItem = PaddingValues(bottom = 8.dp)
    val HorizontalListItem = PaddingValues(end = 12.dp)

    val HorizontalSeparator = PaddingValues(vertical = 8.dp)
    val VerticalSeparator = PaddingValues(horizontal = 8.dp)

    // For rows of Weblink, EmailLink, PhoneLink
    val LinkItem = PaddingValues(end = 4.dp)
    val Links = PaddingValues(vertical = 8.dp)

    val IconSmall = PaddingValues(8.dp)
    val IconLarge = PaddingValues(16.dp)

    val Image = PaddingValues(8.dp)

    val EndOfContent = PaddingValues(bottom = 160.dp)
}

fun Modifier.endOfContent() = padding(Padding.EndOfContent)
inline fun PaddingValues(
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
    bottom = bottom + other.bottom
)

/**
 * When animating padding values, replace .dp with .pdp (paddingDp) to ensure non-negative values.
 */
val Float.pdp: Dp get() = this.coerceAtLeast(0F).dp
