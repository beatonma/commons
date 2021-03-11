package org.beatonma.commons.compose.components.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.test.extensions.assertions.assertEquals
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class SearchFieldTest: ComposeTest() {
    private val tag = "search_field"
    private val hintText = "Search for..."

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    @Test
    fun onQueryChange_isCorrect() {
        val queryText = mutableStateOf("")

        withContent {
            TestLayout(queryText = queryText)
        }

        perform {
            onNodeWithTag(tag).run {
                performTextInput("hello")
                assertTextEquals("hello")
                performTextInput(" there")
                assertTextEquals("hello there")

                queryText.value.assertEquals("hello there")
            }
        }
    }

    @Test
    fun onSubmit_isTriggeredByImeAction() {
        val submittedText = mutableStateOf("")

        withContent {
            TestLayout(submittedText = submittedText)
        }

        perform {
            onNodeWithTag(tag).run {
                performTextInput("hello")
                assert(submittedText.value == "")

                performImeAction()

                submittedText.value.assertEquals("hello")
            }
        }
    }

    @Test
    fun clearQueryIcon_isVisible_onlyWhenQueryIsNotEmpty() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag("clear_icon")
                .assertDoesNotExist()

            onNodeWithTag(tag)
                .performTextInput("a")

            onNodeWithTag("clear_icon")
                .assertIsDisplayed()

            onNodeWithTag(tag)
                .performTextClearance()

            onNodeWithTag("clear_icon")
                .assertDoesNotExist()
        }
    }

    @Test
    fun clearQueryIcon_clearsQueryWhenClicked() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(tag)
                .performTextInput("abcdef")

            onNodeWithTag("clear_icon")
                .assertIsDisplayed()
                .performClick()
                .assertDoesNotExist()

            onNodeWithTag(tag)
                .assertTextEquals("")
        }
    }

    @Composable
    fun TestLayout(
        queryText: MutableState<String> = rememberText(),
        submittedText: MutableState<String> = rememberText(),
    ) {
        val query = rememberText()

        SearchField(
            hintText,
            Modifier.testTag("search_field"),
            query = query,
            onQueryChange = { q ->
                queryText.value = q
            },
            onSubmit = { imeAction, q ->
                submittedText.value = q
            }
        )
    }
}
