@file:Suppress("NOTHING_TO_INLINE")

package org.beatonma.commons.compose.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import org.beatonma.commons.compose.util.ModifierBlock
import org.beatonma.commons.compose.util.TypedModifierBlock
import org.beatonma.commons.core.extensions.fastForEach

inline fun <T> Modifier.forEachOf(
    items: List<T>,
    crossinline block: TypedModifierBlock<T>,
): Modifier = composed {
    this.apply {
        items.fastForEach { item -> block(item) }
    }
}

inline fun <T> Modifier.forEachOf(
    items: Array<T>,
    crossinline block: TypedModifierBlock<T>,
): Modifier = composed {
    this.apply {
        items.forEach { item -> block(item) }
    }
}

/**
 * Conditional modifier only applied when condition is met./
 */
inline fun Modifier.onlyWhen(condition: Boolean, crossinline content: ModifierBlock): Modifier =
    composed {
        if (condition) this.content()
        else this
    }

inline fun Modifier.either(
    condition: Boolean,
    crossinline whenTrue: ModifierBlock,
    crossinline whenFalse: ModifierBlock,
): Modifier = composed {
    if (condition) this.whenTrue() else this.whenFalse()
}

/**
 * Inline Modifier version of when(value) {...} using lambdas for complex cases. If you only require
 * equality checks, use [Modifier.switchEqual] instead.
 */
inline fun <T> Modifier.switch(
    value: T,
    vararg blocks: Pair<((T) -> Boolean), ModifierBlock>,
): Modifier = composed {
    blocks.fastForEach { (func, block) ->
        if (func(value)) this@switch.block()
    }
    this
}

/**
 * Inline Modifier version of when(value) {...} for simple equality-based cases. For more complex
 * case conditions use [Modifier.switch] instead.
 */
inline fun <T> Modifier.switchEqual(
    value: T,
    vararg blocks: Pair<T, ModifierBlock>,
): Modifier = composed {
    blocks.fastForEach { (obj, block) ->
        if (obj == value) this@switchEqual.block()
    }
    this
}

/**
 * Inline Modifier version of when {...}
 */
inline fun Modifier.switch(vararg blocks: Pair<Boolean, ModifierBlock>): Modifier = composed {
    blocks.fastForEach { (condition, block) ->
        if (condition) this.block()
    }
    this
}
