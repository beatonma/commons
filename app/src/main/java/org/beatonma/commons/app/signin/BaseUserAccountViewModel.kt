package org.beatonma.commons.app.signin

import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.beatonma.commons.app
import org.beatonma.commons.context
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.repository.GoogleAccount
import org.beatonma.commons.repo.repository.UserRepository
import org.beatonma.commons.repo.result.onSuccess

private const val TAG = "BaseUserAccountVM"

@Deprecated("Use SignInViewModel")
abstract class BaseUserAccountViewModel(
    protected val repository: UserRepository,
    application: Context,
) : AndroidViewModel(application.app) {

    init {
        getTokenForCurrentSignedInAccountFlow()
    }

    private val _userTokenLiveData = MutableLiveData(NullUserToken)
    val userTokenLiveData: LiveData<UserToken> = _userTokenLiveData

    /**
     * If user is signed in with their Google account, use it to retrieve a UserToken that
     * will enable interaction social features of Commons.
     */
    fun getTokenForCurrentSignedInAccountFlow() =
        withCurrentGoogleAccount(default = null) { account ->
            repository.getTokenForAccount(account).cacheTokenResult()
        }

    protected fun getTokenForAccount(account: GoogleAccount) {
        repository.getTokenForAccount(account).cacheTokenResult()
    }

    /**
     * Force a refresh from server.
     */
    @Deprecated("")
    fun refreshTokenForCurrentAccount() {
//        withCurrentGoogleAccount { account ->
//            repository.forceGetTokenForAccount(account).cacheTokenResult()
//        }
    }

    private inline fun <T> withCurrentGoogleAccount(
        default: T,
        block: (googleAccount: GoogleAccount) -> T,
    ): T {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(context)
        val userAccount = currentGoogleAccount?.asGoogleAccount()
        println("withGoogleAccount $userAccount")
        if (userAccount != null) {
            return block.invoke(userAccount)
        }
        return default
    }

    private inline fun withCurrentGoogleAccount(block: (googleAccount: GoogleAccount) -> Unit) {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(context)
        val userAccount = currentGoogleAccount?.asGoogleAccount()
        println("withGoogleAccount $userAccount")
        if (userAccount != null) {
            block.invoke(userAccount)
        }
    }

    private fun ResultFlow<UserToken>.cacheTokenResult() {
        viewModelScope.launch {
            collect { result ->
                result.onSuccess { data ->
                    _userTokenLiveData.postValue(data)
                }
            }
        }
    }
}

private fun GoogleSignInAccount.asGoogleAccount(): GoogleAccount? =
    try {
        GoogleAccount(
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
