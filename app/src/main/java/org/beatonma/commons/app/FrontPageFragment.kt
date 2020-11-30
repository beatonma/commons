package org.beatonma.commons.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.AmbientContentColor
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.app.featured.AmbientZeitgeistActions
import org.beatonma.commons.app.featured.FeaturedContentViewModel
import org.beatonma.commons.app.featured.ZeitgeistActions
import org.beatonma.commons.app.search.SearchViewModel
import org.beatonma.commons.app.search.compose.AmbientSearchActions
import org.beatonma.commons.app.search.compose.SearchActions
import org.beatonma.commons.app.signin.compose.AmbientSignInActions
import org.beatonma.commons.app.signin.compose.AmbientUserToken
import org.beatonma.commons.app.signin.compose.NullUserToken
import org.beatonma.commons.app.signin.compose.SignInHandler
import org.beatonma.commons.app.ui.compose.composeView
import org.beatonma.commons.app.ui.compose.withSystemUi
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.kotlin.extensions.navigateTo
import org.beatonma.commons.repo.result.LoadingResult

@AndroidEntryPoint
class FrontPageFragment : Fragment(), BackPressConsumer {

    private val viewmodel: FeaturedContentViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()

    private val signInHandler: SignInHandler = object : SignInHandler {
        override var signInLauncher: ActivityResultLauncher<Intent> =
            registerSignInLauncher(this@FrontPageFragment)
        override var signInActions = defaultSignInActions()
    }
    lateinit var searchUiState: MutableState<ExpandCollapseState>

    private val zeitgeistActions = ZeitgeistActions(
        { profile ->
            navigateTo(R.id.action_frontPageFragment_to_memberProfileFragment, profile)
        },
        { bill ->
            navigateTo(R.id.action_frontPageFragment_to_billFragment, bill)
        },
        { division ->
            navigateTo(R.id.action_frontPageFragment_to_divisionProfileFragment,
                division)
        },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        val userTokenState = viewmodel.userTokenLiveData.observeAsState(initial = NullUserToken)

        searchUiState = rememberExpandCollapseState()
        val searchActions = remember {
            SearchActions(
                onSubmit = { query -> searchViewModel.submit(query) },
                onClickMember = { memberSearchResult ->
                    navigateTo(
                        R.id.action_frontPageFragment_to_memberProfileFragment,
                        memberSearchResult.toUri(),
                    )
                }
            )
        }
        val zeitgeistResult by viewmodel.zeitgeist.collectAsState(LoadingResult())
        val searchResults by searchViewModel.resultLiveData.observeAsState(listOf())

        Providers(
            AmbientSearchActions provides searchActions,
            AmbientSignInActions provides signInHandler.signInActions,
            AmbientUserToken provides userTokenState.value,
            AmbientContentColor provides colors.onSurface,
            AmbientZeitgeistActions provides zeitgeistActions,
        ) {
            withSystemUi {
                FrontPageUi(zeitgeistResult, searchResults)
            }
        }
    }

    override fun onBackPressed() = when {
        searchUiState.value == ExpandCollapseState.Expanded -> {
            searchUiState.update(ExpandCollapseState.Collapsed)
            true
        }

        else -> false
    }
}
