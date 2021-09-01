package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

class WrapContentWidthTest: ComposeTest() {
    private val wrapperTag = "wrapper"


    @Test
    fun at_0percent_isCorrect() {
        withContent { WrappedWidth(progress = 0F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(0.dp)
        }
    }

    @Test
    fun at_100percent_isCorrect() {
        withContent { WrappedWidth(progress = 1F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(60.dp)
        }
    }

    @Test
    fun at_50percent_isCorrect() {
        withContent { WrappedWidth(progress = .5F) }

        perform {
            onNodeWithTag(wrapperTag)
                .assertWidthIsEqualTo(30.dp)
        }
    }

    @Composable
    private fun WrappedWidth(progress: Float) {
        Box(Modifier.testTag(wrapperTag)) {
            Box(Modifier.wrapContentWidth(progress)) {
                Box(Modifier.size(60.dp))
            }
        }
    }
}
