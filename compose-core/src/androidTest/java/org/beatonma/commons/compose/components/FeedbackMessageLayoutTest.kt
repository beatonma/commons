package org.beatonma.commons.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertTopPositionInRootIsEqualTo
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestLayout
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.test.extensions.assertions.assertEquals
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class FeedbackMessageLayoutTest : ComposeTest() {
    private val message = "Here's some feedback!"
    private val messageTag = TestTag.FeedbackMessage
    private val surfaceTag = TestTag.FeedbackSurface
    private val toggleAlignmentTag = "toggle_alignment"
    private val showFeedbackTag = "show_feedback"
    private val hideFeedback = "hide_feedback"

    private val displayHeight = 600.dp


    @Test
    fun feedback_shouldBeHidden_byDefault() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(messageTag)
                .assertDoesNotExist()
        }
    }

    @Test
    fun feedback_shouldBeVisible_whenValueIsSet() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(showFeedbackTag)
                .performClick()

            onNodeWithTag(messageTag)
                .assertIsDisplayed()
        }
    }

    @Test
    fun feedback_shouldBeHidden_whenValueIsReset() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(showFeedbackTag)
                .performClick()

            onNodeWithTag(messageTag)
                .assertIsDisplayed()
                .assertTextEquals(message)
        }
    }

    @Test
    fun feedback_shouldBeAlignedCorrectly() {
        val alignment = mutableStateOf(Alignment.TopCenter)

        withContent {
            TestLayout(alignment)
        }

        setUp {
            onNodeWithTag(showFeedbackTag)
                .performClick()
        }

        perform {
            onNodeWithTag(surfaceTag)
                .assertTopPositionInRootIsEqualTo(0.dp)

            alignment.value = Alignment.BottomCenter

            onNodeWithTag(surfaceTag).run {
                val bounds = getUnclippedBoundsInRoot()

                bounds.bottom.assertEquals(displayHeight)
            }
        }
    }

    @Composable
    fun TestLayout(
        alignment: MutableState<Alignment> = mutableStateOf(Alignment.TopCenter),
        feedbackProvider: FeedbackProvider = mutableStateOf(null),
    ) {

        TestLayout(
            LocalFeedbackMessage provides feedbackProvider,
        ) {
            FeedbackLayout(
                Modifier.requiredHeight(displayHeight),
                alignment = alignment.value,
            ) {
                Column {
                    Button(
                        onClick = { feedbackProvider.value = AnnotatedString(message) },
                        Modifier.testTag(showFeedbackTag)
                    ) {
                        Text("Show feedback!")
                    }

                    Button(
                        onClick = { feedbackProvider.clear() },
                        Modifier.testTag(hideFeedback)
                    ) {
                        Text("Hide feedback!")
                    }

                    Button(
                        onClick = {
                            alignment.value = if (alignment.value == Alignment.TopCenter) {
                                Alignment.BottomCenter
                            } else {
                                Alignment.TopCenter
                            }
                        },
                        Modifier.testTag(toggleAlignmentTag)
                    ) {
                        Text("Toggle alignment!")
                    }
                }
            }
        }
    }
}
