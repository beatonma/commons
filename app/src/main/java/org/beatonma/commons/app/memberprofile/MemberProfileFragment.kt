package org.beatonma.commons.app.memberprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.base.SocialFragment
import org.beatonma.commons.app.ui.compose.composeScreen
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.kotlin.extensions.getParliamentID
import org.beatonma.commons.kotlin.extensions.navigateTo

@AndroidEntryPoint
class MemberProfileFragment : SocialFragment() {
    private val viewmodel: ComposeMemberProfileViewModel by viewModels()

    private fun getMemberIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewmodel.memberID = getMemberIdFromBundle()

        return composeScreen(
            AmbientMemberProfileActions provides MemberProfileActions(
                onConstituencyClick = this::navigateTo
            ),
        ) {
            MemberProfileLayout(
                viewmodel,
                socialViewModel,
                userAccountViewModel
            )
        }
    }
}
