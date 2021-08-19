package org.beatonma.commons.compose.layout

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintLayoutScope
import org.beatonma.commons.core.extensions.fastForEachIndexed

/**
 * Convenience for linkTo using [other] as a template - constraints copy [other] unless overridden.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun ConstrainScope.linkTo(
    other: ConstrainedLayoutReference,
    start: ConstraintLayoutBaseScope.VerticalAnchor = other.start,
    top: ConstraintLayoutBaseScope.HorizontalAnchor = other.top,
    end: ConstraintLayoutBaseScope.VerticalAnchor = other.end,
    bottom: ConstraintLayoutBaseScope.HorizontalAnchor = other.bottom,
    startMargin: Dp = 0.dp,
    topMargin: Dp = 0.dp,
    endMargin: Dp = 0.dp,
    bottomMargin: Dp = 0.dp,
    @FloatRange(from = 0.0, to = 1.0) horizontalBias: Float = 0.5f,
    @FloatRange(from = 0.0, to = 1.0) verticalBias: Float = 0.5f
) =
    linkTo(start, top, end, bottom, startMargin, topMargin, endMargin, bottomMargin, horizontalBias, verticalBias)

/**
 * Apply constraints to [children] so they form a column.
 * @return Array of [ConstrainedLayoutReference]s that were applied to the children in the same order
 *         as provided in input.
 */
@Composable
fun ConstraintLayoutScope.verticalChain(
    vararg children: @Composable (Modifier) -> Unit,
    first: (ConstrainScope.() -> Unit)? = null,
    last: (ConstrainScope.() -> Unit)? = null,
): Array<ConstrainedLayoutReference> {
    val size = children.size
    val lastIndex = size - 1
    val refs = Array(size) { createRef() }

    children.fastForEachIndexed { i, child ->
        val self = refs[i]
        val previous: ConstrainedLayoutReference? = if (i == 0) null else refs[i - 1]

        child(
            Modifier.constrainAs(self) {
                if (i == 0) first?.invoke(this)
                else top.linkTo(previous!!.bottom)
                
                if (i == lastIndex) last?.invoke(this)
            }
        )
    }

    return refs
}

/**
 * Apply constraints to [children] so they form a column.
 * @return Array of [ConstrainedLayoutReference]s that were applied to the children in the same order
 *         as provided in input.
 */
@Composable
fun ConstraintLayoutScope.horizontalChain(
    vararg children: @Composable (Modifier) -> Unit,
    first: (ConstrainScope.() -> Unit)? = null,
    last: (ConstrainScope.() -> Unit)? = null,
): Array<ConstrainedLayoutReference> {
    val size = children.size
    val lastIndex = size - 1
    val refs = Array(size) { createRef() }

    children.fastForEachIndexed { i, child ->
        val self = refs[i]
        val previous: ConstrainedLayoutReference? = if (i == 0) null else refs[i - 1]

        child(
            Modifier.constrainAs(self) {
                if (i == 0) first?.invoke(this)
                else start.linkTo(previous!!.end)

                if (i == lastIndex) last?.invoke(this)
            }
        )
    }

    return refs
}
