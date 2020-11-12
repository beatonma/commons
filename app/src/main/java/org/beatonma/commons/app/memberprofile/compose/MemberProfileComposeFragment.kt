package org.beatonma.commons.app.memberprofile.compose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.app.signin.AmbientUserToken
import org.beatonma.commons.app.signin.NullUserToken
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.compose.AmbientSocialActions
import org.beatonma.commons.app.social.compose.AmbientSocialContent
import org.beatonma.commons.app.social.compose.AmbientSocialUiState
import org.beatonma.commons.app.social.compose.SocialActions
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.Themed
import org.beatonma.commons.app.ui.compose.components.Error
import org.beatonma.commons.app.ui.compose.composeView
import org.beatonma.commons.app.ui.compose.withSystemUi
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.autotag
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.kotlin.extensions.getParliamentID
import org.beatonma.commons.repo.FlowIoResult
import org.beatonma.commons.repo.asSocialTarget
import org.beatonma.commons.repo.result.LoadingResult
import org.beatonma.commons.repo.result.isError
import org.beatonma.commons.repo.result.isSuccess
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import org.beatonma.commons.snommoc.models.social.SocialVoteType

@AndroidEntryPoint
class MemberProfileComposeFragment : Fragment(),
    Themed,
    BackPressConsumer {
    private val viewmodel: ComposeMemberProfileViewModel by viewModels()
    private val socialViewModel: SocialViewModel by viewModels()
    private val socialUiState = mutableStateOf(SocialUiState.Collapsed)

    private lateinit var socialFlow: MutableState<FlowIoResult<SocialContent>>

    override var theme: PartyColors? = null

    private fun getMemberIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val memberId = getMemberIdFromBundle()
        val flow = viewmodel.forMember(memberId)
        socialViewModel.forTarget(SocialTarget(SocialTargetType.member, parliamentdotuk = memberId))
        socialFlow = mutableStateOf(socialViewModel.flow)
        val activeUserFlow = viewmodel.getActiveToken()

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
            val socialResult by socialFlow.value.collectAsState(initial = LoadingResult())
            val activeUserState = activeUserFlow?.collectAsState(initial = LoadingResult())

            if (result.isError) {
                Error()
                return@composeView
            }

            val data = result.data ?: return@composeView

            withNotNull(socialResult.data) { socialViewModel.cachedSocialContent = it }

            withNotNull(data.profile) {
                socialViewModel.forTarget(it.asSocialTarget())
            }

            Providers(
                AmbientSocialActions provides socialCallbacks,
                AmbientSocialUiState provides socialUiState,
            ) {

                withSystemUi {
                    Providers(
                        AmbientSocialContent provides (socialResult.data
                            ?: socialViewModel.cachedSocialContent),
                        AmbientUserToken provides (activeUserState?.value?.data ?: NullUserToken),
                    ) {
                        MemberProfileLayout(data)
                    }
                }
            }
        }
    }

    private fun onVoteClicked(voteType: SocialVoteType) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = socialViewModel.updateVote(voteType)

            if (result.isSuccess) {
                socialViewModel.refresh()
                socialFlow.value = socialViewModel.flow
            }
            else if (result.isError) {
                Log.w(autotag, "updateVote failed: ${result.message} $result")
            }
        }
    }

    private fun onPostComment(text: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = socialViewModel.postComment(text)

            when {
                result.isSuccess -> {
                    socialViewModel.refresh()
                    socialFlow.value = socialViewModel.flow
                }

                result.isError -> {
                    Log.w(autotag, "postComment failed: ${result.message} $result")
                }
            }
        }
        socialUiState.update(SocialUiState.Expanded)
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
