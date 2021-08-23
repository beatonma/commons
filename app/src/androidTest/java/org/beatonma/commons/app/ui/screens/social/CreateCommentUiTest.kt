package org.beatonma.commons.app.ui.screens.social

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.MediumTest
import com.google.accompanist.insets.ProvideWindowInsets
import org.beatonma.commons.app.ui.screens.signin.LocalUserAccountActions
import org.beatonma.commons.app.ui.screens.signin.NullUserToken
import org.beatonma.commons.app.ui.screens.signin.UserAccountActions
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class CreateCommentUiTest: ComposeTest() {

    @Test
    fun whenSignedOut_collapsedLayout_shouldShowSignInFab() {
        withContent {
            TestLayout(
                rememberSocialUiState(SocialUiState.Collapsed),
                NullUserToken,
            )
        }

        perform {
            onNodeWithTag(TestTag.Fab)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertTextContains("sign in", substring = true, ignoreCase = true)
        }
    }

    @Test
    fun whenSignedIn_collapsedLayout_shouldShowCreateCommentFab() {
        withContent {
            TestLayout(
                rememberSocialUiState(SocialUiState.Collapsed),
            )
        }

        perform {
            onNodeWithTag(TestTag.Fab)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertTextContains("comment", substring = true, ignoreCase = true)
        }
    }

    @Test
    fun whenSignedIn_expandedLayout_isCorrect() {
        withContent {
            TestLayout(
                rememberSocialUiState(SocialUiState.ComposeComment),
            )
        }

        perform {
            onNodeWithText(SampleUserToken.username)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithTag(TestTag.ValidatedText)
                .assertExists()
                .assertIsDisplayed()
                .assertIsFocused()

            onNodeWithTag(TestTag.Submit)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertIsNotEnabled()
        }
    }

    @Test
    fun submitButton_becomesEnabled_whenCommentIsValid() {
        withContent {
            TestLayout(
                rememberSocialUiState(SocialUiState.ComposeComment),
            )
        }

        perform {
            onNodeWithTag(TestTag.ValidatedText)
                .performTextInput("This is a valid comment!")

            onNodeWithTag(TestTag.Submit)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertIsEnabled()
        }
    }

    @Composable
    private fun TestLayout(
        state: MutableState<SocialUiState>,
        userToken: UserToken = SampleUserToken,
    ) {
        LocalUserAccountActions = staticCompositionLocalOf { UserAccountActions() }

        ProvideWindowInsets {
            CreateCommentUi(
                userToken,
                state
            )
        }
    }
}
