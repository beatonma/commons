package org.beatonma.commons.app.ui.screens.division.lords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.base.SocialFragment
import org.beatonma.commons.app.ui.screens.division.DivisionActions
import org.beatonma.commons.app.ui.screens.division.commons.LocalDivisionActions
import org.beatonma.commons.app.ui.util.composeScreen
import org.beatonma.commons.app.util.BundledDivision
import org.beatonma.commons.app.util.getDivision
import org.beatonma.commons.app.util.navigateToMember


@AndroidEntryPoint
class LordsDivisionFragment : SocialFragment() {
    private val viewmodel: LordsDivisionViewModel by viewModels()

    private fun getDivisionFromBundle(): BundledDivision = arguments.getDivision()

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
        LordsDivisionLayout(viewmodel, socialViewModel, userAccountViewModel)
    }
}
