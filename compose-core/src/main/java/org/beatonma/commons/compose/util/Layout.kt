package org.beatonma.commons.compose.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

val Placeable.size get() = IntSize(width, height)

fun Dp.pxF(context: Context): Float =
    when (this) {
        Dp.Hairline -> 1F
        else -> (value * context.resources.displayMetrics.density)
    }

val Dp.pxF: Float
    @Composable get() = pxF(LocalContext.current)

fun Dp.px(context: Context): Int =
    when (this) {
        Dp.Hairline -> 1
        else -> (value * context.resources.displayMetrics.density)
            .roundToInt()
    }

val Dp.px: Int
    @Composable get() = px(LocalContext.current)
