package org.beatonma.commons.app.signin

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.app
import org.beatonma.commons.context
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.core.repository.UserAccount
import org.beatonma.commons.data.core.repository.UserRepository
import org.beatonma.commons.data.core.repository.toUserAccount
import org.beatonma.commons.data.core.room.entities.user.UserToken

private const val TAG = "SignInViewModel"

class SignInViewModel @ViewModelInject constructor(
    private val repository: UserRepository,
    private val googleSignInClient: GoogleSignInClient,
    @ApplicationContext application: Context,
): AndroidViewModel(application.app) {

    val signInIntent: Intent get() = googleSignInClient.signInIntent

    var activeToken: LiveDataIoResult<UserToken>? = null

    /**
     * If user is signed in with their Google account, use it to retrieve a UserToken that
     * will enable interaction social features of Commons.
     */
    fun getTokenForCurrentSignedInAccount() {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(context)
        val userAccount = currentGoogleAccount?.toUserAccount()
        if (userAccount != null) {
            getTokenForAccount(userAccount)
        }
    }

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
        activeToken = null
        return googleSignInClient.signOut()
    }

    private fun getTokenForAccount(account: UserAccount) {
        activeToken = repository.getTokenForAccount(account).asLiveData()
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
