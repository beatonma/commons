package org.beatonma.commons.app.division

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.toPreviousState
import org.beatonma.commons.app.ui.compose.composeScreen
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.kotlin.extensions.getDivision
import org.beatonma.commons.kotlin.extensions.navigateToMember

@AndroidEntryPoint
class DivisionDetailFragment : Fragment(), BackPressConsumer {
    companion object {
        const val HOUSE = "house"
    }

    private val viewmodel: DivisionDetailViewModel by viewModels()

    private val userAccountViewModel: UserAccountViewModel by activityViewModels()
    private val socialViewModel: SocialViewModel by viewModels()

    private fun getDivisionFromBundle() = arguments.getDivision()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forDivision(getDivisionFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen{

        val divisionActions = remember {
            DivisionActions(
                onMemberClick = { memberID ->
                    navigateToMember(memberID)
                }
            )
        }

        Providers(
            AmbientDivisionActions provides divisionActions,
        ) {
            DivisionDetailLayout(viewmodel, socialViewModel, userAccountViewModel)
        }
    }

    override fun onBackPressed(): Boolean = socialViewModel.uiState.toPreviousState()
}
