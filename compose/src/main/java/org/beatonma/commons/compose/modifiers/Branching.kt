package org.beatonma.commons.compose.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach

private typealias ModifierBlock = @Composable Modifier.() -> Modifier
private typealias TypedModifierBlock<T> = @Composable Modifier.(T) -> Modifier

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
 * Inline Modifier version of when(value) {...}
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
 * Inline Modifier version of when {...}
 */
@Composable
inline fun Modifier.switch(vararg blocks: Pair<(() -> Boolean), ModifierBlock>): Modifier {
    for ((func, block) in blocks) {
        if (func()) return this.block()
    }
    return this
}
