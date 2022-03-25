package org.beatonma.commons.app.ui.screens.frontpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.screens.signin.LocalUserToken
import org.beatonma.commons.app.ui.screens.signin.NullUserToken
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.util.composeScreen

@AndroidEntryPoint
class FrontPageFragment : Fragment() {
    private val accountViewModel: UserAccountViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen {
        val userToken by accountViewModel.userTokenLiveData.observeAsState(NullUserToken)

        CompositionLocalProvider(
            LocalUserToken provides userToken,
            LocalContentColor provides colors.onSurface,
        ) {
            FrontPageUi()
        }
    }
}
