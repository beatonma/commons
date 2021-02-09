package org.beatonma.commons.theme.compose.formatting

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.theme.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TIME_PATTERN = "HH:mm"
private const val DAY_PATTERN = "dd"
private const val MONTH_PATTERN = "MMMM"
private const val YEAR_PATTERN = "yyyy"
private const val ONGOING_PATTERN = ""

sealed class CommonsDateFormat {
    abstract val formatter: DateTimeFormatter

    internal fun format(date: LocalDate): String = formatter.format(date)
    internal fun format(datetime: LocalDateTime): String = formatter.format(datetime)

    object TimeToday: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN)
    }

    object TimeYesterday: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("$TIME_PATTERN - 'yesterday'")
    }

    object TimeDateThisYear: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("$TIME_PATTERN - $DAY_PATTERN $MONTH_PATTERN")
    }

    object TimeDateFull: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("$TIME_PATTERN - $DAY_PATTERN $MONTH_PATTERN $YEAR_PATTERN")
    }

    object Date: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("$DAY_PATTERN $MONTH_PATTERN $YEAR_PATTERN")
    }

    object DateThisYear: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("$DAY_PATTERN $MONTH_PATTERN")
    }

    object Month: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("$MONTH_PATTERN $YEAR_PATTERN")
    }

    object Year: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(YEAR_PATTERN)
    }

    object Ongoing: CommonsDateFormat() {
        override val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(ONGOING_PATTERN)
    }
}

fun LocalDate?.formatted(
    default: String = "",
    today: LocalDate = LocalDate.now(),
    formatter: CommonsDateFormat? = null
): String = when (this) {
    null -> default
    else -> formatter?.format(this) ?: chooseDateFormatter(today = today).format(this)
}

@Composable
fun dateRange(
    start: LocalDate?,
    end: LocalDate?,
    formatter: CommonsDateFormat = CommonsDateFormat.Date
) = when {
    start == null -> null
    end == null -> stringResource(R.string.date_since, start.formatted(formatter = formatter))
    else -> stringResource(
        R.string.date_range,
        start.formatted(formatter = formatter),
        end.formatted(formatter = formatter)
    )
}

fun LocalDateTime?.formatted(
    default: String = "",
    today: LocalDate = LocalDate.now(),
    formatter: CommonsDateFormat? = null,
): String = when(this) {
    null -> default
    else -> formatter?.format(this) ?: chooseTimeFormatter(today).format(this)
}

private fun LocalDateTime.chooseTimeFormatter(today: LocalDate = LocalDate.now()): CommonsDateFormat {
    val date = toLocalDate()
    return when {
        date == today -> CommonsDateFormat.TimeToday
        date == today.minusDays(1) -> CommonsDateFormat.TimeYesterday
        year == date.year -> CommonsDateFormat.TimeDateThisYear
        else -> CommonsDateFormat.TimeDateFull
    }
}

private fun LocalDate.chooseDateFormatter(today: LocalDate = LocalDate.now()): CommonsDateFormat {
    return when {
        this == today -> CommonsDateFormat.Ongoing
        year == today.year -> CommonsDateFormat.DateThisYear
        else -> CommonsDateFormat.Date
    }
}
