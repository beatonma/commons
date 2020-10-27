package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

private const val DOT = '·'

@Composable
fun dotted(vararg components: String?) =
    components.filterNot(String?::isNullOrEmpty)
        .joinToString(
            separator = " $DOT "
        )

/**
 * Render basic asterisk-based markdown-like styling as [AnnotatedString].
 * Currently only handles italic(*) and bold(**) styles.
 *
 * e.g. It can handle
 *      *this* or **this**
 *      or combinations like ***this***
 *      or **nesting *like* this!**
 */
fun String.withAnnotatedStyle() = AnnotatedString.Builder().apply {

    var totalAsterisks = 0
    var pendingStyle = 0 // Track number of asterisks that we have seen without applying the style.

    val styleMap = mutableMapOf<Int, Int>()

    this@withAnnotatedStyle.forEachIndexed { i, c ->
        if (c == '*') {
            pendingStyle += 1
            totalAsterisks += 1
        }
        else {
            if (pendingStyle > 0) {
                styleMap[i - totalAsterisks] = pendingStyle
                pendingStyle = 0
            }
            append(c)
        }
    }
    if (pendingStyle > 0) {
        println("Added trailing style $pendingStyle")
        styleMap[length] = pendingStyle
        println(pendingStyle)
    }

    if (styleMap.isEmpty()) {
        return@apply
    }

    val italicPositions = mutableListOf<Int>()
    val boldPositions = mutableListOf<Int>()

    styleMap.forEach { (position, style) ->
        println("[$position] $style")
        when (style) {
            1 -> italicPositions.add(position)
            2 -> boldPositions.add(position)

            3 -> {
                italicPositions.add(position)
                boldPositions.add(position)
            }
        }
    }

    val italicStyle = SpanStyle(fontStyle = FontStyle.Italic)
    val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)

    italicPositions.pairwise().forEach { (start, end) ->
        addStyle(italicStyle, start = start, end = end)
    }
    boldPositions.pairwise().forEach { (start, end) ->
        addStyle(boldStyle, start = start, end = end)
    }
}.toAnnotatedString()

/**
 * Take the list in (i, i+1) pairs. If list size is odd the last item will be ignored.
 */
private fun <T> List<T>.pairwise(): List<Pair<T, T>> {
    val s = size
    val result = mutableListOf<Pair<T, T>>()

    for (i in 0 until s step 2) {
        if (i + 1 < s) {
            result.add(this[i] to this[i + 1])
        }
    }
    return result
}
