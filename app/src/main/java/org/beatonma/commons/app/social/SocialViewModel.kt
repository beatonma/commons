package org.beatonma.commons.app.social

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.app
import org.beatonma.commons.app.signin.BaseUserAccountViewModel
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.interfaces.Sociable
import org.beatonma.commons.repo.FlowIoResult
import org.beatonma.commons.repo.asSocialTarget
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.repository.SocialRepository
import org.beatonma.commons.repo.repository.UserRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.NotSignedInError
import org.beatonma.commons.repo.result.isSuccess
import org.beatonma.commons.snommoc.annotations.SignInRequired
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialVoteType

private const val TAG = "SocialViewModel"

private const val SavedStateSocialContent = "socialcontent"

class SocialViewModel @ViewModelInject constructor(
    userRepository: UserRepository,
    private val socialRepository: SocialRepository,
    @ApplicationContext application: Context,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseUserAccountViewModel(userRepository, application.app) {

    lateinit var flow: FlowIoResult<SocialContent>
    val livedata: LiveData<SocialContent> =
        savedStateHandle.getLiveData(SavedStateSocialContent, EmptySocialContent)

    lateinit var socialTarget: SocialTarget

    fun forTarget(target: SocialTarget) {
        socialTarget = target
        getTokenForCurrentSignedInAccountFlow()
        refresh()
    }

    fun forTarget(target: Sociable) = forTarget(target.asSocialTarget())

    private suspend fun fetchSocialContent(): IoResult<SocialContent> {
        return socialRepository.getSocialContent(
            socialTarget,
            userTokenLiveData.value?.snommocToken
        ).first { it.isSuccess }.let { result ->
            withNotNull(result.data) { data ->
                withContext(Dispatchers.Main) {
                    savedStateHandle[SavedStateSocialContent] = data
                }
            }
            result
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSocialContent()
        }
    }

    @SignInRequired
    suspend fun postComment(text: String): IoResult<*> {
        val token = userTokenLiveData.value
            ?: return NotSignedInError("User must be signed in to post a comment")

        val comment = CreatedComment(
            userToken = token,
            target = socialTarget,
            text = text,
        )

        return socialRepository.postComment(comment)
    }

    @SignInRequired
    suspend fun postVote(voteType: SocialVoteType): IoResult<*> {
        val token =
            userTokenLiveData.value ?: return NotSignedInError("User must be signed in to vote")

        val vote = CreatedVote(
            userToken = token,
            target = socialTarget,
            voteType = voteType
        )

        return socialRepository.postVote(vote)
    }

    @SignInRequired
    suspend fun updateVote(voteType: SocialVoteType): IoResult<*> = postVote(
        if (shouldRemoveVote(voteType)) {
            SocialVoteType.NULL
        }
        else {
            voteType
        }
    )

    @Deprecated("Validation handled in composable")
    internal fun validateComment(text: String): CommentValidation {
        val length = text.length

        return when {
            length < 10 -> CommentValidation.INVALID_TOO_SHORT
            length > BuildConfig.SOCIAL_COMMENT_MAX_LENGTH -> CommentValidation.INVALID_TOO_LONG

            // Further validation on server side
            else -> CommentValidation.VALID
        }
    }

    /**
     * Called when a user submits a vote.
     * Returns true if the voteType is the same as that already registered.
     */
    private fun shouldRemoveVote(voteType: SocialVoteType): Boolean =
        voteType == livedata.value?.userVote
}

@Deprecated("Not needed in Compose ui implementation")
internal enum class CommentValidation {
    VALID,
    INVALID_TOO_SHORT,
    INVALID_TOO_LONG,
}
