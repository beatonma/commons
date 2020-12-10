package org.beatonma.commons.app.signin.compose

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.ProvidableAmbient
import org.beatonma.commons.app.signin.UserAccountViewModel

/**
 * Initiate value in the activity from which you are calling [UserProfileActions.registerSignInLauncher]
 */
lateinit var AmbientUserProfileActions: ProvidableAmbient<UserProfileActions>

interface UserProfileActions {
    val signInLauncher: ActivityResultLauncher<Intent>
    val userAccountActions: UserAccountActions

    fun handleSignInResult(data: Intent?)

    private val resultHandler
        get() = { activityResult: ActivityResult ->
            handleSignInResult(activityResult.data)
        }

    fun registerSignInLauncher(host: ComponentActivity) =
        host.registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            resultHandler)

    fun defaultSignInActions(viewmodel: UserAccountViewModel) = UserAccountActions(
        signIn = { googleSignIn(viewmodel.signInIntent) },
        signOut = viewmodel::signOut,
        renameAccount = viewmodel::requestRename,
        deleteAccount = viewmodel::deleteAccount,
    )

    private fun googleSignIn(intent: Intent) {
        signInLauncher.launch(intent)
    }

    private fun googleSignOut() {
        println("googleSignOut()")
//        TODO()
    }

    private fun deleteAccount() {
        println("deleteAccount()")
    }
}

fun UserProfileActions(
    activity: ComponentActivity,
    viewmodel: UserAccountViewModel,
) = object : UserProfileActions {
    override val signInLauncher: ActivityResultLauncher<Intent> = registerSignInLauncher(activity)
    override val userAccountActions: UserAccountActions = defaultSignInActions(viewmodel)

    override fun handleSignInResult(data: Intent?) {
        viewmodel.getTokenFromSignInResult(data)
    }
}
