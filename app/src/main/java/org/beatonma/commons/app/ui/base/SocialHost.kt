package org.beatonma.commons.app.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.toPreviousState
import org.beatonma.commons.app.ui.navigation.BackPressConsumer

interface SocialHost: BackPressConsumer {
    val userAccountViewModel: UserAccountViewModel
    val socialViewModel: SocialViewModel

    override fun onBackPressed(): Boolean = socialViewModel.uiState.toPreviousState()
}

abstract class SocialFragment: Fragment(), SocialHost {
    override val userAccountViewModel: UserAccountViewModel by activityViewModels()
    override val socialViewModel: SocialViewModel by viewModels()
}
