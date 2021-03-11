package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class WrapContentOrFillWidthTest: ComposeTest() {
    private val wrapperTag = "wrapper"
    private val contentTag = "content"

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    @Test
    fun at_0percent_isCorrect() {
        withContent { WrappedContentOrFillWidth(progress = 0F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(10.dp)
        }
    }

    @Test
    fun at_100percent_isCorrect() {
        withContent { WrappedContentOrFillWidth(progress = 1F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(200.dp)
        }
    }

    @Test
    fun at_50percent_isCorrect() {
        withContent { WrappedContentOrFillWidth(progress = .5F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(105.dp)
        }
    }

    @Test
    fun at_5percent_isCorrect() {
        withContent { WrappedContentOrFillWidth(progress = .05F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(19.5.dp)
        }
    }


    @Composable
    private fun WrappedContentOrFillWidth(progress: Float) {
        Box(
            Modifier
                .width(200.dp)
        ) {
            Box(
                Modifier
                    .testTag(wrapperTag)
                    .wrapContentOrFillWidth(progress)
            ) {
                Box(
                    Modifier
                        .testTag(contentTag)
                        .size(10.dp)
                )
            }
        }
    }
}
