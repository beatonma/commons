package org.beatonma.commons.app.bill.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.base.SocialFragment
import org.beatonma.commons.app.ui.compose.composeScreen
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.kotlin.extensions.getParliamentID
import org.beatonma.commons.kotlin.extensions.navigateToMember

@AndroidEntryPoint
class BillDetailFragment: SocialFragment() {
    private val viewmodel: BillDetailViewModel by viewModels()

    private fun getBillFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forBill(getBillFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeScreen {
        val billActions = remember {
            BillActions(
                onSponsorClick = { sponsor ->
                    val sponsorId = sponsor.sponsor.parliamentdotuk
                    if (sponsorId == null) {
                        TODO("This should show a message and/or initiate a search..?")
                    }
                    else {
                        navigateToMember(sponsorId)
                    }
                }
            )
        }

        Providers(
            AmbientBillActions provides billActions,
        ) {
            BillDetailLayout(viewmodel, socialViewModel, userAccountViewModel)
        }
    }
}
