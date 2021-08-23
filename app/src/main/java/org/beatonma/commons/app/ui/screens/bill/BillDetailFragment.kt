package org.beatonma.commons.app.ui.screens.bill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.base.SocialFragment
import org.beatonma.commons.app.ui.util.composeScreen
import org.beatonma.commons.app.util.navigateToMember
import org.beatonma.commons.app.util.parliamentID
import org.beatonma.commons.core.ParliamentID

@AndroidEntryPoint
class BillDetailFragment : SocialFragment() {
    private val viewmodel: BillDetailViewModel by viewModels()

    private fun getBillFromBundle(): ParliamentID = arguments.parliamentID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forBill(getBillFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen(
        LocalBillActions provides BillActions(
            onSponsorClick = { sponsor ->
                val sponsorId = sponsor.sponsor.parliamentdotuk
                if (sponsorId == null) {
                    TODO("This should show a message and/or initiate a search..?")
                } else {
                    navigateToMember(sponsorId)
                }
            }
        ),
    ) {
        BillDetailLayout(viewmodel, socialViewModel, userAccountViewModel)
    }
}
