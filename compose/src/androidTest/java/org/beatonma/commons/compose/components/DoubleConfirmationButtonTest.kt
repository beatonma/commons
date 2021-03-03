package org.beatonma.commons.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Before
import org.junit.Test

class DoubleConfirmationButtonTest : ComposeTest() {
    private val buttonTag = "double_confirmation_button"
    private val safeContentText = "Nothing has happened yet"
    private val awaitingConfirmationText = "Click again to confirm"
    private val actionConfirmedText = "Action has been triggered!"
    private var onClickHasBeenTriggered by mutableStateOf(false)
    private val onClick = { onClickHasBeenTriggered = true }

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }


    @Before
    fun setUp() {
        onClickHasBeenTriggered = false
    }

    @Test
    fun state_default_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(buttonTag)
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithText(safeContentText).assertIsDisplayed()
            onNodeWithText(awaitingConfirmationText).assertDoesNotExist()
            onNodeWithText(actionConfirmedText).assertDoesNotExist()
            onNodeWithText("false").assertIsDisplayed()
        }
    }

    @Test
    fun state_afterOneClick_showsCorrectContent_and_hasNotTriggeredClickAction() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(buttonTag)
                .performClick()

            onNodeWithText(safeContentText).assertDoesNotExist()
            onNodeWithText(awaitingConfirmationText).assertIsDisplayed()
            onNodeWithText(actionConfirmedText).assertDoesNotExist()

            onNodeWithText("false").assertIsDisplayed()
        }
    }

    /**
     * TODO Currently failing - not sure how to handle waiting for autoCollapse coroutine
     *      to complete in this context
     */
    @Test
    fun state_afterOneClick_afterAutoCollapseTimeout_revertsToSafeState() {
        val state = mutableStateOf(ConfirmationState.Safe)

        withContent {
            TestLayout(state, autoCollapse = 400)
        }

        perform {
            runBlocking {
                onNodeWithTag(buttonTag)
                    .performClick()

                async { delay(1000) }.await()
                mainClock.advanceTimeBy(1000)
                waitUntil(1000) { state.value == ConfirmationState.Safe }

                onNodeWithText(awaitingConfirmationText).assertIsDisplayed()

                onNodeWithText(safeContentText).assertIsDisplayed()
            }
        }
    }

    @Test
    fun state_afterConfirmationClick_hasTriggeredClickAction() {
        val state = mutableStateOf(ConfirmationState.AwaitingConfirmation)

        withContent {
            TestLayout(state)
        }

        perform {
            onNodeWithTag(buttonTag)
                .assertHasClickAction()
                .performClick() // Confirm

            onNodeWithText("true").assertIsDisplayed()
            onNodeWithText(actionConfirmedText).assertIsDisplayed()
        }
    }

    @Test
    fun state_afterTwoClicks_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(buttonTag)
                .assertHasClickAction()
                .performClick() // Initiate
                .performClick() // Confirm

            onRoot().printToLog("afterConfirmationClick")

            onNodeWithText("true").assertExists()
            onNodeWithText(actionConfirmedText).assertExists()
        }
    }

    @Composable
    private fun TestLayout(
        state: MutableState<ConfirmationState> = mutableStateOf(ConfirmationState.Safe),
        autoCollapse: Long = 2500L,
    ) {
        Column {
            DoubleConfirmationButton(
                state = state,
                onClick = onClick,
                safeContent = {
                    Text(safeContentText)
                },
                awaitingConfirmationContent = {
                    Text(awaitingConfirmationText)
                },
                confirmedContent = {
                    Text(actionConfirmedText)
                },
                autoCollapse = autoCollapse,
                modifier = Modifier.testTag(buttonTag),
            )
            Text(onClickHasBeenTriggered.toString(), Modifier.testTag("onclick_monitor"))
        }
    }
}
