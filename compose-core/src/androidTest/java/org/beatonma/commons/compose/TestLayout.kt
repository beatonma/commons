package org.beatonma.commons.compose

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.RepeatableSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import org.beatonma.commons.themed.LocalAnimationSpec
import org.beatonma.commons.themed.LocalButtonTheme
import org.beatonma.commons.themed.LocalElevation
import org.beatonma.commons.themed.LocalPadding
import org.beatonma.commons.themed.LocalSize
import org.beatonma.commons.themed.LocalSpanStyle
import org.beatonma.commons.themed.Padding
import org.beatonma.commons.themed.ThemedAnimation
import org.beatonma.commons.themed.ThemedButtons
import org.beatonma.commons.themed.ThemedElevation
import org.beatonma.commons.themed.ThemedPadding
import org.beatonma.commons.themed.ThemedSize
import org.beatonma.commons.themed.ThemedSpanStyle
import org.beatonma.commons.themed.paddingValues

/**
 * Base layout for all UI tests, providing all expected Local values such as window insets and
 * theme values that are expected by composables in the library.
 *
 * Any test-specific Local values may be provided as varargs without further nested
 * CompositionLocalProvider blocks.
 */
@Composable
internal fun TestLayout(
    vararg providedValues: ProvidedValue<*>,
    content: @Composable () -> Unit
) {
    MaterialTheme {
        ProvideWindowInsets {
            CompositionLocalProvider(
                LocalAnimationSpec provides TestAnimation,
                LocalButtonTheme provides TestButtons,
                LocalElevation provides TestElevation,
                LocalPadding provides TestPadding,
                LocalSize provides TestSize,
                LocalSpanStyle provides TestSpanStyle,
                *providedValues,
                content = content,
            )
        }
    }
}

private object TestAnimation : ThemedAnimation {
    override val itemDelay: Long = 10

    override fun <T> spec(): FiniteAnimationSpec<T> = spring()
    override fun <T> fast(): FiniteAnimationSpec<T> = tween(50)
    override fun <T> spring(): SpringSpec<T> = androidx.compose.animation.core.spring()
    override fun <T> tween(duration: Int): TweenSpec<T> = androidx.compose.animation.core.tween(100)

    override fun <T> repeatable(
        iterations: Int,
        animation: DurationBasedAnimationSpec<T>,
        repeatMode: RepeatMode
    ): RepeatableSpec<T> = RepeatableSpec(iterations, animation, repeatMode)

    override fun <T> infiniteRepeatable(
        duration: Int,
        repeatMode: RepeatMode
    ): InfiniteRepeatableSpec<T> = InfiniteRepeatableSpec(tween(duration), repeatMode)
}

private object TestButtons : ThemedButtons {
    @Composable
    override fun outlineButtonColors(): ButtonColors =
        buttonColors(
            Color.White,
            Color.Black,
            Color.Red,
        )

    @Composable
    override fun warningButtonColors(): ButtonColors =
        buttonColors(
            Color.White,
            Color.Red,
            Color.Gray,
        )

    @Composable
    override fun contentButtonColors(): ButtonColors =
        buttonColors(
            Color.Black,
            Color.White,
            Color.Green,
        )

    @Composable
    override fun buttonColors(
        contentColor: Color,
        backgroundColor: Color,
        disabledContentColor: Color
    ): ButtonColors =
        ButtonDefaults.buttonColors(
            contentColor = Color.White,
            backgroundColor = Color.Black,
            disabledContentColor = Color.Red,
        )
}

private object TestElevation : ThemedElevation {
    override val Card: Dp = 4.dp
    override val Toolbar: Dp = 6.dp
    override val ModalSurface: Dp = 32.dp
}

private object TestPadding : ThemedPadding {
    override val Zero: Padding = paddingValues()
    override val Card: Padding = paddingValues()
    override val ScreenHorizontal: Padding = paddingValues()
    override val Screen: Padding = paddingValues()
    override val Spacer: Padding = paddingValues()
    override val VerticalListItem: Padding = paddingValues()
    override val VerticalListItemLarge: Padding = paddingValues()
    override val HorizontalListItem: Padding = paddingValues()
    override val HorizontalSeparator: Padding = paddingValues()
    override val VerticalSeparator: Padding = paddingValues()
    override val IconSmall: Padding = paddingValues()
    override val IconLarge: Padding = paddingValues()
    override val Tag: Padding = paddingValues()
    override val Image: Padding = paddingValues()
    override val Fab: Padding = paddingValues()
    override val FabContent: Padding = paddingValues()
    override val ExtendedFabContent: Padding = paddingValues()
    override val Snackbar: Padding = paddingValues()
    override val CardButton: Padding = paddingValues()
    override val EndOfContent: Padding = paddingValues()
    override val EndOfContentHorizontal: Padding = paddingValues()
}

private object TestSize : ThemedSize {
    override val IconButton: Dp = 64.dp
    override val IconSmall: Dp = 18.dp
    override val IconLarge: Dp = 32.dp
}

private object TestSpanStyle : ThemedSpanStyle {
    override val italic: SpanStyle @Composable get() = SpanStyle(fontStyle = FontStyle.Italic)
    override val bold: SpanStyle @Composable get() = SpanStyle(fontWeight = FontWeight.Bold)
    override val quote: SpanStyle @Composable get() = SpanStyle(fontWeight = FontWeight.Light)
    override val hyperlink: SpanStyle @Composable get() = SpanStyle(color = Color.Blue)
}
