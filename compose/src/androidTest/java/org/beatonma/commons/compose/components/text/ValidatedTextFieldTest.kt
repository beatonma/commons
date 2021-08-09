package org.beatonma.commons.compose.components.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.AnnotatedString
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.components.FeedbackProvider
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class ValidatedTextFieldTest: ComposeTest() {
    private val counterTag = TestTag.ValidationCounter
    private val textTag = TestTag.ValidatedText
    private val feedbackTag = TestTag.FeedbackText


    private fun ComposeTestRule.clearText() {
        onNodeWithTag(textTag)
            .performTextClearance()
    }

    private fun ComposeTestRule.inputText(text: String) {
        onNodeWithTag(textTag)
            .performTextInput(text)
    }

    private fun ComposeTestRule.assertFeedbackTextEquals(expected: String) {
        onNodeWithTag(feedbackTag)
            .assertTextEquals(expected)
    }

    private fun ComposeTestRule.assertCounterTextEquals(expected: String) {
        onNodeWithTag(counterTag)
            .assertTextEquals(expected)
    }

    @Test
    fun feedback_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            inputText("ab")
            assertFeedbackTextEquals(TextValidationResult.TOO_SHORT.name)
        }

        perform {
            inputText("cd")
            assertFeedbackTextEquals(TextValidationResult.OK.name)
        }

        perform {
            inputText("efghijklmnop")
            assertFeedbackTextEquals(TextValidationResult.TOO_LONG.name)
        }

        perform {
            clearText()
            inputText("123")

            assertFeedbackTextEquals(TextValidationResult.FORMAT_ERROR.name)
        }
    }

    @Test
    fun counter_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            assertCounterTextEquals("0/10")
        }

        perform {
            inputText("ab")
            assertCounterTextEquals("2/10")
        }

        perform {
            inputText("abcdefg")
            assertCounterTextEquals("9/10")
        }

        perform {
            clearText()
            assertCounterTextEquals("0/10")
        }

        perform {
            inputText("a")
            assertCounterTextEquals("1/10")
        }
    }

    @Composable
    fun TestLayout(
        text: MutableState<String> = rememberText(),
        rules: TextValidationRules = rememberValidationRules(
            minLength = 3,
            maxLength = 10,
            regex = "[a-z]+".toRegex()
        ),
        feedback: FeedbackProvider = remember { mutableStateOf(null) }
    ) {
        ValidatedTextField(
            value = text.value,
            validationRules = rules,
            onValueChange = { newValue, valid ->
                text.value = newValue
                return@ValidatedTextField AnnotatedString(valid.name)
            },
            feedbackProvider = feedback,
        )
    }
}
