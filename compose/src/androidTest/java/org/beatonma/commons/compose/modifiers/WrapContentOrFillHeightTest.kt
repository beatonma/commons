package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class WrapContentOrFillHeightTest: ComposeTest() {
    private val wrapperTag = "wrapper"
    private val contentTag = "content"


    @Test
    fun at_0percent_isCorrect() {
        withContent { WrappedContentOrFillHeight(progress = 0F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertHeightIsEqualTo(10.dp)
        }
    }

    @Test
    fun at_100percent_isCorrect() {
        withContent { WrappedContentOrFillHeight(progress = 1F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertHeightIsEqualTo(100.dp)
        }
    }

    @Test
    fun at_50percent_isCorrect() {
        withContent { WrappedContentOrFillHeight(progress = .5F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertHeightIsEqualTo(55.dp)
        }
    }

    @Test
    fun at_5percent_isCorrect() {
        withContent { WrappedContentOrFillHeight(progress = .05F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertHeightIsEqualTo(14.25.dp)
        }
    }


    @Composable
    private fun WrappedContentOrFillHeight(progress: Float) {
        Box(
            Modifier
                .height(100.dp)
        ) {
            Box(
                Modifier
                    .testTag(wrapperTag)
                    .wrapContentOrFillHeight(progress)
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
