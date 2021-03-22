package org.beatonma.commons.app.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.filters.MediumTest
import org.beatonma.commons.R
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.hamcrest.core.Is.`is`
import org.junit.Test

@MediumTest
class SignInUiTest: UserAccountComposeTest() {
    private val signInFabTag = "fab_bottomsheet_surface__fab"
    private val signInSheetTag = "sign_in_sheet"
    private val signInButtonTag = "google_signin_button"
    private val accountRationaleTag = "account_rationale"

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    @Test
    fun defaultLayout_shouldBeSignInFab() {
        var signInText: String? = null

        withContent {
            signInText = stringResource(R.string.account_sign_in)

            TestLayout(
                rememberUserToken(NullUserToken)
            )
        }

        perform {
            onNodeWithText(signInText!!)
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }

    @Test
    fun clickOnSignInFab_shouldOpenSignInUi() {
        withContent {
            TestLayout(
                rememberUserToken(NullUserToken)
            )
        }

        perform {
            onNodeWithTag(signInFabTag)
                .performClick()

            onNodeWithTag(signInSheetTag)
                .assertIsDisplayed()
        }
    }

    @Test
    fun signInSheet_isCorrect() {
        withContent {
            TestLayout(
                rememberUserToken(NullUserToken)
            )
        }

        setUp {
            onNodeWithTag(signInFabTag)
                .performClick()
        }

        perform {
            onNodeWithTag(accountRationaleTag)
                .assertIsDisplayed()

            onNodeWithTag(signInButtonTag)
                .assertIsDisplayed()
        }
    }

    @Test
    fun clickSignIn_shouldTriggerSignInAction() {
        val token = mutableStateOf(NullUserToken)
        withContent {
            TestLayout(token)
        }

        setUp {
            onNodeWithTag(signInFabTag)
                .performClick()
        }

        perform {
            Espresso.onView(withClassName(`is`("com.google.android.gms.common.SignInButton")))
                .perform(click())

            token.value shouldbe SampleUserToken
        }
    }
}
