package org.beatonma.commons.app.social

import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.annotations.SignInRequired
import org.beatonma.commons.context
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.NotSignedInError
import org.beatonma.commons.data.core.interfaces.Sociable
import org.beatonma.commons.data.core.repository.*
import org.beatonma.commons.data.core.social.CreatedComment
import org.beatonma.commons.data.core.social.CreatedVote
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType
import javax.inject.Inject

private const val TAG = "SocialViewModel"

class SocialViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val socialRepository: SocialRepository,
    application: CommonsApplication,
): AndroidViewModel(application) {

    lateinit var livedata: LiveDataIoResult<SocialContent>
    lateinit var socialTarget: SocialTarget

    fun forTarget(sociableTarget: Sociable) = forTarget(
            SocialTarget(sociableTarget)
        )

    fun forTarget(target: SocialTarget) {
//        if (socialTarget != null) return
        livedata = socialRepository.observeSocialContent(target)
        socialTarget = target
    }

    fun refresh() {
        if (socialTarget != null) {
            livedata = socialRepository.observeSocialContent(socialTarget)
        }
    }

    @SignInRequired
    suspend fun postComment(text: String): IoResult<*> {
        return withUserAccount { account ->
            val token = userRepository.getUserTokenSync(account)
                ?: return NotSignedInError("", null)

            val comment = CreatedComment(
                userToken = token,
                target = socialTarget,
                text = text,
            )

            socialRepository.postComment(comment)
        }
    }

    @SignInRequired
    suspend fun postVote(voteType: SocialVoteType): IoResult<*> {
        return withUserAccount { account ->
            val token = userRepository.getUserTokenSync(account)
                ?: return NotSignedInError("", null)

            val vote = CreatedVote(
                userToken = token,
                target = socialTarget,
                voteType = voteType
            )

            socialRepository.postVote(vote)
        }
    }

    private suspend inline fun withUserAccount(block: (UserAccount) -> IoResult<*>):IoResult<*> {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(context)
        val userAccount = currentGoogleAccount?.toUserAccount()

        return if (userAccount == null) {
            NotSignedInError("Current Google Account is null")
        }
        else {
            block.invoke(userAccount)
        }
    }
}
