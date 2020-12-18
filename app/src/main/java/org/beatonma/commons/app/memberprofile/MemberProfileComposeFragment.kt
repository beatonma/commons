package org.beatonma.commons.app.memberprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.toPreviousState
import org.beatonma.commons.app.ui.compose.composeScreen
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.kotlin.extensions.getParliamentID

@AndroidEntryPoint
class MemberProfileComposeFragment : Fragment(),
    BackPressConsumer {
    private val viewmodel: ComposeMemberProfileViewModel by viewModels()

    private val userAccountViewModel: UserAccountViewModel by activityViewModels()
    private val socialViewModel: SocialViewModel by viewModels()

    private fun getMemberIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewmodel.memberID = getMemberIdFromBundle()

        return composeScreen {
            MemberProfileLayout(
                viewmodel,
                socialViewModel,
                userAccountViewModel
            )
        }
    }

    override fun onBackPressed(): Boolean = socialViewModel.uiState.toPreviousState()
}
