package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class WrapContentHeightTest: ComposeTest() {
    private val wrapperTag = "wrapper"

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    @Test
    fun at_0percent_isCorrect() {
        withContent { WrappedHeight(progress = 0F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertHeightIsEqualTo(0.dp)
        }
    }

    @Test
    fun at_100percent_isCorrect() {
        withContent { WrappedHeight(progress = 1F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertHeightIsEqualTo(50.dp)
        }
    }

    @Test
    fun at_50percent_isCorrect() {
        withContent { WrappedHeight(progress = .5F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertHeightIsEqualTo(25.dp)
        }
    }

    @Composable
    private fun WrappedHeight(progress: Float) {
        Box(Modifier.testTag(wrapperTag)) {
            Box(Modifier.wrapContentHeight(progress)) {
                Box(Modifier.size(50.dp))
            }
        }
    }
}
