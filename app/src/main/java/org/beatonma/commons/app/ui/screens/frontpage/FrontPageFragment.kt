package org.beatonma.commons.app.ui.screens.frontpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.app.ui.screens.search.LocalSearchActions
import org.beatonma.commons.app.ui.screens.search.SearchActions
import org.beatonma.commons.app.ui.screens.search.SearchViewModel
import org.beatonma.commons.app.ui.screens.signin.LocalUserToken
import org.beatonma.commons.app.ui.screens.signin.NullUserToken
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.util.composeScreen
import org.beatonma.commons.app.util.navigateTo
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.collapse
import org.beatonma.commons.compose.animation.isExpanded
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.repo.result.IoLoading

@AndroidEntryPoint
class FrontPageFragment : Fragment(), BackPressConsumer {
    private val viewmodel: ZeitgeistViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val accountViewModel: UserAccountViewModel by activityViewModels()

    lateinit var searchUiState: MutableState<ExpandCollapseState>

    private val zeitgeistActions = ZeitgeistActions(
        onMemberClick = { profile ->
            navigateTo(profile)
        },
        onDivisionClick = { division ->
            navigateTo(division)
        },
        onBillClick = { bill ->
            navigateTo(bill)
        },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen {
        val userTokenState = accountViewModel.userTokenLiveData.observeAsState(NullUserToken)

        searchUiState = rememberExpandCollapseState()
        val searchActions = remember {
            SearchActions(
                onSubmit = { query -> searchViewModel.submit(query) },
                onClickMember = { memberSearchResult ->
                    navigateTo(memberSearchResult)
                }
            )
        }
        val zeitgeistResult by viewmodel.zeitgeist.collectAsState(IoLoading)
        val searchResults by searchViewModel.resultLiveData.observeAsState(listOf())

        CompositionLocalProvider(
            LocalSearchActions provides searchActions,
            LocalUserToken provides userTokenState.value,
            LocalContentColor provides colors.onSurface,
            LocalZeitgeistActions provides zeitgeistActions,
        ) {
            FrontPageUi(zeitgeistResult, searchResults, searchUiState)
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
