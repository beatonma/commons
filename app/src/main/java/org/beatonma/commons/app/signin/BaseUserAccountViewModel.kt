package org.beatonma.commons.app.signin

import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.beatonma.commons.app
import org.beatonma.commons.context
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.core.repository.UserAccount
import org.beatonma.commons.data.core.repository.UserRepository
import org.beatonma.commons.data.core.repository.toUserAccount
import org.beatonma.commons.data.core.room.entities.user.UserToken

abstract class BaseUserAccountViewModel constructor(
    private val repository: UserRepository,
    application: Context,
): AndroidViewModel(application.app) {

    var activeUserToken: LiveDataIoResult<UserToken>? = null

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

    protected fun getTokenForAccount(account: UserAccount) {
        activeUserToken = repository.getTokenForAccount(account).asLiveData()
    }
}
