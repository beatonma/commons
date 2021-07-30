package org.beatonma.commons.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.ambient.LocalAccessibility
import org.beatonma.commons.testcompose.assertSizeIsSquare
import org.beatonma.commons.testcompose.assertSizeIsTouchable
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class CollapsibleChipTest: ComposeTest() {
    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    private val chipTag = TestTag.Chip
    private val chipText = "Compose email"
    private val contentDescription = "Compose an email to fake@snommoc.org"

    @Test
    fun layout_default_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(chipTag)
                .assertIsDisplayed()
                .assertSizeIsSquare()
                .assertSizeIsTouchable()
                .assertHasClickAction()
        }
    }

    @Test
    fun layout_expandsWhenClickedOnce() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(chipTag)
                .performClick()
                .assertWidthIsAtLeast(50.dp)
        }
    }

    @Test
    fun whenClickedOnce_clickActionShouldNotBeCalled() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(chipTag)
                .performClick()

            onNodeWithText("0")
                .assertIsDisplayed()
        }
    }

    @Test
    fun clickingCancel_whenExpanded_shouldCollapse() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(chipTag)
                .performClick()

            onNodeWithTag(TestTag.Cancel)
                .performClick()

            onNodeWithTag(chipTag)
                .assertSizeIsSquare()
        }
    }

    @Test
    fun clickingMainArea_whenExpanded_shouldTriggerAction() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(chipTag)
                .performClick()

            onNodeWithTag(TestTag.Confirm)
                .performClick()

            dump(label = "CollapsibleChip")

            onNodeWithText("1").assertIsDisplayed()
        }
    }

    @Test
    fun withAccessibility_isCorrect() {
        withContent {
            CompositionLocalProvider(LocalAccessibility provides true) {
                TestLayout()
            }
        }

        perform {
            onNodeWithTag(TestTag.Confirm)
                .assertExists()
                .assertTextEquals(chipText)
                .assertIsDisplayed()
                .performClick()

            onNodeWithText("1")
                .assertIsDisplayed()
        }
    }

    @Composable
    private fun TestLayout() {
        var counter by remember { mutableStateOf(0) }

        Column {
            CollapsibleChip(
                text = AnnotatedString(chipText),
                contentDescription = contentDescription,
                icon = Icons.Default.Email,
            ) {
                counter += 1
            }

            Text("$counter")
        }
    }
}
