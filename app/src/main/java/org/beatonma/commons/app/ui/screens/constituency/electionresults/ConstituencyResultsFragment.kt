package org.beatonma.commons.app.ui.screens.constituency.electionresults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.util.composeScreen
import org.beatonma.commons.app.util.getConstituencyResult
import org.beatonma.commons.app.util.logWarning
import org.beatonma.commons.app.util.navigateTo

@AndroidEntryPoint
class ConstituencyResultsFragment : Fragment() {
    private val viewmodel: ConstituencyResultsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forConstituencyInElection(arguments.getConstituencyResult())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen {
        ConstituencyResultsLayout(
            viewmodel,
            onClickCandidate = { name, profile ->
                when (profile) {
                    null -> logWarning("No profile available - this should trigger a search by candidate name '$name'")
                    else -> navigateTo(profile)
                }
            },
        )
    }
}
