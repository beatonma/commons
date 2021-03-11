package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class WrapContentSizeTest: ComposeTest() {
    private val wrapperTag = "wrapper"

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    @Test
    fun at_0percent_isCorrect() {
        withContent { WrappedSize(progress = 0F) }

        perform {
            onNodeWithTag(wrapperTag).assertWidthIsEqualTo(0.dp)
            onNodeWithTag(wrapperTag).assertHeightIsEqualTo(0.dp)
        }
    }

    @Test
    fun at_100percent_isCorrect() {
        withContent { WrappedSize(progress = 1F) }

        perform {
            onNodeWithTag(wrapperTag).assertWidthIsEqualTo(30.dp)
            onNodeWithTag(wrapperTag).assertHeightIsEqualTo(70.dp)
        }
    }

    @Test
    fun at_50percent_isCorrect() {
        withContent { WrappedSize(progress = .5F) }

        perform {
            onNodeWithTag(wrapperTag).assertWidthIsEqualTo(15.dp)
            onNodeWithTag(wrapperTag).assertHeightIsEqualTo(35.dp)
        }
    }

    @Composable
    private fun WrappedSize(progress: Float) {
        Box(Modifier.testTag(wrapperTag)) {
            Box(Modifier.wrapContentSize(progress, progress)) {
                Box(Modifier.size(width = 30.dp, height = 70.dp))
            }
        }
    }
}
