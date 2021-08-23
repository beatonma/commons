package org.beatonma.commons.app.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.screens.social.toPreviousState

@Deprecated("Remove fragments")
interface SocialHost: BackPressConsumer {
    val userAccountViewModel: UserAccountViewModel
    val socialViewModel: SocialViewModel

    override fun onBackPressed(): Boolean = socialViewModel.uiState.toPreviousState()
}

@Deprecated("Remove fragments")
abstract class SocialFragment: Fragment(), SocialHost {
    override val userAccountViewModel: UserAccountViewModel by activityViewModels()
    override val socialViewModel: SocialViewModel by viewModels()
}
