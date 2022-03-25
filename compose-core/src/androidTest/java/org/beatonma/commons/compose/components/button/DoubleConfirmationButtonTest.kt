package org.beatonma.commons.compose.components.button

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import org.beatonma.commons.compose.TestLayout
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Before
import org.junit.Test

@MediumTest
class DoubleConfirmationButtonTest : ComposeTest() {
    private val buttonTag = "double_confirmation_button"
    private val safeContentText = "Nothing has happened yet"
    private val awaitingConfirmationText = "Click again to confirm"
    private val actionConfirmedText = "Action has been triggered!"
    private var onClickHasBeenTriggered by mutableStateOf(false)
    private val onClick = { onClickHasBeenTriggered = true }


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

            onNodeWithText(safeContentText)
                .assertIsDisplayed()
            onNodeWithText(awaitingConfirmationText)
                .assertDoesNotExist()
            onNodeWithText(actionConfirmedText)
                .assertDoesNotExist()
            onNodeWithText("false")
                .assertIsDisplayed()
        }
    }

    @Test
    fun state_afterOneClick_showsCorrectContent_and_hasNotTriggeredClickAction() {
        val state = mutableStateOf(ConfirmationState.Safe)
        withContent {
            TestLayout(state)
        }

        perform {
            onNodeWithTag(buttonTag)
                .performClick()

            onNodeWithText(safeContentText)
                .assertDoesNotExist()
            onNodeWithText(awaitingConfirmationText)
                .assertIsDisplayed()
            onNodeWithText(actionConfirmedText)
                .assertDoesNotExist()

            onNodeWithText("false")
                .assertIsDisplayed()
        }
    }

    @Test
    fun state_afterOneClick_afterAutoCollapseTimeout_revertsToSafeState() {
        val state = mutableStateOf(ConfirmationState.Safe)

        withContent {
            TestLayout(state, autoCollapse = 1500)
        }

        setUp {
            onNodeWithTag(buttonTag)
                .performClick()

            onNodeWithText(safeContentText)
                .assertDoesNotExist()
            onNodeWithText(awaitingConfirmationText)
                .assertIsDisplayed()
            onNodeWithText(actionConfirmedText)
                .assertDoesNotExist()
        }

        perform {
            waitUntil(2000) {
                state.value == ConfirmationState.Safe
            }

            onNodeWithText(safeContentText)
                .assertIsDisplayed()
        }
    }

    @Test
    fun autoCollapseJob_isCancelled_whenStateIsComplete() {
        val state = mutableStateOf(ConfirmationState.Safe)
        val coroutineScope = CoroutineScope(Dispatchers.Default)

        withContent {
            TestLayout(state, autoCollapse = 2500, coroutineScope = coroutineScope)
        }

        setUp {
            onNodeWithTag(buttonTag)
                .performClick()

            onNodeWithText(awaitingConfirmationText)
                .assertIsDisplayed()

        }

        perform {
            onNodeWithTag(buttonTag)
                .performClick() // Confirm

            onNodeWithText(actionConfirmedText)
                .assertIsDisplayed()

            onNodeWithText(actionConfirmedText)
                .assertIsDisplayed()

            // Scope should be cancelled when action is confirmed.
            assert(!coroutineScope.isActive)
            assert(state.value == ConfirmationState.Confirmed)
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

            onNodeWithText("true")
                .assertIsDisplayed()
            onNodeWithText(actionConfirmedText)
                .assertIsDisplayed()
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

            onNodeWithText("true")
                .assertExists()
            onNodeWithText(actionConfirmedText)
                .assertExists()
        }
    }

    @Composable
    private fun TestLayout(
        state: MutableState<ConfirmationState> = mutableStateOf(ConfirmationState.Safe),
        autoCollapse: Long = 2500L,
        coroutineScope: CoroutineScope = rememberCoroutineScope(),
    ) {
        TestLayout {
            Column {
                DoubleConfirmationButton(
                    onClick = onClick,
                    state = state.value,
                    onStateChange = { state.value = it },
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
                    coroutineScope = coroutineScope,
                    modifier = Modifier.testTag(buttonTag),
                )
                Text(onClickHasBeenTriggered.toString())
            }
        }
    }
}
