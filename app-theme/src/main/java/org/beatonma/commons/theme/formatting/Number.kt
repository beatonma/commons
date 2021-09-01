package org.beatonma.commons.theme.formatting

import java.text.NumberFormat

object CommonsNumberFormat {
    val numberFormatter: NumberFormat = NumberFormat.getNumberInstance()
    val percentFormatter: NumberFormat = NumberFormat.getPercentInstance().apply {
        maximumFractionDigits = 1
        minimumFractionDigits = 1
    }
}

fun Int?.formatted(default: Int = 0): String = CommonsNumberFormat.numberFormatter.format(this ?: default)
fun Float?.formatPercent(default: Float = 0F): String = CommonsNumberFormat.percentFormatter.format(this ?: default)

fun formatPercent(numerator: Int, denominator: Int): String {
    require(numerator >= 0)
    require(denominator > 0)
    require(numerator <= denominator)
    return (numerator.toFloat() / denominator.toFloat()).formatPercent()
}
