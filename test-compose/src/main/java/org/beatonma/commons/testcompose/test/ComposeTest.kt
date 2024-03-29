package org.beatonma.commons.testcompose.test

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsNodeInteraction
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
    fun withContent(content: @Composable () -> Unit) {
        composeTestRule.apply { setContent(content) }
    }

    /**
     * Structural helper. Run the given [actions] on [composeTestRule] to get the UI to
     * the state required for the test. Assertions should not be made here - see [perform].
     */
    fun setUp(actions: ComposeContentTestRule.() -> Unit) {
        composeTestRule.actions()
    }

    /**
     * Run the given [actions] on composeTestRule.
     *
     * Run any gestures that are directly related to what you are testing and make
     * your assertions here.
     */
    fun perform(actions: ComposeContentTestRule.() -> Unit) {
        composeTestRule.actions()
    }

    fun <E, T : Enum<E>> ComposeTestRule.onNodeWithTag(
        enum: T,
        useUnmergedTree: Boolean = false
    ): SemanticsNodeInteraction =
        onNodeWithTag(enum.name, useUnmergedTree = useUnmergedTree)

    /**
     * Print UI state to log in both unmerged and merged forms to help identify
     * unexpected merge points.
     */
    fun dump(label: String = "default") {
        println("[printToLog $label] Unmerged tree:")
        composeTestRule.onRoot(useUnmergedTree = true).printToLog(label)
        println("[printToLog] -")
        println("[printToLog $label] Merged tree:")
        composeTestRule.onRoot(useUnmergedTree = false).printToLog(label)
        println("[printToLog] =======")
    }
}
