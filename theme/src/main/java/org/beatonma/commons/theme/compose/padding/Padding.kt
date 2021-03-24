package org.beatonma.commons.theme.compose.padding

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*


fun paddingValues(
    all: Dp = 0.dp,
    horizontal: Dp = all,
    vertical: Dp = all,
    start: Dp = horizontal,
    top: Dp = vertical,
    end: Dp = horizontal,
    bottom: Dp = vertical,
) = Padding(start, top, end, bottom)

class Padding(
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp,
) {
    companion object {
        val Zero = paddingValues(0.dp)
        val Card = paddingValues(all = 12.dp, bottom = 16.dp)

        // Standard inset from screen edges
        val ScreenHorizontal = paddingValues(horizontal = 12.dp)
        val Screen = paddingValues(16.dp)

        val SearchBar = Screen

        val Spacer = paddingValues(bottom = 16.dp)

        val VerticalListItem = paddingValues(bottom = 8.dp)
        val VerticalListItemLarge = paddingValues(top = 8.dp, bottom = 8.dp)
        val HorizontalListItem = paddingValues(end = 12.dp)

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
        val EndOfContentHorizontal = paddingValues(end = 220.dp)
    }

    operator fun plus(other: Padding) = Padding(
        start = start + other.start,
        top = top + other.top,
        end = end + other.end,
        bottom = bottom + other.bottom
    )

    fun asPaddingValues() = PaddingValues(start, top, end, bottom)

    override fun hashCode(): Int {
        return Objects.hash(start, top, end, bottom)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Padding) {
            return start == other.start
                && top == other.top
                && end == other.end
                && bottom == other.bottom
        }
        return false
    }

    override fun toString(): String {
        return "Padding(start=$start, top=$top, end=$end, bottom=$bottom)"
    }
}

fun Modifier.padding(padding: Padding): Modifier = this.then(
    padding(padding.asPaddingValues())
)
