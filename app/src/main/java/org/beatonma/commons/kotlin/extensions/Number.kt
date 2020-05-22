package org.beatonma.commons.kotlin.extensions

import java.text.NumberFormat

object CommonsNumberFormat {
    val numberFormatter: NumberFormat = NumberFormat.getNumberInstance()
    val percentFormatter: NumberFormat = NumberFormat.getPercentInstance().apply {
        maximumFractionDigits = 1
        minimumFractionDigits = 1
    }
}

fun Int.formatted(): String = CommonsNumberFormat.numberFormatter.format(this)
fun Float.formatPercent(): String = CommonsNumberFormat.percentFormatter.format(this)
