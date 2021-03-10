package org.beatonma.commons.compose.components

import org.beatonma.commons.test.extensions.assertions.shouldNotBe
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class TextValidationRulesTest {

    @Test
    fun minLength_isCorrect() {
        val rules = TextValidationRules(minLength = 3)

        with (rules) {
            validate("") shouldbe TextValidationResult.TOO_SHORT
            validate("a") shouldbe TextValidationResult.TOO_SHORT
            validate("ab") shouldbe TextValidationResult.TOO_SHORT

            validate("abc") shouldbe TextValidationResult.OK
            validate("abcd") shouldbe TextValidationResult.OK

            validate("abcdefghijklmnopqrstuvwxyz") shouldbe TextValidationResult.OK
        }
    }

    @Test
    fun maxLength_isCorrect() {
        val rules = TextValidationRules(maxLength = 3)

        with (rules) {
            validate("") shouldbe TextValidationResult.OK
            validate("a") shouldbe TextValidationResult.OK
            validate("ab") shouldbe TextValidationResult.OK
            validate("abc") shouldbe TextValidationResult.OK

            validate("abcd") shouldbe TextValidationResult.TOO_LONG
            validate("abcde") shouldbe TextValidationResult.TOO_LONG

            validate("abcdefghijklmnopqrstuvwxyz") shouldbe TextValidationResult.TOO_LONG
        }
    }

    @Test
    fun minLength_and_maxLength_isCorrect() {
        val rules = TextValidationRules(minLength = 5, maxLength = 7)

        with (rules) {
            validate("abcd") shouldbe TextValidationResult.TOO_SHORT

            validate("abcde") shouldbe TextValidationResult.OK
            validate("abcdef") shouldbe TextValidationResult.OK
            validate("abcdefg") shouldbe TextValidationResult.OK

            validate("abcdefgh") shouldbe TextValidationResult.TOO_LONG
            validate("abcdefghijk") shouldbe TextValidationResult.TOO_LONG
            validate("abcdefghijklmnopqrstuvwxyz") shouldbe TextValidationResult.TOO_LONG
        }
    }

    @Test
    fun regex_isCorrect() {
        val rules = TextValidationRules(regex = "[a-z_-]+".toRegex())

        with (rules) {
            validate("a") shouldbe TextValidationResult.OK
            validate("afdajuhjfaejngagf") shouldbe TextValidationResult.OK
            validate("kjfbmnabeuiryaiojf") shouldbe TextValidationResult.OK
            validate("_afdajuhjfaejnga___gf") shouldbe TextValidationResult.OK
            validate("kjfbmna-beuir-yaiojf") shouldbe TextValidationResult.OK

            validate("") shouldbe TextValidationResult.FORMAT_ERROR
            validate("1") shouldbe TextValidationResult.FORMAT_ERROR
            validate(" ") shouldbe TextValidationResult.FORMAT_ERROR
            validate("$") shouldbe TextValidationResult.FORMAT_ERROR

            validate("A") shouldbe TextValidationResult.FORMAT_ERROR
            validate("AFDAJUHJFAEJNGAGF") shouldbe TextValidationResult.FORMAT_ERROR
            validate("KJFBMNABEUIRYAIOJF") shouldbe TextValidationResult.FORMAT_ERROR
        }
    }

    @Test
    fun allRules_isCorrect() {
        val rules = TextValidationRules(
            minLength = 3,
            maxLength = 10,
            regex = "[aA][a-zA-Z0-9_-]+".toRegex()
        )

        with (rules) {
            // Check regex and minLength
            validate("abc") shouldbe TextValidationResult.OK
            validate("ABC") shouldbe TextValidationResult.OK
            validate("A-123") shouldbe TextValidationResult.OK

            validate("and-a-123") shouldbe TextValidationResult.OK
            validate("and-a-123").isOk shouldbe true
            validate("and-a-123").isError shouldbe false

            validate("ab") shouldbe TextValidationResult.TOO_SHORT
            validate("AB") shouldbe TextValidationResult.TOO_SHORT
            validate("AB") shouldbe TextValidationResult.TOO_SHORT
            validate("AB").isError shouldbe true
            validate("AB").isOk shouldbe false

            validate("BA") shouldNotBe TextValidationResult.OK
            validate("12") shouldNotBe TextValidationResult.OK
            validate("BA").isError shouldbe true
            validate("BA").isOk shouldbe false
        }

        with (rules) {
            // Check regex and maxLength
            validate("ABcdeafoehjafjkn") shouldbe TextValidationResult.TOO_LONG
            validate("abdcjdiajoirj") shouldbe TextValidationResult.TOO_LONG
            validate("!abdcjdiajoirj") shouldNotBe TextValidationResult.OK
        }
    }
}
