package org.beatonma.commons.app.social

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.beatonma.commons.data.core.interfaces.Sociable
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.kotlin.extensions.withMainContext
import org.beatonma.commons.repo.asSocialTarget
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.repository.SocialRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.isSuccess
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.snommoc.annotations.SignInRequired
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialVoteType

private const val TAG = "SocialViewModel"

private const val SavedStateSocialContent = "social_content"

class SocialViewModel @ViewModelInject constructor(
    private val socialRepository: SocialRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val livedata: LiveData<SocialContent> =
        savedStateHandle.getLiveData(SavedStateSocialContent, EmptySocialContent)

    lateinit var socialTarget: SocialTarget

    fun forTarget(target: Sociable, userToken: UserToken?) {
        socialTarget = target.asSocialTarget()
        refresh(userToken)
    }

    fun refresh(userToken: UserToken?) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSocialContent(userToken)
        }
    }

    @SignInRequired
    suspend fun postComment(text: String, userToken: UserToken): IoResult<*> {
        val comment = CreatedComment(
            userToken = userToken,
            target = socialTarget,
            text = text,
        )

        return socialRepository.postComment(comment)
    }

    @SignInRequired
    suspend fun postVote(voteType: SocialVoteType, userToken: UserToken): IoResult<*> {
        val vote = CreatedVote(
            userToken = userToken,
            target = socialTarget,
            voteType = voteType
        )

        return socialRepository.postVote(vote)
    }

    @SignInRequired
    suspend fun updateVote(voteType: SocialVoteType, userToken: UserToken): IoResult<*> = postVote(
        voteType = if (shouldRemoveVote(voteType)) SocialVoteType.NULL else voteType,
        userToken = userToken
    )

    /**
     * Called when a user submits a vote.
     * Returns true if the voteType is the same as that already registered.
     */
    private fun shouldRemoveVote(voteType: SocialVoteType): Boolean =
        voteType == livedata.value?.userVote

    private suspend fun fetchSocialContent(userToken: UserToken?) {
        socialRepository.getSocialContent(
            socialTarget,
            userToken?.snommocToken
        ).first { it.isSuccess }.onSuccess { data ->
            withMainContext {
                savedStateHandle[SavedStateSocialContent] = data
            }
        }
    }
}
