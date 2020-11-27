package org.beatonma.commons.app.memberprofile.compose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.app.signin.compose.AmbientSignInActions
import org.beatonma.commons.app.signin.compose.AmbientUserToken
import org.beatonma.commons.app.signin.compose.NullUserToken
import org.beatonma.commons.app.signin.compose.SignInActions
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.compose.AmbientSocialActions
import org.beatonma.commons.app.social.compose.AmbientSocialContent
import org.beatonma.commons.app.social.compose.AmbientSocialUiState
import org.beatonma.commons.app.social.compose.SocialActions
import org.beatonma.commons.app.ui.compose.components.Error
import org.beatonma.commons.app.ui.compose.components.Loading
import org.beatonma.commons.app.ui.compose.composeView
import org.beatonma.commons.app.ui.compose.withSystemUi
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.autotag
import org.beatonma.commons.kotlin.extensions.getParliamentID
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.LoadingResult
import org.beatonma.commons.repo.result.isError
import org.beatonma.commons.repo.result.isLoading
import org.beatonma.commons.repo.result.isSuccess
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType

@AndroidEntryPoint
class MemberProfileComposeFragment : Fragment(),
    BackPressConsumer {
    private val viewmodel: ComposeMemberProfileViewModel by viewModels()

    private val socialViewModel: SocialViewModel by viewModels()
    private val socialUiState = mutableStateOf(SocialUiState.Collapsed)

    private fun getMemberIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Member data
        val memberId = getMemberIdFromBundle()
        val flow = viewmodel.forMember(memberId)

        val socialCallbacks = SocialActions(
            onVoteUpClick = { onVoteClicked(SocialVoteType.aye) },
            onVoteDownClick = { onVoteClicked(SocialVoteType.no) },
            onExpandedCommentIconClick = { },
            onCommentClick = { comment ->
                println("Clicked comment $comment")
            },
            onCreateCommentClick = { socialUiState.update(SocialUiState.ComposeComment) },
            onCommentSubmitClick = ::onPostComment
        )

        return composeView {
            val result by flow.collectAsState(initial = LoadingResult())
            val activeUserState = socialViewModel.userTokenLiveData.observeAsState(NullUserToken)
            val social = socialViewModel.livedata.observeAsState(EmptySocialContent)

            withSystemUi {
                when {
                    result.isError -> {
                        Error(result.message)
                    }

                    result.isLoading -> {
                        Loading()
                    }

                    result.isSuccess -> {
                        val data = result.data ?: error("No data! $result")
                        socialViewModel.forTarget(data.profile)

                        Providers(
                            AmbientSocialActions provides socialCallbacks,
                            AmbientSocialUiState provides socialUiState,
                            AmbientSocialContent provides social.value,
                            AmbientUserToken provides activeUserState.value,
                            AmbientSignInActions provides SignInActions()
                        ) {
                            MemberProfileLayout(data)
                        }
                    }
                }
            }
        }
    }

    private fun onVoteClicked(voteType: SocialVoteType) {
        onSubmitSocialContent { socialViewModel.updateVote(voteType) }
    }

    private fun onPostComment(text: String) {
        onSubmitSocialContent { socialViewModel.postComment(text) }
        socialUiState.update(SocialUiState.Expanded)
    }

    private fun <T> onSubmitSocialContent(block: suspend () -> IoResult<T>) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = block()
            when {
                result.isSuccess -> {
                    refreshSocialContent()
                }

                result.isError -> {
                    Log.w(autotag, "social content submission failed: ${result.message} $result")
                }
            }
        }
    }

    private fun refreshSocialContent() {
        socialViewModel.refresh()
    }

    override fun onBackPressed(): Boolean = when (socialUiState.value) {
        SocialUiState.Expanded -> {
            socialUiState.update(SocialUiState.Collapsed)
            true
        }

        SocialUiState.ComposeComment -> {
            socialUiState.update(SocialUiState.Expanded)
            true
        }

        else -> false
    }
}
