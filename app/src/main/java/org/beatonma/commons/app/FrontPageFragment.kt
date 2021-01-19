package org.beatonma.commons.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.AmbientContentColor
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.app.featured.AmbientZeitgeistActions
import org.beatonma.commons.app.featured.FeaturedContentViewModel
import org.beatonma.commons.app.featured.ZeitgeistActions
import org.beatonma.commons.app.search.AmbientSearchActions
import org.beatonma.commons.app.search.SearchActions
import org.beatonma.commons.app.search.SearchViewModel
import org.beatonma.commons.app.signin.AmbientUserToken
import org.beatonma.commons.app.signin.NullUserToken
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.compose.composeScreen
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.collapse
import org.beatonma.commons.compose.animation.isExpanded
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.kotlin.extensions.navigateTo
import org.beatonma.commons.repo.result.IoLoading

@AndroidEntryPoint
class FrontPageFragment : Fragment(), BackPressConsumer {
    private val viewmodel: FeaturedContentViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val accountViewModel: UserAccountViewModel by activityViewModels()

    lateinit var searchUiState: MutableState<ExpandCollapseState>

    private val zeitgeistActions = ZeitgeistActions(
        onMemberClick = { profile ->
            navigateTo(R.id.action_frontPageFragment_to_memberProfileFragment, profile)
        },
        onDivisionClick = { division ->
            navigateTo(R.id.action_frontPageFragment_to_divisionProfileFragment, division)
        },
        onBillClick = { bill ->
            navigateTo(R.id.action_frontPageFragment_to_billFragment, bill)
        },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen {
        val userTokenState = accountViewModel.userTokenLiveData.observeAsState(initial = NullUserToken)

        searchUiState = rememberExpandCollapseState()
        val searchActions = remember {
            SearchActions(
                onSubmit = { query -> searchViewModel.submit(query) },
                onClickMember = { memberSearchResult ->
                    navigateTo(memberSearchResult.toUri())
                }
            )
        }
        val zeitgeistResult by viewmodel.zeitgeist.collectAsState(IoLoading)
        val searchResults by searchViewModel.resultLiveData.observeAsState(listOf())

        Providers(
            AmbientSearchActions provides searchActions,
            AmbientUserToken provides userTokenState.value,
            AmbientContentColor provides colors.onSurface,
            AmbientZeitgeistActions provides zeitgeistActions,
        ) {
            FrontPageUi(zeitgeistResult, searchResults)
        }
    }

    override fun onBackPressed() = when {
        searchUiState.isExpanded -> {
            searchUiState.collapse()
            true
        }

        else -> false
    }
}
