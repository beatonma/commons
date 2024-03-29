package org.beatonma.commons.app.ui.screens.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.app.TestTheme
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.testcompose.test.ComposeTest

/**
 * Shared TestLayout for [UserAccountUiTest] and [SignInUiTest].
 */
abstract class UserAccountComposeTest : ComposeTest() {
    @Composable
    internal fun UserAccountTestLayout(
        userToken: MutableState<UserToken>,
        deleted: MutableState<Boolean> = remember { mutableStateOf(false) },
    ) {
        val userAccountActions = remember {
            UserAccountActions(
                signIn = { userToken.value = SampleUserToken },
                signOut = { userToken.value = NullUserToken },
                renameAccount = { _, _ -> RenameResult.ACCEPTED },
                deleteAccount = { deleted.value = true }
            )
        }

        TestTheme {
            UserAccountFabUi(userToken.value, userAccountActions)
        }
    }

    @Composable
    protected fun rememberUserToken(token: UserToken) = remember { mutableStateOf(token) }
}
