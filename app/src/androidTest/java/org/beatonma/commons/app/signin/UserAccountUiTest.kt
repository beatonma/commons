package org.beatonma.commons.app.signin

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import org.beatonma.commons.app.ui.compose.components.image.AvatarTestTag
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

@MediumTest
class UserAccountUiTest: UserAccountComposeTest() {
    private val fabTag = TestTag.Fab
    private val accountSheetTag = UserAccountTestTag.UserAccountSheet
    private val signOutTag = UserAccountTestTag.SignOut
    private val deleteAccountTag = UserAccountTestTag.DeleteAccount
    private val editUsernameButtonTag = UserAccountTestTag.UsernameEdit
    private val deleteUiTag = UserAccountTestTag.DeleteAccountUI

    private val username = SampleUserToken.username
    private val email = SampleUserToken.email!!
    private val name = SampleUserToken.name!!

    init {
        check(username.isNotBlank())
        check(email.isNotBlank())
        check(name.isNotBlank())
    }


    @Test
    fun defaultLayout_shouldBeFabWithUsername() {
        withContent {
            TestLayout(
                rememberUserToken(SampleUserToken)
            )
        }

        perform {
            onNodeWithText(username)
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }

    @Test
    fun clickOnAccountFab_shouldOpenAccountProfileUI() {
        withContent {
            TestLayout(
                rememberUserToken(SampleUserToken)
            )
        }

        perform {
            onNodeWithTag(fabTag)
                .assertIsDisplayed()
                .performClick()

            onNodeWithTag(accountSheetTag)
                .assertIsDisplayed()

            onNodeWithTag(deleteUiTag)
                .assertDoesNotExist()
        }
    }

    @Test
    fun accountProfileUi_hasRequiredData() {
        withContent {
            TestLayout(
                rememberUserToken(SampleUserToken)
            )
        }

        setUp {
            onNodeWithTag(fabTag)
                .performClick()
        }

        perform {
            onNodeWithTag(AvatarTestTag)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(username)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(email, substring = true)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText(name, substring = true)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun accountProfileUi_hasRequiredActions() {
        withContent {
            TestLayout(
                rememberUserToken(SampleUserToken)
            )
        }

        setUp {
            onNodeWithTag(fabTag)
                .performClick()
        }

        perform {
            onNodeWithTag(deleteAccountTag)
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithTag(signOutTag)
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithTag(editUsernameButtonTag)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }

    @Test
    fun accountProfileUi_clickSignOut_triggersSignOut() {
        val token = mutableStateOf(SampleUserToken)

        withContent {
            TestLayout(token)
        }

        setUp {
            onNodeWithTag(fabTag)
                .performClick()
        }

        perform {
            onNodeWithTag(signOutTag)
                .performClick()

            token.value shouldbe NullUserToken
        }
    }

    @Test
    fun accountProfileUi_clickDelete_opensDeleteConfirmationUi() {
        withContent {
            TestLayout(
                rememberUserToken(SampleUserToken)
            )
        }

        setUp {
            onNodeWithTag(fabTag)
                .performClick()
        }

        perform {
            onNodeWithTag(deleteAccountTag)
                .performClick()

            onNodeWithTag(deleteUiTag)
                .assertExists()
        }
    }

    @Test
    fun accountProfileUi_shouldNotBeCoveredByIME_whenEditingUsername() {
        withContent {
            TestLayout(
                rememberUserToken(SampleUserToken)
            )
        }

        setUp {
            onNodeWithTag(fabTag)
                .performClick()
        }

        perform {
            onNodeWithTag(editUsernameButtonTag)
                .performClick()

            onNodeWithTag(EditableState.Editable)
                .assertIsDisplayed()
        }
    }
}
