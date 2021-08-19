package org.beatonma.commons.compose.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import org.junit.Test
import java.util.regex.Pattern

class StringWithAnnotatedUrlsTest {
    private val style = SpanStyle(color = Color.Red)

    @Test
    fun singleUrl_isCorrect() {
        val result = "beatonma.org".withAnnotatedUrls(style, Patterns.WEB_URL)

        val expected = buildAnnotatedString {
            withStyle(style) {
                append("beatonma.org")
            }
        }

        result.shouldBe(expected)
    }

    @Test
    fun singleUrl_withinOtherText_isCorrect() {
        val result = "More info at beatonma.org!".withAnnotatedUrls(style, Patterns.WEB_URL)

        val expected = buildAnnotatedString {
            append("More info at ")
            withStyle(style) {
                append("beatonma.org")
            }
            append("!")
        }

        result.shouldBe(expected)
    }

    @Test
    fun multipleUrls_isCorrect() {
        val result = "beatonma.com snommoc.org".withAnnotatedUrls(style, Patterns.WEB_URL)

        val expected = buildAnnotatedString {
            withStyle(style) {
                append("beatonma.com")
            }
            append(" ")
            withStyle(style) {
                append("snommoc.org")
            }
        }

        result.shouldBe(expected)
    }

    @Test
    fun multipleUrls_withinOtherText_isCorrect() {
        val result = "blah blah beatonma.com blah blah https://snommoc.org/about blah!".withAnnotatedUrls(style,
            Patterns.WEB_URL
        )

        val expected = buildAnnotatedString {
            append("blah blah ")
            withStyle(style) {
                append("beatonma.com")
            }
            append(" blah blah ")
            withStyle(style) {
                append("https://snommoc.org/about")
            }
            append(" blah!")
        }

        result.shouldBe(expected)
    }
}

/**
 * Clone of the bits of android.util.Patterns that we need.
 */
private object Patterns {
    private const val IP_ADDRESS_STRING =
        ("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
            + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
            + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
            + "|[1-9][0-9]|[0-9]))")

    private const val UCS_CHAR = "[" +
        "\u00A0-\uD7FF" +
        "\uF900-\uFDCF" +
        "\uFDF0-\uFFEF" +
        "\uD800\uDC00-\uD83F\uDFFD" +
        "\uD840\uDC00-\uD87F\uDFFD" +
        "\uD880\uDC00-\uD8BF\uDFFD" +
        "\uD8C0\uDC00-\uD8FF\uDFFD" +
        "\uD900\uDC00-\uD93F\uDFFD" +
        "\uD940\uDC00-\uD97F\uDFFD" +
        "\uD980\uDC00-\uD9BF\uDFFD" +
        "\uD9C0\uDC00-\uD9FF\uDFFD" +
        "\uDA00\uDC00-\uDA3F\uDFFD" +
        "\uDA40\uDC00-\uDA7F\uDFFD" +
        "\uDA80\uDC00-\uDABF\uDFFD" +
        "\uDAC0\uDC00-\uDAFF\uDFFD" +
        "\uDB00\uDC00-\uDB3F\uDFFD" +
        "\uDB44\uDC00-\uDB7F\uDFFD" +
        "&&[^\u00A0[\u2000-\u200A]\u2028\u2029\u202F\u3000]]"

    /**
     * Valid characters for IRI label defined in RFC 3987.
     */
    private const val LABEL_CHAR = "a-zA-Z0-9$UCS_CHAR"

    /**
     * Valid characters for IRI TLD defined in RFC 3987.
     */
    private const val TLD_CHAR = "a-zA-Z$UCS_CHAR"

    /**
     * RFC 1035 Section 2.3.4 limits the labels to a maximum 63 octets.
     */
    private const val IRI_LABEL =
        "[" + LABEL_CHAR + "](?:[" + LABEL_CHAR + "_\\-]{0,61}[" + LABEL_CHAR + "]){0,1}"

    /**
     * RFC 3492 references RFC 1034 and limits Punycode algorithm output to 63 characters.
     */
    private const val PUNYCODE_TLD = "xn\\-\\-[\\w\\-]{0,58}\\w"

    private const val TLD = "($PUNYCODE_TLD|[$TLD_CHAR]{2,63})"

    private const val HOST_NAME = "($IRI_LABEL\\.)+$TLD"

    private const val DOMAIN_NAME_STR = "($HOST_NAME|$IP_ADDRESS_STRING)"

    private const val PROTOCOL = "(?i:http|https|rtsp)://"

    /* A word boundary or end of input.  This is to stop foo.sure from matching as foo.su */
    private const val WORD_BOUNDARY = "(?:\\b|$|^)"

    private const val USER_INFO = ("(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)"
        + "\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_"
        + "\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@")

    private const val PORT_NUMBER = "\\:\\d{1,5}"

    private const val PATH_AND_QUERY = ("[/\\?](?:(?:[" + LABEL_CHAR
        + ";/\\?:@&=#~" // plus optional query params
        + "\\-\\.\\+!\\*'\\(\\),_\\$])|(?:%[a-fA-F0-9]{2}))*")

    /**
     * Regular expression pattern to match most part of RFC 3987
     * Internationalized URLs, aka IRIs.
     */
    val WEB_URL: Pattern = Pattern.compile(
        "("
            + "("
            + "(?:" + PROTOCOL + "(?:" + USER_INFO + ")?" + ")?"
            + "(?:" + DOMAIN_NAME_STR + ")"
            + "(?:" + PORT_NUMBER + ")?"
            + ")"
            + "(" + PATH_AND_QUERY + ")?"
            + WORD_BOUNDARY
            + ")"
    )
}
