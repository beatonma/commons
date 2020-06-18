package org.beatonma.commons.app.signin

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.commonsApp
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.core.repository.UserAccount
import org.beatonma.commons.data.core.repository.UserRepository
import org.beatonma.commons.data.core.repository.toUserAccount
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.kotlin.extensions.dump
import javax.inject.Inject

private const val TAG = "SignInViewModel"

/**
 * Return the registered snommoc.org user token for the current signed-in user, or null if
 * it is not available.
 */
class SignInViewModel @Inject constructor(
    private val repository: UserRepository,
    application: CommonsApplication,
): AndroidViewModel(application) {

    var activeToken: LiveDataIoResult<UserToken>? = null

    fun getTokenForCurrentSignedInAccount(): LiveDataIoResult<UserToken>? {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(commonsApp)
        val userAccount = currentGoogleAccount?.toUserAccount()
        if (userAccount != null) {
            return getTokenForAccount(userAccount)
        }

        return null
    }

    fun getTokenForAccount(account: UserAccount): LiveDataIoResult<UserToken>? {
        activeToken = repository.observeSignedInUser(account)
        return activeToken
    }

    fun getGoogleAccountFromSignInResult(completedTask: Task<GoogleSignInAccount>): UserAccount? {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.run {
                idToken.dump("idToken")
                id.dump("id")
                email.dump("email")
                displayName.dump("name")
                givenName.dump("givenName")
                familyName.dump("familyName")
                photoUrl.dump("photo")
                grantedScopes.forEach { it.dump("scope") }
            }
            return account?.toUserAccount()
        }
        catch(e: ApiException) {
            Log.w(TAG, "Google sign in failed - code=${e.statusCode}")
        }
        return null
    }
}
