package org.beatonma.commons.kotlin.extensions

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.context
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val commonsDateFormat = DateTimeFormatter.ofPattern("dd MM yyyy")

fun LocalDate?.formatted(default: String = ""): String = when (this) {
    null -> default
    else -> commonsDateFormat.format(this)
}

fun Context?.dateRange(start: LocalDate?, end: LocalDate?) = when {
    start == null -> null
    end == null -> stringCompat(R.string.date_since, start.formatted())
    else -> stringCompat(R.string.date_range, start.formatted(), end.formatted())
}

fun RecyclerView.ViewHolder.dateRange(start: LocalDate?, end: LocalDate?) =
    itemView.context.dateRange(start, end)

fun AndroidViewModel.dateRange(start: LocalDate?, end: LocalDate?) = context.dateRange(start, end)
