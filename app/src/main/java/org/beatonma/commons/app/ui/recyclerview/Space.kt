package org.beatonma.commons.app.ui.recyclerview

import android.content.Context
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.extensions.dimenCompat
import org.beatonma.lib.ui.recyclerview.kotlin.extensions.RvSpacing

fun defaultItemSpace(context: Context, horizontal: Boolean = false, vertical: Boolean = false) = RvSpacing(
    verticalItemSpace = if (vertical) context.dimenCompat(R.dimen.flow_gap_vertical) else -1,
    horizontalItemSpace = if (horizontal) context.dimenCompat(R.dimen.flow_gap_horizontal) else -1,
)
