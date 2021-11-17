package org.beatonma.commons.app.ui.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.SocialViewModel

interface SocialHost {
    val userAccountViewModel: UserAccountViewModel
    val socialViewModel: SocialViewModel
}

abstract class SocialFragment : Fragment(), SocialHost {
    override val userAccountViewModel: UserAccountViewModel by activityViewModels()
    override val socialViewModel: SocialViewModel by viewModels()
}
