package org.beatonma.commons.app.ui.screens.constituency.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.base.SocialFragment
import org.beatonma.commons.app.ui.maps.LocalMapConfig
import org.beatonma.commons.app.ui.maps.defaultMapConfig
import org.beatonma.commons.app.ui.util.composeScreen
import org.beatonma.commons.app.util.navigateToConstituencyResult
import org.beatonma.commons.app.util.navigateToMember
import org.beatonma.commons.app.util.parliamentID
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails

@AndroidEntryPoint
class ConstituencyDetailFragment: SocialFragment() {
    private val viewmodel: ConstituencyDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forConstituency(arguments.parliamentID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen(
        LocalConstituencyActions provides ConstituencyDetailActions(
            onMemberClick = ::navigateToMember,
            onConstituencyResultsClick = ::navigateToResult
        ),
        LocalMapConfig provides requireContext().defaultMapConfig(),
    ) {
        ConstituencyDetailLayout(
            viewmodel,
            socialViewModel,
            userAccountViewModel
        )
    }

    private fun navigateToMember(result: ConstituencyResultWithDetails) =
        navigateToMember(result.profile.parliamentdotuk)

    private fun navigateToResult(constituency: Constituency, result: ConstituencyResultWithDetails) =
        navigateToConstituencyResult(constituency.parliamentdotuk, result.election.parliamentdotuk)
}
