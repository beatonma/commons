package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.ui.test.ComposeTestRuleJUnit
import androidx.ui.test.assertHeightIsEqualTo
import androidx.ui.test.assertWidthIsEqualTo
import androidx.ui.test.createComposeRule
import androidx.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    WrapContentTest::class,
    WrapContentOrFillTest::class,
)
class SizeModifierTestSuite

private const val WRAPPER = "wrapper"
private const val CONTENT = "content"
private fun ComposeTestRuleJUnit.onWrapper() = onNodeWithText(WRAPPER)
private fun ComposeTestRuleJUnit.onContent() = onNodeWithText(CONTENT)

class WrapContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    // Width only
    @Test
    fun modifier_wrapContentWidth_zero_isCorrect() {
        with(composeRule) {
            setContent { wrappedWidth(progress = 0F) }
            onWrapper().assertWidthIsEqualTo(0.dp)
        }
    }

    @Test
    fun modifier_wrapContentWidth_full_isCorrect() {
        with(composeRule) {
            setContent { wrappedWidth(progress = 1F) }
            onWrapper().assertWidthIsEqualTo(60.dp)
        }
    }

    @Test
    fun modifier_wrapContentWidth_half_isCorrect() {
        with(composeRule) {
            setContent { wrappedWidth(progress = .5F) }
            onWrapper().assertWidthIsEqualTo(30.dp)
        }
    }

    // Height only
    @Test
    fun modifier_wrapContentHeight_zero_isCorrect() {
        with(composeRule) {
            setContent { wrappedHeight(progress = 0F) }
            onWrapper().assertHeightIsEqualTo(0.dp)
        }
    }

    @Test
    fun modifier_wrapContentHeight_full_isCorrect() {
        with(composeRule) {
            setContent { wrappedHeight(progress = 1F) }
            onWrapper().assertHeightIsEqualTo(50.dp)
        }
    }

    @Test
    fun modifier_wrapContentHeight_half_isCorrect() {
        with(composeRule) {
            setContent { wrappedHeight(progress = .5F) }
            onWrapper().assertHeightIsEqualTo(25.dp)
        }
    }

    // Width and height
    @Test
    fun modifier_wrapContentSize_zero_isCorrect() {
        with(composeRule) {
            setContent { wrappedSize(progress = 0F) }
            onWrapper().assertWidthIsEqualTo(0.dp)
            onWrapper().assertHeightIsEqualTo(0.dp)
        }
    }

    @Test
    fun modifier_wrapContentSize_full_isCorrect() {
        with(composeRule) {
            setContent { wrappedSize(progress = 1F) }
            onWrapper().assertWidthIsEqualTo(30.dp)
            onWrapper().assertHeightIsEqualTo(70.dp)
        }
    }

    @Test
    fun modifier_wrapContentSize_half_isCorrect() {
        with(composeRule) {
            setContent { wrappedSize(progress = .5F) }
            onWrapper().assertWidthIsEqualTo(15.dp)
            onWrapper().assertHeightIsEqualTo(35.dp)
        }
    }
}

class WrapContentOrFillTest {
    @get:Rule
    val composeRule = createComposeRule()

    // Width only
    @Test
    fun modifier_wrapContentWidth_zero_isCorrect() {
        with(composeRule) {
            setContent { wrappedContentOrFillWidth(progress = 0F) }
            onWrapper().assertWidthIsEqualTo(10.dp)
        }
    }

    @Test
    fun modifier_wrapContentWidth_full_isCorrect() {
        with(composeRule) {
            setContent { wrappedContentOrFillWidth(progress = 1F) }
            onWrapper().assertWidthIsEqualTo(200.dp)
        }
    }

    @Test
    fun modifier_wrapContentWidth_half_isCorrect() {
        with(composeRule) {
            setContent { wrappedContentOrFillWidth(progress = .1F) }
            onWrapper().assertWidthIsEqualTo((10 + ((200 - 10) / 10)).dp)
        }
    }
}

@Composable
private fun wrappedWidth(progress: Float) {
    Box(Modifier.semantics {
        text = AnnotatedString(WRAPPER)
    }) {
        Box(Modifier.wrapContentWidth(progress)) {
            Box(Modifier.size(60.dp))
        }
    }
}

@Composable
private fun wrappedHeight(progress: Float) {
    Box(Modifier.semantics {
        text = AnnotatedString(WRAPPER)
    }) {
        Box(Modifier.wrapContentHeight(progress)) {
            Box(Modifier.size(50.dp))
        }
    }
}

@Composable
private fun wrappedSize(progress: Float) {
    Box(Modifier.semantics {
        text = AnnotatedString(WRAPPER)
    }) {
        Box(Modifier.wrapContentSize(progress, progress)) {
            Box(Modifier.size(width = 30.dp, height = 70.dp))
        }
    }
}

@Composable
private fun wrappedContentOrFillWidth(progress: Float) {
    Box(
        Modifier.width(200.dp)
    ) {
        Box(Modifier
            .semantics {
                text = AnnotatedString(WRAPPER)
            }
            .wrapContentOrFillWidth(progress)
        ) {
            Box(
                Modifier
                    .semantics {
                        text = AnnotatedString(CONTENT)
                    }
                    .size(10.dp)
            )
        }
    }
}
