package org.beatonma.commons.compose.components.collapsibleheader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.bottomCenter
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.topCenter
import androidx.compose.ui.unit.dp
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestLayout
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class CollapsibleHeaderLayoutTest : ComposeTest() {
    private val staticTag = "static"
    private val collapsibleTag = "collapsible"

    private val collapsingHeight = 97.dp
    private val staticHeight = 61.dp


    @Test
    fun heightWhenFullyExpanded_isCorrect() {
        withContent { TestLayout() }

        perform {
            onNodeWithTag(staticTag)
                .assertHeightIsEqualTo(staticHeight)

            onNodeWithTag(collapsibleTag)
                .assertHeightIsEqualTo(collapsingHeight)

            onNodeWithTag(TestTag.CollapsingHeader)
                .assertHeightIsEqualTo(collapsingHeight + staticHeight)
        }
    }

    @Test
    fun swipeToEnd_onHeader_collapsesHeaderToMinimum() {
        withContent { TestLayout() }

        perform {
            onNodeWithTag(TestTag.CollapsingHeader)
                .performGesture {
                    swipe(bottomCenter, topCenter)
                }
                .assertHeightIsEqualTo(staticHeight)

            onNodeWithTag(staticTag).assertHeightIsEqualTo(staticHeight)
            onNodeWithTag(collapsibleTag).assertHeightIsEqualTo(0.dp)
        }
    }

    @Test
    fun swipeToEnd_onList_collapsesHeaderToMinimum() {
        withContent { TestLayout() }

        perform {
            onNodeWithTag(TestTag.LazyList)
                .performGesture {
                    swipe(bottomCenter, topCenter)
                }

            onNodeWithTag(TestTag.CollapsingHeader)
                .assertHeightIsEqualTo(staticHeight)
        }
    }

    @Test
    fun swipeToStart_onHeader_expandsHeaderToFullHeight() {
        withContent { TestLayout() }

        perform {
            onNodeWithTag(TestTag.CollapsingHeader)
                .performGesture {
                    swipe(bottomCenter, topCenter)
                }
                .assertHeightIsEqualTo(staticHeight)
                .performGesture {
                    swipeDown()
                }
                .assertHeightIsAtLeast(20.dp)
        }
    }

    @Test
    fun swipeToStart_onList_expandsHeaderToFullHeight() {
        withContent { TestLayout() }

        perform {
            onNodeWithTag(TestTag.LazyList)
                .performGesture {
                    // swipe towards end
                    swipe(bottomCenter, topCenter)
                }

            onNodeWithTag(TestTag.LazyList)
                .performGesture {
                    // swipe back towards start
                    swipe(topCenter, bottomCenter)
                }

            onNodeWithTag(TestTag.CollapsingHeader)
                .assertHeightIsEqualTo(collapsingHeight + staticHeight)
        }
    }

    @Composable
    private fun TestLayout(
        lazyListState: LazyListState = rememberLazyListState(),
        headerState: CollapsibleHeaderState = rememberCollapsibleHeaderState(
            lazyListState,
            snapToStateAt = null,
        ),
    ) {
        TestLayout {
            val numbers = remember { (1..100).toList() }
            CollapsibleHeaderLayout(
                collapsingHeader = { expandedness ->
                    Column {
                        Spacer(
                            Modifier
                                .testTag(staticTag)
                                .background(Color.Blue)
                                .fillMaxWidth()
                                .height(staticHeight)
                        )

                        Spacer(
                            Modifier
                                .testTag(collapsibleTag)
                                .background(Color.Red)
                                .fillMaxWidth()
                                .height(collapsingHeight * expandedness)
                        )
                    }
                },
                lazyListContent = {
                    items(numbers) {
                        Text("$it")
                    }
                },
                headerState = headerState,
            )
        }
    }
}
