package org.beatonma.commons.testcompose.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule


abstract class ComposeTest {
    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    /**
     * Set content for [composeTestRule].
     */
    abstract fun withContent(content: @Composable () -> Unit): ComposeContentTestRule

    /**
     * Structural helper. Run the given [actions] on [composeTestRule] to get the UI to
     * the state required for the test. Assertions should not be made here - see [perform].
     */
    infix fun setUp(actions: ComposeContentTestRule.() -> Unit) {
        composeTestRule.actions()
    }

    /**
     * Run the given [actions] on composeTestRule.
     *
     * Run any gestures that are directly related to what you are testing and make
     * your assertions here.
     */
    infix fun perform(actions: ComposeContentTestRule.() -> Unit) {
        composeTestRule.actions()
    }

    fun <E, T: Enum<E>> ComposeTestRule.onNodeWithTag(enum: T, useUnmergedTree: Boolean = false) =
        onNodeWithTag(enum.name, useUnmergedTree = useUnmergedTree)

    /**
     * Print UI state to log in both unmerged and merged forms to help identify
     * unexpected merge points.
     */
    fun dump(label: String = "dump") {
        println("[printToLog] Unmerged tree:")
        composeTestRule.onRoot(useUnmergedTree = true).printToLog(label)
        println("[printToLog] -")
        println("[printToLog] Merged tree:")
        composeTestRule.onRoot(useUnmergedTree = false).printToLog(label)
        println("[printToLog] =======")
    }
}
