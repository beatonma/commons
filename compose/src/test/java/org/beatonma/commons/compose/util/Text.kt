package org.beatonma.commons.compose.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class ComposeTextUtilTest {
    private val italicStyle = SpanStyle(fontStyle = FontStyle.Italic)
    private val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)

    private fun AnnotatedString.shouldBe(other: AnnotatedString) {
        // Custom assertion implementation to handle equality of list fields
        val thisSpanStyles = this.spanStyles.map { "${it.start} ${it.end}" }
        val otherSpanStyles = other.spanStyles.map { "${it.start} ${it.end}" }

        println(thisSpanStyles.joinToString("\n"))
        println("-")
        println(otherSpanStyles.joinToString("\n"))
        println("-")
        println("${thisSpanStyles.size} vs ${otherSpanStyles.size}")

        this.text shouldbe other.text
        this.spanStyles.sortedBy { it.start } shouldbe other.spanStyles.sortedBy { it.start }
        this.paragraphStyles.sortedBy { it.start } shouldbe other.paragraphStyles.sortedBy { it.start }
    }

    @Test
    fun string_withAnnotatedStyle_with_single_style_isCorrect() {
        val italic = "Hello *Username*!"
        val bold = "Hello **Username**!"

        // Italics alone
        val italicResult = italic.withAnnotatedStyle()
        italicResult shouldbe AnnotatedString.Builder().apply {
            append("Hello ")
            withStyle(italicStyle) {
                append("Username")
            }
            append("!")
        }.toAnnotatedString()

        // Bold alone
        val boldResult = bold.withAnnotatedStyle()
        boldResult shouldbe AnnotatedString.Builder().apply {
            append("Hello ")
            withStyle(boldStyle) {
                append("Username")
            }
            append("!")
        }.toAnnotatedString()
    }

    @Test
    fun string_withAnnotatedStyle_with_multiple_styles_isCorrect() {
        val text = "Hello **Username**! How are *you*?"

        val result = text.withAnnotatedStyle()
        val expected = AnnotatedString.Builder().apply {
            append("Hello ")
            withStyle(boldStyle) {
                append("Username")
            }
            append("! How are ")
            withStyle(italicStyle) {
                append("you")
            }
            append("?")
        }.toAnnotatedString()

        result.shouldBe(expected)
    }

    @Test
    fun string_withAnnotatedStyle_with_nested_styles_isCorrect() {
        val text = "**Hello Username! How are *you*?**"

        val result = text.withAnnotatedStyle()
        val expected = AnnotatedString.Builder().apply {
            withStyle(boldStyle) {
                append("Hello Username! How are ")
                withStyle(italicStyle) {
                    append("you")
                }
                append("?")
            }
        }.toAnnotatedString()

        result.shouldBe(expected)
    }

    @Test
    fun string_withAnnotatedStyle_with_combined_styles_isCorrect() {
        val text = "*Hello* ***Username***! **How *are* you**?"

        val result = text.withAnnotatedStyle()
        val expected = AnnotatedString.Builder().apply {
            withStyle(italicStyle) {
                append("Hello")
            }

            append(" ")

            withStyle(italicStyle) {
                withStyle(boldStyle) {
                    append("Username")
                }
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
        }.toAnnotatedString()

        result.shouldBe(expected)
    }
}
