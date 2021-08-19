package org.beatonma.commons.app.frontpage

import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.util.HslColor
import org.beatonma.commons.compose.util.toColor
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class MembersLayoutTest : ComposeTest() {
    private val profileLayoutTag = "profile_layout"
    private val smallHeight = 24.dp
    private val tallHeight = smallHeight * 3
    private val itemWidth = 32.dp

    @Test
    fun testProfileLayout_withAllChildrenSameSize() {
        withContent {
            TestLayout {
                (0 until 5).forEach {
                    Block(height = tallHeight, color = HslColor(it * 20F).toColor())
                }
            }
        }

        perform {
            val expectedWidth = itemWidth * 5
            val expectedHeight = tallHeight

            onNodeWithTag(profileLayoutTag).run {
                assertWidthIsEqualTo(expectedWidth)
                assertHeightIsEqualTo(expectedHeight)
            }
        }
    }

    @Test
    fun testProfileLayout_withNoChildren() {
        withContent {
            TestLayout {

            }
        }

        perform {
            onNodeWithTag(profileLayoutTag).run {
                assertWidthIsEqualTo(0.dp)
                assertHeightIsEqualTo(0.dp)
            }
        }
    }

    @Test
    fun testProfileLayout_withSmallAndLargeChildren() {
        withContent {
            TestLayout {
                (0 until 5).forEach {
                    Block(height = smallHeight, color = HslColor(it * 20F).toColor())
                    Block(height = tallHeight, color = HslColor(it * 20F).toColor())
                }
            }
        }

        perform {
            // 3 small items per column -> 2 columns, and 5 columns for the large items
            val expectedWidth = (itemWidth * 2) + (itemWidth * 5)

            onNodeWithTag(profileLayoutTag).run {
                assertWidthIsEqualTo(expectedWidth)
                assertHeightIsEqualTo(tallHeight)
            }
        }
    }

    @Composable
    private fun TestLayout(content: @Composable () -> Unit) {
        MembersLayout(
            Modifier.testTag(profileLayoutTag),
            content = content,
        )
    }

    @Composable
    private fun Block(
        width: Dp = itemWidth,
        height: Dp,
        color: Color,
        content: @Composable () -> Unit = { Text("Hello") }
    ) {
        Surface(Modifier.size(width, height), color = color, content = content)
    }
}
