package org.beatonma.commons.compose.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import org.beatonma.commons.compose.util.ModifierBlock
import org.beatonma.commons.compose.util.TypedModifierBlock

@Composable
inline fun <T> Modifier.forEachOf(
    items: List<T>,
    block: TypedModifierBlock<T>,
): Modifier {
    return this.apply {
        items.fastForEach { item ->
            block(item)
        }
    }
}

@Composable
inline fun <T> Modifier.forEachOf(
    items: Array<T>,
    block: TypedModifierBlock<T>,
): Modifier {
    return this.apply {
        items.forEach { item ->
            block(item)
        }
    }
}

/**
 * Conditional modifier only applied when condition is met./
 */
@Composable
inline fun Modifier.onlyWhen(condition: Boolean, content: ModifierBlock): Modifier =
    if (condition) this.content()
    else this

@Composable
inline fun Modifier.either(
    condition: Boolean,
    whenTrue: ModifierBlock,
    whenFalse: ModifierBlock,
): Modifier {
    return if (condition) this.whenTrue() else this.whenFalse()
}

/**
 * Inline Modifier version of when(value) {...} using lambdas for complex cases. If you only require
 * equality checks, use [Modifier.switchEqual] instead.
 */
@Composable
inline fun <T> Modifier.switch(
    value: T,
    vararg blocks: Pair<((T) -> Boolean), ModifierBlock>,
): Modifier {
    for ((func, block) in blocks) {
        if (func(value)) return this@switch.block()
    }
    return this
}

/**
 * Inline Modifier version of when(value) {...} for simple equality-based cases. For more complex
 * case conditions use [Modifier.switch] instead.
 */
@Composable
inline fun <T> Modifier.switchEqual(
    value: T,
    vararg blocks: Pair<T, ModifierBlock>,
): Modifier {
    for ((obj, block) in blocks) {
        if (obj == value) return this@switchEqual.block()
    }
    return this
}

/**
 * Inline Modifier version of when {...}
 */
@Composable
inline fun Modifier.switch(vararg blocks: Pair<Boolean, ModifierBlock>): Modifier {
    for ((condition, block) in blocks) {
        if (condition) return this.block()
    }
    return this
}
