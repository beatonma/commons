package org.beatonma.commons.app.ui.screens.signin

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class DeleteAccountUiTest: UserAccountComposeTest() {
    private val fabTag = TestTag.Fab
    private val deleteAccountTag = UserAccountTestTag.DeleteAccount
    private val accountSheetTag = UserAccountTestTag.UserAccountSheet

    private val confirmTag = UserAccountTestTag.DeleteAccount
    private val cancelTag = TestTag.Cancel

    /**
     * Starting fromm FAB, make actions necessary to show the account deletion UI.
     */
    private fun setupUiState() {
        setUp {
            onNodeWithTag(fabTag)
                .performClick()

            onNodeWithTag(deleteAccountTag)
                .performClick()
        }
    }

    @Test
    fun requiredAction_areAvailable() {
        withContent {
            UserAccountTestLayout(
                rememberUserToken(SampleUserToken)
            )
        }

        setupUiState()

        perform {
            onNodeWithTag(confirmTag)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithTag(cancelTag)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }

    @Test
    fun singleConfirmation_shouldNotTriggerDeleteAction() {
        val deleted = mutableStateOf(false)

        withContent {
            UserAccountTestLayout(
                rememberUserToken(SampleUserToken),
                deleted = deleted
            )
        }

        setupUiState()

        perform {
            onNodeWithTag(confirmTag)
                .performClick()

            deleted.value shouldbe false
        }
    }

    @Test
    fun doubleConfirmation_shouldTriggerDeleteAction() {
        val deleted = mutableStateOf(false)

        withContent {
            UserAccountTestLayout(
                rememberUserToken(SampleUserToken),
                deleted = deleted
            )
        }

        setupUiState()

        perform {
            onNodeWithTag(confirmTag)
                .performClick()
                .performClick()

            deleted.value shouldbe true
        }
    }

    @Test
    fun clickCancel_shouldReturnToMainAccountUi() {
        withContent {
            UserAccountTestLayout(
                rememberUserToken(SampleUserToken),
            )
        }

        setupUiState()

        perform {
            onNodeWithTag(cancelTag)
                .performClick()

            onNodeWithTag(accountSheetTag)
                .assertIsDisplayed()
        }
    }
}
