/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE", "unused")

package org.beatonma.commons.compose.systemui

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.offset
import com.google.accompanist.insets.Insets
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.imePadding


/**
 * Selectively apply additional space which matches the width/height of any system bars present
 * on the respective edges of the screen.
 *
 * @param enabled Whether to apply padding using the system bars dimensions on the respective edges.
 * Defaults to `true`.
 */
fun Modifier.systemBarsPadding(enabled: Boolean = true, scale: Float = 1F) = composed {
    InsetsPaddingModifier(
        insets = LocalWindowInsets.current.systemBars * scale,
        applyLeft = enabled,
        applyTop = enabled,
        applyRight = enabled,
        applyBottom = enabled,
    )
}

/**
 * Apply additional space which matches the height of the status bars height along the top edge
 * of the content.
 */
fun Modifier.statusBarsPadding(scale: Float = 1F) = composed {
    InsetsPaddingModifier(
        insets = LocalWindowInsets.current.statusBars * scale,
        applyTop = true,
    )
}

/**
 * Apply additional space which matches the height of the navigation bars height
 * along the [bottom] edge of the content, and additional space which matches the width of
 * the navigation bars on the respective [left] and [right] edges.
 *
 * @param bottom Whether to apply padding to the bottom edge, which matches the navigation bars
 * height (if present) at the bottom edge of the screen. Defaults to `true`.
 * @param left Whether to apply padding to the left edge, which matches the navigation bars width
 * (if present) on the left edge of the screen. Defaults to `true`.
 * @param right Whether to apply padding to the right edge, which matches the navigation bars width
 * (if present) on the right edge of the screen. Defaults to `true`.
 */
fun Modifier.navigationBarsPadding(
    bottom: Boolean = true,
    left: Boolean = true,
    right: Boolean = true,
    scale: Float = 1F,
) = composed {
    InsetsPaddingModifier(
        insets = LocalWindowInsets.current.navigationBars * scale,
        applyLeft = left,
        applyRight = right,
        applyBottom = bottom,
    )
}

//fun Modifier.imePadding(scale: Float = 1F) = composed {
//    InsetsPaddingModifier(
//        insets = LocalWindowInsets.current.ime * scale,
//        applyBottom = true,
//    )
//}

///**
// * Scaling is applied only to navigationBars padding.
// */
//fun Modifier.imeOrNavigationBarsPadding(
//    bottom: Boolean = true,
//    left: Boolean = true,
//    right: Boolean = true,
//    scale: Float = 1F,
//) = composed {
//    println("IME ${LocalWindowInsets.current.ime.toPaddingValues()}")
//    val insets = LocalWindowInsets.current.ime
//        .coerceAtLeast(LocalWindowInsets.current.navigationBars * scale)
//
//    InsetsPaddingModifier(
//        insets,
//        applyLeft = left,
//        applyRight = right,
//        applyBottom = bottom,
//    )
//}
fun Modifier.imeOrNavigationBarsPadding(
    bottom: Boolean = true,
    left: Boolean = true,
    right: Boolean = true,
    scale: Float = 1F
) = navigationBarsPadding(bottom, left, right, scale)
    .imePadding()

fun Insets.coerceAtLeast(
    other: Insets,
): Insets {
    if (left >= other.left && top >= other.top && right >= other.right && bottom >= other.bottom) {
        return this
    }

    return copy(
        left = left.coerceAtLeast(other.left),
        top = top.coerceAtLeast(other.top),
        right = right.coerceAtLeast(other.right),
        bottom = bottom.coerceAtLeast(other.bottom)
    )
}

operator fun Insets.times(scale: Float): Insets {
    if (scale == 1F) return this
    return this.copy(
        left = (left.toFloat() * scale).toInt(),
        top = (top.toFloat() * scale).toInt(),
        right = (right.toFloat() * scale).toInt(),
        bottom = (bottom.toFloat() * scale).toInt(),
    )
}

private data class InsetsPaddingModifier(
    private val insets: Insets,
    private val applyLeft: Boolean = false,
    private val applyTop: Boolean = false,
    private val applyRight: Boolean = false,
    private val applyBottom: Boolean = false,
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {

        val left = if (applyLeft) insets.left else 0
        val top = if (applyTop) insets.top else 0
        val right = if (applyRight) insets.right else 0
        val bottom = if (applyBottom) insets.bottom else 0
        val horizontal = left + right
        val vertical = top + bottom

        val placeable = measurable.measure(
            constraints.offset(-horizontal, -vertical)
        )

        val width = (placeable.width + horizontal)
            .coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = (placeable.height + vertical)
            .coerceIn(constraints.minHeight, constraints.maxHeight)

        return layout(width, height) {
            placeable.place(left, top)
        }
    }
}
