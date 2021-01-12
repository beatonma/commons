package org.beatonma.commons.testcompose.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule


abstract class ComposeTest {
    @get:Rule
    val composeTestRule: ComposeTestRule = createComposeRule()

    abstract fun withContent(content: @Composable () -> Unit): ComposeTestRule

    infix fun perform(actions: ComposeTestRule.() -> Unit) {
        composeTestRule.actions()
    }
}
