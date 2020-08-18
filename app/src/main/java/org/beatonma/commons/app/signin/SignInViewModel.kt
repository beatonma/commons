package org.beatonma.commons.app.signin

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.app
import org.beatonma.commons.data.core.repository.UserAccount
import org.beatonma.commons.data.core.repository.UserRepository
import org.beatonma.commons.data.core.repository.toUserAccount

private const val TAG = "SignInViewModel"

/**
 * Implementation of [BaseUserAccountViewModel] which allows signing in/out.
 */
class SignInViewModel @ViewModelInject constructor(
    repository: UserRepository,
    private val googleSignInClient: GoogleSignInClient,
    @ApplicationContext application: Context,
): BaseUserAccountViewModel(repository, application.app) {

    val signInIntent: Intent get() = googleSignInClient.signInIntent

    /**
     * Handle the response from [signInIntent]
     */
    fun getTokenFromSignInResult(data: Intent?) {
        val account = getUserAccountFromSignInResult(data)
        if (account != null) {
            getTokenForAccount(account)
        }
    }

    fun signOut(): Task<Void> {
        activeUserToken = null
        return googleSignInClient.signOut()
    }

    private fun getUserAccountFromSignInResult(data: Intent?): UserAccount? {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            return account?.toUserAccount()
        }
        catch(e: ApiException) {
            Log.w(TAG, "Google sign in failed - code=${e.statusCode}")
        }
        return null
    }
}
