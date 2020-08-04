package org.beatonma.commons.app.signin

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
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

/**
 * Return the registered snommoc.org user token for the current signed-in user, or null if
 * it is not available.
 */
class SignInViewModel @ViewModelInject constructor(
    private val repository: UserRepository,
    val googleSignInClient: GoogleSignInClient,
    @ApplicationContext application: Context,
): AndroidViewModel(application.app) {

    val signInIntent: Intent get() = googleSignInClient.signInIntent
    var activeToken: LiveDataIoResult<UserToken>? = null

    fun getTokenForCurrentSignedInAccount(): LiveDataIoResult<UserToken>? {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(context)
        val userAccount = currentGoogleAccount?.toUserAccount()
        if (userAccount != null) {
            return getTokenForAccount(userAccount)
        }

        return null
    }

    fun getTokenForAccount(account: UserAccount): LiveDataIoResult<UserToken>? {
        activeToken = repository.getTokenForAccount(account).asLiveData()
        return activeToken
    }

    fun getGoogleAccountFromSignInResult(completedTask: Task<GoogleSignInAccount>): UserAccount? {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            return account?.toUserAccount()
        }
        catch(e: ApiException) {
            Log.w(TAG, "Google sign in failed - code=${e.statusCode}")
        }
        return null
    }

    fun signOut() = googleSignInClient.signOut()
}
