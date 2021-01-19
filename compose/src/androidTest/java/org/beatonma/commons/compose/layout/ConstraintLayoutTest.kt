package org.beatonma.commons.compose.layout

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertTopPositionInRootIsEqualTo
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class ConstraintLayoutTest: ComposeTest() {
    @Test
    fun constraintLayoutScope_verticalChain_isCorrect() {
        withContent {
            ConstraintLayout(
                Modifier
                    .wrapContentHeight()
                    .semantics { testTag = "cl" }
            ) {
                verticalChain(
                    {
                        Spacer(it
                            .semantics { testTag = "first" }
                            .height(30.dp)
                            .fillMaxWidth())
                    },
                    {
                        Spacer(it
                            .semantics { testTag = "second" }
                            .height(70.dp)
                            .fillMaxWidth())
                    },
                )
            }
        }

        perform {
            onNodeWithTag("cl").run {
                assertHeightIsEqualTo(100.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(0.dp)
            }

            onNodeWithTag("first").run {
                assertHeightIsEqualTo(30.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(0.dp)
            }

            onNodeWithTag("second").run {
                assertHeightIsEqualTo(70.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(30.dp)
            }
        }
    }

    @Test
    fun constraintLayoutScope_verticalChain_withCustomParams_isCorrect() {
        withContent {
            ConstraintLayout(
                Modifier
                    .height(200.dp)
                    .width(300.dp)
                    .semantics { testTag = "cl" }
            ) {
                verticalChain(
                    {
                        Spacer(it
                            .semantics { testTag = "first" }
                            .height(31.dp)
                            .width(29.dp)
                        )
                    },
                    {
                        Spacer(it
                            .semantics { testTag = "second" }
                            .height(71.dp)
                            .width(92.dp)
                        )
                    },
                    first = {
                        // Stick to end of parent
                        end.linkTo(parent.end)
                    },
                    last = {
                        // Centered in parent horizontally
                        // Centered between previous item and parent bottom vertically
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                )
            }
        }

        perform {
            onNodeWithTag("cl").run {
                assertHeightIsEqualTo(200.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(0.dp)
            }

            onNodeWithTag("first").run {
                assertHeightIsEqualTo(31.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(0.dp)
                assertLeftPositionInRootIsEqualTo(271.dp)
            }

            onNodeWithTag("second").run {
                assertHeightIsEqualTo(71.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(80.dp)
                assertLeftPositionInRootIsEqualTo(104.dp)
            }
        }
    }

    override fun withContent(content: @Composable () -> Unit): ComposeTestRule =
        composeTestRule.apply { setContent(content) }
}
