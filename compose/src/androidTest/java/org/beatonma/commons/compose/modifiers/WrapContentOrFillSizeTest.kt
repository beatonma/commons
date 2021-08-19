package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class WrapContentOrFillSizeTest: ComposeTest() {
    private val wrapperTag = "wrapper"
    private val contentTag = "content"


    @Test
    fun at_0_percent_isCorrect() {
        withContent { WrappedContentOrFillSize(progress = 0F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(10.dp)
                .assertHeightIsEqualTo(10.dp)
        }
    }

    @Test
    fun at_100_percent_isCorrect() {
        withContent { WrappedContentOrFillSize(progress = 1F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(200.dp)
                .assertHeightIsEqualTo(100.dp)
        }
    }

    @Test
    fun at_50_percent_isCorrect() {
        withContent { WrappedContentOrFillSize(progress = .5F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(105.dp)
                .assertHeightIsEqualTo(55.dp)
        }
    }

    @Test
    fun at_5_percent_isCorrect() {
        withContent { WrappedContentOrFillSize(progress = .05F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(19.5.dp)
                .assertHeightIsEqualTo(14.25.dp)
        }
    }


    @Composable
    private fun WrappedContentOrFillSize(progress: Float) {
        Box(
            Modifier
                .width(200.dp)
                .height(100.dp)
        ) {
            Box(
                Modifier
                    .testTag(wrapperTag)
                    .wrapContentOrFillSize(progress, progress)
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
