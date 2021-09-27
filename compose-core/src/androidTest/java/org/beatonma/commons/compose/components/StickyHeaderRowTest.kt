package org.beatonma.commons.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.unit.dp
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestLayout
import org.beatonma.commons.testcompose.actions.swipeLeft
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class StickyHeaderRowTest : ComposeTest() {
    private val stickyLayoutTag = "sticky_row_layout"
    private val headerTag = "header"
    private val itemTag = "item"

    private val itemWidth = 60.dp
    private val itemHeight = 70.dp

    private val headerWidth = 80.dp
    private val headerHeight = 90.dp

    private val layoutWidth = 360.dp

    /**
     * Expected X position of the header of the 2nd item group
     */
    private val expectedLeftOfSecondHeader = itemWidth * 4

    @Test
    fun size_isCorrect() {
        withContent { TestLayout() }

        perform {
            onNodeWithTag(stickyLayoutTag)
                .assertHeightIsEqualTo(headerHeight + itemHeight)
                .assertWidthIsEqualTo(layoutWidth)
        }
    }

    @Test
    fun visibleHeaders_isCorrect() {
        withContent { TestLayout() }

        // First 6 items should be visible
        perform {
            onNodeWithText("6")
                .assertIsDisplayed()

            onNodeWithText("7")
                .assertIsNotDisplayed()

            onNodeWithText(Headers.LessThanFive.toString())
                .assertLeftPositionInRootIsEqualTo(0.dp)

            onNodeWithText(Headers.LessThanFifty.toString())
                .assertLeftPositionInRootIsEqualTo(expectedLeftOfSecondHeader)
        }
    }

    @Test
    fun swipeGesture_onHeader_isPassedToList() {
        withContent { TestLayout() }

        perform {
            onNodeWithText(Headers.LessThanFifty.toString())
                .performGesture {
                    swipeLeft(200F)
                }

            // Previously not-visible node should be visible
            onNodeWithText("7")
                .assertIsDisplayed()
        }
    }

    enum class Headers(val color: Color) {
        LessThanFive(Color.Red),
        LessThanFifty(Color.Blue),
        Other(Color.Yellow),
        Null(Color.Green),
        ;
    }

    @Suppress("RemoveExplicitTypeArguments")
    @Composable
    private fun TestLayout() {
        @Composable
        fun Header(header: Headers?, modifier: Modifier = Modifier) {
            Text(
                header?.toString() ?: "null",
                modifier
                    .requiredWidth(headerWidth)
                    .requiredHeight(headerHeight)
                    .background(Color.Cyan)
                    .testTag(headerTag)
            )
        }

        @Composable
        fun Item(item: Int, modifier: Modifier = Modifier) {
            Text(
                "$item",
                modifier
                    .requiredWidth(itemWidth)
                    .requiredHeight(itemHeight)
                    .background(Color.Yellow)
                    .testTag(itemTag)
            )
        }

        val items = remember { (1..100).toList() }

        TestLayout {
            Box(Modifier.width(layoutWidth)) {
                StickyHeaderRow(
                    items = items,
                    headerForItem = { item ->
                        when {
                            item < 5 -> Headers.LessThanFive
                            item < 50 -> Headers.LessThanFifty
                            else -> Headers.Other
                        }
                    },
                    itemContent = { item ->
                        Item(item)
                    },
                    headerContent = { header ->
                        Header(header)
                    },
                    modifier = Modifier.testTag(stickyLayoutTag),
                    groupModifier = { Modifier },
                )
            }
        }
    }
}
