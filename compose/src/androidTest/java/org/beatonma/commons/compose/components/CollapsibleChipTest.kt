package org.beatonma.commons.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.beatonma.commons.theme.compose.Size
import org.junit.Test

class CollapsibleChipTest: ComposeTest() {
    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    private val chipTag = "chip"
    private val chipText = "Compose email"
    private val contentDescription = "Compose an email to fake@snommoc.org"

    // Padding in all directions + icon size
    private val expectedSizeCollapsed = 8.dp + Size.IconSmall + 8.dp

    @Test
    fun layout_default_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {

            onNodeWithTag(chipTag)
                .assertIsDisplayed()
                .assertWidthIsEqualTo(expectedSizeCollapsed)
                .assertHeightIsEqualTo(expectedSizeCollapsed)
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

            onNodeWithTag("clickable_cancel")
                .performClick()

            onNodeWithTag(chipTag)
                .assertWidthIsEqualTo(expectedSizeCollapsed)
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

            onNodeWithTag("clickable_confirm")
                .performClick()

            onNodeWithText("1").assertIsDisplayed()
        }
    }

    @Composable
    fun TestLayout() {
        var counter by remember { mutableStateOf(0) }

        Column {
            CollapsibleChip(
                text = AnnotatedString(chipText),
                contentDescription = contentDescription,
                icon = Icons.Default.Email,
                modifier = Modifier.testTag(chipTag)
            ) {
                counter += 1
            }

            Text(counter.toString())
        }
    }
}
