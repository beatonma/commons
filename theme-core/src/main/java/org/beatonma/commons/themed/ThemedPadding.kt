package org.beatonma.commons.themed

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Objects

val LocalPadding =
    staticCompositionLocalOf<ThemedPadding> { error("LocalPadding has not been provided") }

inline val padding: ThemedPadding
    @ReadOnlyComposable @Composable get() = LocalPadding.current


interface ThemedPadding {
    val Zero: Padding
    val Card: Padding

    // Standard inset from screen edges
    val ScreenHorizontal: Padding
    val Screen: Padding

    val Spacer: Padding

    val VerticalListItem: Padding
    val VerticalListItemLarge: Padding
    val HorizontalListItem: Padding

    val HorizontalSeparator: Padding
    val VerticalSeparator: Padding

    val IconSmall: Padding
    val IconLarge: Padding

    val Tag: Padding

    val Image: Padding

    val Fab: Padding
    val FabContent: Padding
    val ExtendedFabContent: Padding

    val Snackbar: Padding

    val CardButton: Padding
    val EndOfContent: Padding
    val EndOfContentHorizontal: Padding
}

class Padding(
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp,
) {
    operator fun plus(other: Padding) = Padding(
        start = start + other.start,
        top = top + other.top,
        end = end + other.end,
        bottom = bottom + other.bottom
    )

    fun copy(
        start: Dp? = null,
        top: Dp? = null,
        end: Dp? = null,
        bottom: Dp? = null,
    ): Padding = Padding(
        start = start ?: this.start,
        top = top ?: this.top,
        end = end ?: this.end,
        bottom = bottom ?: this.bottom,
    )

    fun asPaddingValues() = PaddingValues(start, top, end, bottom)

    operator fun times(scale: Float) = Padding(
        start = start * scale,
        top = top * scale,
        end = end * scale,
        bottom = bottom * scale
    )

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

fun paddingValues(
    all: Dp = 0.dp,
    horizontal: Dp = all,
    vertical: Dp = all,
    start: Dp = horizontal,
    top: Dp = vertical,
    end: Dp = horizontal,
    bottom: Dp = vertical,
) = Padding(start, top, end, bottom)
