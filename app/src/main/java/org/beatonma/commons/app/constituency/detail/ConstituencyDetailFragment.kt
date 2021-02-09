package org.beatonma.commons.app.constituency.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.base.SocialFragment
import org.beatonma.commons.app.ui.compose.composeScreen
import org.beatonma.commons.app.ui.maps.AmbientMapConfig
import org.beatonma.commons.app.ui.maps.defaultMapConfig
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.kotlin.extensions.navigateToConstituencyResult
import org.beatonma.commons.kotlin.extensions.navigateToMember
import org.beatonma.commons.kotlin.extensions.parliamentID

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
        AmbientConstituencyActions provides ConstituencyDetailActions(
            onMemberClick = ::navigateToMember,
            onConstituencyResultsClick = ::navigateToResult
        ),
        AmbientMapConfig provides requireContext().defaultMapConfig(),
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

