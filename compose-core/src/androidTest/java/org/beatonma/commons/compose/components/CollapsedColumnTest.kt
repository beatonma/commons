package org.beatonma.commons.compose.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestLayout
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.testcompose.assertSizeIsTouchable
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class CollapsedColumnTest : ComposeTest() {
    private val headerText = "A wonderful group of integers"
    private val moreContentTag = TestTag.ShowMore


    @Test
    fun defaultLayout_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithText(headerText)
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertSizeIsTouchable()

            onNodeWithText("1")
                .assertIsDisplayed()
            onNodeWithText("2")
                .assertIsDisplayed()
            onNodeWithText("3")
                .assertIsDisplayed()

            onNodeWithTag(moreContentTag)
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithText("4")
                .assertDoesNotExist()
            onNodeWithText("10")
                .assertDoesNotExist()
        }
    }

    @Test
    fun click_onHeader_shouldExpandToShowAllItems() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithText(headerText)
                .performClick()

            onNodeWithText("1")
                .assertIsDisplayed()

            onNodeWithText("10")
                .assertIsDisplayed()
        }
    }

    @Test
    fun clickAgain_onHeader_shouldReturnToCollapsedState() {
        withContent {
            TestLayout()
        }

        setUp {
            onNodeWithText(headerText)
                .performClick()

            onNodeWithText("10")
                .assertIsDisplayed()
        }

        perform {
            onNodeWithText(headerText)
                .performClick()

            onNodeWithText("10")
                .assertDoesNotExist()
        }
    }

    @Test
    fun click_onMoreContentIndication_shouldExpandToShowAllItems() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(moreContentTag)
                .performClick()

            onNodeWithText("1")
                .assertIsDisplayed()

            onNodeWithText("10")
                .assertIsDisplayed()
        }
    }

    @Test
    fun moreContentIndication_shouldBeGone_whenExpanded() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(moreContentTag)
                .performClick()
                .assertDoesNotExist()
        }
    }

    @Test
    fun moreContentIndication_shouldBeGone_when_collapsedItemCount_isMoreThan_numberOfItems() {
        withContent {
            TestLayout(
                collapsedItemCount = 12
            )
        }

        perform {
            onNodeWithTag(moreContentTag)
                .assertDoesNotExist()
        }
    }

    @Composable
    private fun TestLayout(
        items: List<Int> = remember { (1..10).toList() },
        collapsedItemCount: Int = 3,
    ) {
        TestLayout {
            CollapsedColumn(
                items = items,
                collapsedItemCount = collapsedItemCount,
                lazy = true,
                headerBlock = CollapsedColumn.simpleHeader(headerText)
            ) { int ->
                Text("$int")
            }
        }
    }
}
