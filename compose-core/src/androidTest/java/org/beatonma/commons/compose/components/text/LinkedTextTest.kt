package org.beatonma.commons.compose.components.text

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.click
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.percentOffset
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.text.font.FontFamily
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestLayout
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class LinkedTextTest: ComposeTest() {
    /**
     * Using Monospace font we can expect that a click in the right half
     * should trigger the click action, left half should not.
     */
    private val defaultText = "beatonma#org | beatonma.org"
    private val clickedUrlsTag = "clicked_urls"


    @Test
    fun linkedText_clickOnNormalText_shouldNotTriggerClickAction() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithText(defaultText)
                .performGesture {
                    // Click in left half
                    click(percentOffset(0.25F, .5F))
                }

            onNodeWithTag(clickedUrlsTag)
                .assertTextEquals("")
        }
    }

    @Test
    fun linkedText_clickOnLink_shouldTriggerClickAction() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithText(defaultText)
                .performGesture {
                    // Click in right half
                    click(percentOffset(0.75F, .5F))
                }

            onNodeWithTag(clickedUrlsTag)
                .assertTextEquals("beatonma.org")
        }
    }

    @Composable
    fun TestLayout(
        text: String = defaultText
    ) {
        val urlsOpened: MutableState<List<String>> = remember { mutableStateOf(listOf()) }

        TestLayout {
            Column {
                LinkedText(
                    text,
                    fontFamily = FontFamily.Monospace,
                    action = { url ->
                        urlsOpened.value = urlsOpened.value + listOf(url)
                    }
                )

                Text(
                    urlsOpened.value.joinToString(","),
                    Modifier.testTag(clickedUrlsTag)
                )
            }
        }
    }
}
