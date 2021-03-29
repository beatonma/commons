package org.beatonma.commons.app.signin

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Initiate value in the activity from which you are calling [PlatformUserAccountActions.registerSignInLauncher]
 */
lateinit var LocalPlatformUserAccountActions: CompositionLocal<PlatformUserAccountActions>
/**
 * Initialised by [userProfileActions] which should be called from your host activity.
 */
lateinit var LocalUserAccountActions: CompositionLocal<UserAccountActions>

/**
 * Handle platform interactions required for [UserAccountActions].
 */
interface PlatformUserAccountActions {
    val signInLauncher: ActivityResultLauncher<Intent>

    fun handleSignInResult(data: Intent?)

    private val resultHandler
        get() = { activityResult: ActivityResult ->
            handleSignInResult(activityResult.data)
        }

    fun registerSignInLauncher(host: ComponentActivity) =
        host.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            resultHandler
        )

    fun defaultAccountActions(viewmodel: UserAccountViewModel) =
        UserAccountActions(
            signIn = { googleSignIn(viewmodel.signInIntent) },
            signOut = viewmodel::signOut,
            renameAccount = viewmodel::requestRename,
            deleteAccount = viewmodel::deleteAccount,
        )

    private fun googleSignIn(intent: Intent) {
        signInLauncher.launch(intent)
    }
}

/**
 * Call from your host activity.
 * The result from this function should be stored and made available via [LocalPlatformUserAccountActions].
 * [LocalUserAccountActions]
 */
fun userProfileActions(
    activity: ComponentActivity,
    viewmodel: UserAccountViewModel,
) = object : PlatformUserAccountActions {
    override val signInLauncher: ActivityResultLauncher<Intent> = registerSignInLauncher(activity)

    init {
        LocalUserAccountActions = staticCompositionLocalOf { defaultAccountActions(viewmodel) }
    }

    override fun handleSignInResult(data: Intent?) {
        viewmodel.getTokenFromSignInResult(data)
    }
}
