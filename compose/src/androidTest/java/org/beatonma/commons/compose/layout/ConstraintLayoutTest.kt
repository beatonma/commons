package org.beatonma.commons.compose.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertLeftPositionInRootIsEqualTo
import androidx.compose.ui.test.assertTopPositionInRootIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class ConstraintLayoutTest: ComposeTest() {
    private val layoutTag = "constraint_layout"
    private val firstTag = "first"
    private val secondTag = "second"

    @Test
    fun constraintLayoutScope_verticalChain_isCorrect() {
        withContent {
            ConstraintLayout(
                Modifier
                    .wrapContentHeight()
                    .testTag(layoutTag)
            ) {
                verticalChain(
                    {
                        Spacer(
                            it
                                .testTag(firstTag)
                                .height(30.dp)
                                .fillMaxWidth())
                    },
                    {
                        Spacer(
                            it
                                .testTag(secondTag)
                                .height(70.dp)
                                .fillMaxWidth())
                    },
                )
            }
        }

        perform {
            onNodeWithTag(layoutTag).run {
                assertHeightIsEqualTo(100.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(0.dp)
            }

            onNodeWithTag(firstTag).run {
                assertHeightIsEqualTo(30.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(0.dp)
            }

            onNodeWithTag(secondTag).run {
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
                    .testTag(layoutTag)
            ) {
                verticalChain(
                    {
                        Spacer(
                            it
                                .testTag(firstTag)
                                .height(31.dp)
                                .width(29.dp)
                        )
                    },
                    {
                        Spacer(
                            it
                                .testTag(secondTag)
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
            onNodeWithTag(layoutTag).run {
                assertHeightIsEqualTo(200.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(0.dp)
            }

            onNodeWithTag(firstTag).run {
                assertHeightIsEqualTo(31.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(0.dp)
                assertLeftPositionInRootIsEqualTo(271.dp)
            }

            onNodeWithTag(secondTag).run {
                assertHeightIsEqualTo(71.dp)
                assertIsDisplayed()
                assertTopPositionInRootIsEqualTo(80.dp)
                assertLeftPositionInRootIsEqualTo(104.dp)
            }
        }
    }
}
