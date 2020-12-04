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
    val signInActions: SignInActions

    private val resultHandler
        get() = { activityResult: ActivityResult ->
            handleSignInResult(activityResult.data)
        }

    fun registerSignInLauncher(host: ComponentActivity) =
        host.registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            resultHandler)

    fun defaultSignInActions(signinIntent: Intent) = SignInActions(
        signIn = { googleSignIn(signinIntent) },
        signOut = ::googleSignOut,
        renameAccount = ::requestNewUsername,
        deleteAccount = { /* TODO("deleteAccount action has not been implemented") */ }
    )

    private fun requestNewUsername(newName: String) {
//        TODO()
    }

    fun googleSignIn(intent: Intent) {
        println("googleSignIn()")
        signInLauncher.launch(intent)
    }

    private fun googleSignOut() {
//        TODO()
    }

    fun handleSignInResult(data: Intent?)
}

fun UserProfileActions(
    activity: ComponentActivity,
    viewmodel: UserAccountViewModel,
) = object : UserProfileActions {
    override val signInLauncher: ActivityResultLauncher<Intent> = registerSignInLauncher(activity)
    override val signInActions: SignInActions = defaultSignInActions(viewmodel.signInIntent)

    override fun handleSignInResult(data: Intent?) {
        viewmodel.getTokenFromSignInResult(data)
    }
}
