package org.beatonma.commons.compose.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.beatonma.commons.test.extensions.assertions.shouldbe

/**
 * Custom assertion implementation to handle equality of list fields.
 */
fun AnnotatedString.shouldBe(other: AnnotatedString) {
    val thisSpanStyles = this.spanStyles.map(::spanStyleString)
    val otherSpanStyles = other.spanStyles.map(::spanStyleString)

    println("Actual (${thisSpanStyles.size}):")
    println(thisSpanStyles.joinToString("\n"))
    println("-")

    println("Expected (${otherSpanStyles.size}):")
    println(otherSpanStyles.joinToString("\n"))
    print("\n\n")

    this.text shouldbe other.text
    this.spanStyles.sortedBy { it.start } shouldbe other.spanStyles.sortedBy { it.start }
    this.paragraphStyles.sortedBy { it.start } shouldbe other.paragraphStyles.sortedBy { it.start }
}

private fun spanStyleString(span: AnnotatedString.Range<SpanStyle>): String =
    buildString {
        append("${span.start} -> ${span.end} ")
        val style = span.item

        whenNotNullOrEqual(style.fontWeight, FontWeight.Normal, ::append)
        whenNotNullOrEqual(style.fontStyle, FontStyle.Normal, ::append)
        whenNotNullOrEqual(style.fontFamily, FontFamily.Default, ::append)
    }

private inline fun <T> whenNotNullOrEqual(self: T?, other: T?, block: (T) -> Unit) {
    if (self == null) return
    if (self == other) return

    block(self)
}
