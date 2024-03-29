package org.beatonma.commons.app.ui.screens.division.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.base.SocialFragment
import org.beatonma.commons.app.ui.screens.division.DivisionActions
import org.beatonma.commons.app.ui.util.composeScreen
import org.beatonma.commons.app.util.getDivision
import org.beatonma.commons.app.util.navigateToMember

@AndroidEntryPoint
class CommonsDivisionFragment : SocialFragment() {
    private val viewmodel: CommonsDivisionViewModel by viewModels()

    private fun getDivisionFromBundle() = arguments.getDivision()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forDivision(getDivisionFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen(
        LocalDivisionActions provides DivisionActions(
            onMemberClick = { memberId ->
                navigateToMember(memberId)
            }
        ),
    ) {
        CommonsDivisionLayout(viewmodel, socialViewModel, userAccountViewModel)
    }
}
