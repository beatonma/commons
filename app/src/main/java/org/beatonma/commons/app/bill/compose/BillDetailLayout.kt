package org.beatonma.commons.app.bill.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import org.beatonma.commons.R
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.social.ProvideSocial
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.StickySocialScaffold
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.components.CollapsedColumn
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.components.ResourceText
import org.beatonma.commons.compose.layout.optionalItem
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.components.Quote
import org.beatonma.commons.theme.compose.components.ScreenTitle
import org.beatonma.commons.theme.compose.theme.positive
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

internal val AmbientBillActions: ProvidableAmbient<BillActions> = ambientOf { BillActions() }

@Composable
fun BillDetailLayout(
    viewmodel: BillDetailViewModel,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
) {
    val resultState by viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(resultState) { billData ->
        ProvideSocial(
            viewmodel,
            socialViewModel,
            userAccountViewModel
        ) {
            BillDetailLayout(billData)
        }
    }
}

@Composable
fun BillDetailLayout(
    bill: CompleteBill,
    onSponsorClick: SponsorAction = AmbientBillActions.current.onSponsorClick,
) {
    StickySocialScaffold(
        headerContentAboveSocial = { headerExpansion, modifier ->
            HeaderAboveSocial(bill, headerExpansion, modifier)
        },
        headerContentBelowSocial = { headerExpansion, modifier ->

        },
        lazyListContent = {
            lazyContent(bill, onSponsorClick)
        }
    )
}

@Composable
private fun HeaderAboveSocial(
    completeBill: CompleteBill,
    expansionProgress: Float,
    modifier: Modifier
) {
    val bill = completeBill.bill

    Column(
        modifier
            .padding(Padding.Screen)
            .statusBarsPadding()
    ) {
        ScreenTitle(bill.title)
        Text(dotted(completeBill.type.name, completeBill.session.name))
        Quote(bill.description)
        ResourceText(R.string.bill_publications_count, completeBill.publications.size)
//        Todo("publications")
//        Todo("stages")
//        Todo("sponsors")
    }
}

private fun LazyListScope.lazyContent(
    completeBill: CompleteBill,
    onSponsorClick: SponsorAction,
) {
    optionalItem(completeBill.publications) { publications ->
        Publications(publications)
    }

    optionalItem(completeBill.sponsors) { sponsors ->
        Sponsors(sponsors, onSponsorClick)
    }
}

@Composable
private fun Publications(publications: List<BillPublication>) {
    CollapsedColumn(
        publications,
        headerBlock = CollapsedColumn.simpleHeader("Publications", "${ publications.size }"),
        Modifier.padding(Padding.VerticalListItemLarge),
    ) { publication, itemModifier ->
        Column {
            Text(
                publication.title,
                itemModifier
                    .padding(Padding.HorizontalListItem),
            )
            Text("Type: ${publication.contentType}", color = colors.positive)
        }
    }
}

@Composable
private fun Sponsors(
    sponsors: List<BillSponsorWithParty>,
    onSponsorClick: SponsorAction,
) {
    Column {
        ResourceText(R.string.bill_sponsors_count, sponsors.size)
        LazyRow {
            items(sponsors) { sponsor ->
                Column(
                    Modifier
                        .clickable { onSponsorClick(sponsor) }
                        .padding(Padding.HorizontalListItem),
                ) {
                    Text(sponsor.sponsor.name)
                    OptionalText(sponsor.party?.name)
                }
            }
        }
    }
}
