package org.beatonma.commons.compose.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.SpanStyleRange
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test
import kotlin.system.measureTimeMillis

class ComposeTextUtilTest {
    private val italicStyle = SpanStyle(fontStyle = FontStyle.Italic)
    private val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)
    private val quoteStyle = SpanStyle(fontFamily = FontFamily.Monospace)

    private fun AnnotatedString.shouldBe(other: AnnotatedString) {
        // Custom assertion implementation to handle equality of list fields
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

    private fun <T> whenNotNullOrEqual(self: T?, other: T?, block: (T) -> Unit) {
        if (self == null) return
        if (self == other) return

        block(self!!)
    }

    private fun spanStyleString(span: SpanStyleRange): String =
        buildString {
            append("${span.start} -> ${span.end} ")
            val style = span.item

            whenNotNullOrEqual(style.fontWeight, FontWeight.Normal, ::append)
            whenNotNullOrEqual(style.fontStyle, FontStyle.Normal, ::append)
            whenNotNullOrEqual(style.fontFamily, FontFamily.Default, ::append)

        }

    @Test
    fun string_withAnnotatedStyle_with_single_style_isCorrect() {
        val italic = "Hello *Username*!"
        val bold = "Hello **Username**!"

        // Italics alone
        val italicResult = italic.withAnnotatedStyle(italicStyle, boldStyle, quoteStyle)
        italicResult shouldbe annotatedString {
            append("Hello ")
            withStyle(italicStyle) {
                append("Username")
            }
            append("!")
        }

        // Bold alone
        val boldResult = bold.withAnnotatedStyle(italicStyle, boldStyle, quoteStyle)
        boldResult shouldbe annotatedString {
            append("Hello ")
            withStyle(boldStyle) {
                append("Username")
            }
            append("!")
        }
    }

    @Test
    fun string_withAnnotatedStyle_with_multiple_styles_isCorrect() {
        val text = "Hello **Username**! `How are` *you*?"
        println(text)

        val result = text.withAnnotatedStyle(italicStyle, boldStyle, quoteStyle)
        val expected = annotatedString {
            append("Hello ")
            withStyle(boldStyle) {
                append("Username")
            }
            append("! ")
            withStyle(quoteStyle) {
                append("How are")
            }
            append(" ")
            withStyle(italicStyle) {
                append("you")
            }
            append("?")
        }

        result.shouldBe(expected)
    }

    @Test
    fun string_withAnnotatedStyle_with_nested_styles_isCorrect() {
        val text = "**Hello Username! How are *you*?**"
        println(text)

        val result = text.withAnnotatedStyle(italicStyle, boldStyle, quoteStyle)
        val expected = annotatedString {
            withStyle(boldStyle) {
                append("Hello Username! How are ")
                withStyle(italicStyle) {
                    append("you")
                }
                append("?")
            }
        }

        result.shouldBe(expected)
    }

    @Test
    fun string_withAnnotatedStyle_with_combined_styles_isCorrect() {
        val text = "*Hello* ***Username***! **How *are* you**?"
        println(text)

        val result = text.withAnnotatedStyle(italicStyle, boldStyle, quoteStyle)
        val expected = annotatedString {
            withStyle(italicStyle) {
                append("Hello")
            }

            append(" ")

            withStyle(italicStyle + boldStyle) {
                append("Username")
            }

            append("! ")
            withStyle(boldStyle) {
                append("How ")
                withStyle(italicStyle) {
                    append("are")
                }
                append(" you")
            }
            append("?")
        }

        result.shouldBe(expected)
    }

    @Test
    fun string_withAnnotatedStyle_with_quotes_isCorrect() {
        val text = "Hello 'yourname'"

        val result = text.withAnnotatedStyle(italicStyle, boldStyle, quoteStyle)
        val expected = annotatedString {
            append("Hello ")
            withStyle(quoteStyle) {
                append("yourname")
            }
        }

        result.shouldBe(expected)
    }

    @Test
    fun string_withAnnotatedStyle_with_quotes_benchmark() {
        val runCount = 1000
        val shortText = "*Hello* ***Username***! **How *are* you**?"
        val longText =
            "Lorem ipsum dolor sit amet, **consectetur adipiscing elit**, sed do eiusmod tempor 'incididunt' ut labore et `dolore` magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur *sint* occaecat cupidatat non proident, ***sunt in culpa qui officia deserunt mollit*** anim id est laborum."

        for (text in arrayOf(shortText, longText)) {
            measureTimeMillis {
                for (i in 0..runCount) {
                    text.withAnnotatedStyle(
                        italicStyle,
                        boldStyle,
                        quoteStyle)
                }
            }.let { duration ->
                println("[${text.length} chars] $runCount runs took ${duration}ms")
            }
        }
    }
}
