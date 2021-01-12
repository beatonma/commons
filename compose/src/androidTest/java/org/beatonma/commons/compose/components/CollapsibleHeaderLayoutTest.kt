package org.beatonma.commons.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.util.pxF
import org.beatonma.commons.test.extensions.assertions.assertFuzzyEquals
import org.beatonma.commons.testcompose.actions.swipeUp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class CollapsibleHeaderLayoutTest: ComposeTest() {

    override fun withContent(content: @Composable () -> Unit): ComposeTestRule =
        composeTestRule.apply { setContent(composable = content) }

    @Test
    fun CollapsibleHeader_height_at_full_expansion_is_correct() {
        withContent {
            TestLayout()
        }
        perform {
            onNodeWithTag("static").assertHeightIsEqualTo(10.dp)
            onNodeWithTag("collapsible").assertHeightIsEqualTo(100.dp)
            onNodeWithTag("collapsing_header").assertHeightIsEqualTo(110.dp)
        }
    }

    @Test
    fun CollapsibleHeader_height_at_full_collapse_is_correct() {
        withContent {
            TestLayout()
        }
        perform {
            onNodeWithTag("collapsing_header").run {
                performGesture {
                    swipeUp()
                }.run {
                    assertHeightIsEqualTo(10.dp)

                    onNodeWithTag("static").assertHeightIsEqualTo(10.dp)
                    onNodeWithTag("collapsible").assertHeightIsEqualTo(0.dp)
                }
            }
        }
    }

    @Test
    fun CollapsibleHeader_height_during_swipe_is_animated_is_correct() {
        val headerState = CollapsibleHeaderState()
        var dp = 0F
        withContent {
            TestLayout(headerState = headerState)
            dp = 1.dp.pxF
        }
        perform {
            onNodeWithTag("collapsing_header").run {
                performGesture {
                    swipeUp(55F * dp, durationMillis = 10)
                }.run {
                    assertHeightIsEqualTo(60.dp)
                    onNodeWithTag("static").assertHeightIsEqualTo(10.dp)
                    headerState.expandProgress.assertFuzzyEquals(0.5F)
                }
            }
        }
    }
}


@Composable
fun TestLayout(
    headerState: CollapsibleHeaderState = remember { CollapsibleHeaderState() }
) {
    LinearCollapsibleHeaderLayout(
        collapsingHeader = { expandedness ->
            Column(
                Modifier.semantics { testTag = "collapsing_header" }
            ) {
                Spacer(
                    Modifier
                        .semantics { testTag = "static" }
                        .background(Color.Blue)
                        .fillMaxWidth()
                        .height(10.dp)
                )

                Spacer(
                    Modifier
                        .semantics { testTag = "collapsible" }
                        .background(Color.Red)
                        .fillMaxWidth()
                        .height(100.dp * expandedness)
                )
            }
        },

        lazyListContent = {
            items(items = (1..100).toList()) {
                Text("$it")
            }
        },
        touchInterceptModifier = Modifier.semantics { testTag = "layout" },
        headerState = headerState
    )
}
