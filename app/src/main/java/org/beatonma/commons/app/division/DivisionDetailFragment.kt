package org.beatonma.commons.app.division

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.base.SocialFragment
import org.beatonma.commons.app.ui.compose.composeScreen
import org.beatonma.commons.kotlin.extensions.getDivision
import org.beatonma.commons.kotlin.extensions.navigateToMember

@AndroidEntryPoint
class DivisionDetailFragment : SocialFragment() {
    companion object {
        const val HOUSE = "house"
    }

    private val viewmodel: DivisionDetailViewModel by viewModels()

    private fun getDivisionFromBundle() = arguments.getDivision()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forDivision(getDivisionFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        composeScreen(
            AmbientDivisionActions provides DivisionActions(
                onMemberClick = { vote ->
                    navigateToMember(vote.memberId)
                }
            ),
        ) {
            DivisionDetailLayout(viewmodel, socialViewModel, userAccountViewModel)
        }
}
