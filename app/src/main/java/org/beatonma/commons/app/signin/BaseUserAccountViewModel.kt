package org.beatonma.commons.app.signin

import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import org.beatonma.commons.app
import org.beatonma.commons.context
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.repository.UserAccount
import org.beatonma.commons.repo.repository.UserRepository

private const val TAG = "BaseUserAccountVM"

abstract class BaseUserAccountViewModel constructor(
    protected val repository: UserRepository,
    application: Context,
): AndroidViewModel(application.app) {

    var activeUserToken: LiveDataIoResult<UserToken>? = null

    /**
     * If user is signed in with their Google account, use it to retrieve a UserToken that
     * will enable interaction social features of Commons.
     */
    fun getTokenForCurrentSignedInAccount() = withCurrentGoogleAccount { userAccount ->
        getTokenForAccount(userAccount)
    }

    protected fun getTokenForAccount(account: UserAccount) {
        activeUserToken = repository.getTokenForAccount(account).asLiveData()
    }

    fun refreshTokenForCurrentAccount() = withCurrentGoogleAccount { account ->
        activeUserToken = repository.forceGetTokenForAccount(account).asLiveData()
    }

    private inline fun withCurrentGoogleAccount(block: (userAccount: UserAccount) -> Unit) {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(context)
        val userAccount = currentGoogleAccount?.toUserAccount()
        if (userAccount != null) {
            block.invoke(userAccount)
        }
    }
}


fun GoogleSignInAccount.toUserAccount(): UserAccount? =
    try {
        UserAccount(
            name = displayName,
            photoUrl = photoUrl?.toString(),
            email = email,
            googleId = id!!,
            googleIdToken = idToken!!,
        )
    }
    catch (e: NullPointerException) {
        Log.w(TAG, "Unable to retrieve required ID from account.")
        null
    }
