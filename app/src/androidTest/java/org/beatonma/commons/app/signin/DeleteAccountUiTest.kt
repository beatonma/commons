package org.beatonma.commons.app.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class DeleteAccountUiTest: UserAccountComposeTest() {
    private val fabTag = "fab_bottomsheet_surface__fab"
    private val deleteAccountTag = "action_delete_account"
    private val accountSheetTag = "user_account_sheet"

    private val confirmTag = "action_confirm_delete"
    private val cancelTag = "action_cancel"

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

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
            TestLayout(
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
            TestLayout(
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
            TestLayout(
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
            TestLayout(
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
