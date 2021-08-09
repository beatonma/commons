package org.beatonma.commons.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class ModalTest: ComposeTest() {

    @Test
    fun contentShouldNotBeMerged() {
        withContent {
            TestLayout(visible = true)
        }

        perform {
            onNodeWithTag(TestTag.ModalScrim)
                .assertExists()
                .assertHasClickAction()

            onNodeWithText("Content")
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun shouldBeInaccessibleWhen_visibleIsFalse () {
        withContent {
            TestLayout(visible = false)
        }

        perform {
            onNodeWithTag(TestTag.ModalScrim)
                .assertDoesNotExist()
        }
    }

    @Test
    fun clickOnScrim_shouldTriggerOnClickAction() {
        val backgroundClickCount = mutableStateOf(0)

        withContent {
            TestLayout(
                backgroundClickCount = backgroundClickCount,
                visible = true
            )
        }

        perform {
            onNodeWithTag(TestTag.ModalScrim)
                .performClick()

            backgroundClickCount.value shouldbe 1
        }
    }

    @Composable
    fun TestLayout(
        visible: Boolean = true,
        backgroundClickCount: MutableState<Int> = remember { mutableStateOf(0) }
    ) {
        ModalScrim(
            onClickLabel = "dismiss",
            onClickAction = { backgroundClickCount.value += 1 },
            visible = visible,
        ) {
            Column {
                Text("Content")
            }
        }
    }
}
