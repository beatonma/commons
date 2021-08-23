package org.beatonma.commons.app.ui.screens.bill

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.colors.SurfaceTheme
import org.beatonma.commons.app.ui.colors.houseTheme
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.app.ui.screens.bill.viewmodel.AnnotatedBillStage
import org.beatonma.commons.app.ui.screens.bill.viewmodel.BillStageCategory
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.HeaderExpansion
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.screens.social.StickySocialScaffold
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.components.CollapsedColumn
import org.beatonma.commons.compose.components.Tag
import org.beatonma.commons.compose.components.stickyrow.StickyHeaderRow
import org.beatonma.commons.compose.components.text.Caption
import org.beatonma.commons.compose.components.text.ComponentTitle
import org.beatonma.commons.compose.components.text.OptionalText
import org.beatonma.commons.compose.components.text.PluralText
import org.beatonma.commons.compose.components.text.Quote
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.components.text.ScreenTitle
import org.beatonma.commons.compose.layout.optionalItem
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.core.House
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.resolution.uiDescription
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.compose.formatting.formatted
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.padding.paddingValues
import org.beatonma.commons.theme.compose.theme.house
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

internal val LocalBillActions: ProvidableCompositionLocal<BillActions> =
    compositionLocalOf { BillActions() }

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
    onSponsorClick: SponsorAction = LocalBillActions.current.onSponsorClick,
) {
    StickySocialScaffold(
        aboveSocial = { headerExpansion, modifier ->
            HeaderAboveSocial(bill, headerExpansion, modifier)
        },
        lazyListContent = {
            lazyContent(bill, onSponsorClick)
        }
    )
}

@Composable
private fun HeaderAboveSocial(
    completeBill: CompleteBill,
    expansionProgress: HeaderExpansion,
    modifier: Modifier,
) {
    val bill = completeBill.bill

    Column(
        modifier
            .padding(Padding.Screen)
            .statusBarsPadding()
    ) {
        ScreenTitle(bill.title, autoPadding = false)
        Caption(completeBill.type.name dot completeBill.session.name)
        Quote(bill.description)
        ResourceText(R.string.bill_publications_count, completeBill.publications.size)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.lazyContent(
    completeBill: CompleteBill,
    onSponsorClick: SponsorAction,
) {
    val sectionModifier = ScreenPaddingModifier.padding(Padding.VerticalListItemLarge)
    val horizontalItemModifier = Modifier.padding(Padding.HorizontalListItem)

    optionalItem(completeBill.publications) { publications ->
        Publications(publications, sectionModifier)
    }

    optionalItem(completeBill.sponsors) { sponsors ->
        Sponsors(sponsors, onSponsorClick, sectionModifier, horizontalItemModifier)
    }

    optionalItem(completeBill.stages) { stages ->
        Stages(completeBill.getAnnotatedBillStages())
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Publications(
    publications: List<BillPublication>,
    modifier: Modifier,
) {
    CollapsedColumn(
        publications,
        headerBlock = CollapsedColumn.simpleHeader(
            stringResource(R.string.bill_publications),
            "${publications.size}"
        ),
        modifier = modifier,
        scrollable = false,
    ) { publication ->
        ListItem(
            text = { Text(publication.title) },
            trailing = { Tag("${publication.contentType}") }
        )
    }
}

@Composable
private fun Sponsors(
    sponsors: List<BillSponsorWithParty>,
    onSponsorClick: SponsorAction,
    modifier: Modifier,
    itemModifier: Modifier,
) {
    Text("Sponsors")
    Column(modifier) {
        ResourceText(R.string.bill_sponsors_count, sponsors.size)
        LazyRow {
            items(sponsors) { sponsor ->
                Column(
                    itemModifier
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Stages(
    stages: List<AnnotatedBillStage>,
    modifier: Modifier = Modifier,
) {
    Text("Stages")

    StickyHeaderRow(
        stages,
        modifier = modifier,
        headerForItem = { it?.category },
        groupBackground = { category ->
            if (category != null) {
                val theme = category.theme()
                Spacer(
                    Modifier
                        .clip(shapes.small)
                        .background(theme.surface)
                )
            }
        },
        headerContent = { category ->
            if (category != null) {
                val theme = category.theme()
                ComponentTitle(
                    category.uiDescription(),
                    Modifier
                        .padding(Padding.ScreenHorizontal)
                        .padding(Padding.VerticalListItemLarge)
                        .padding(end = 32.dp),
                    color = theme.onSurface,
                    maxLines = 1,
                )
            }
        },
        itemContent = { item ->
            val theme = item.category.theme()
            val stage = item.stage.stage
            val sittings = item.stage.sittings

            CompositionLocalProvider(
                LocalContentColor provides theme.onSurface
            ) {
                Column(
                    Modifier
                        .padding(paddingValues(horizontal = 8.dp, top = 6.dp, bottom = 16.dp))
                        .clip(shapes.small)
                        .background(theme.onSurface.copy(alpha = .1F))
                        .padding(8.dp)
                ) {
                    Text(stage.type)

                    if (sittings.size > 1) {
                        PluralText(R.plurals.bill_sittings, sittings.size)
                    }

                    Caption(
                        sittings
                            .sortedBy { it.date }
                            .map { it.date.formatted() }
                            .dotted()
                    )
                }
            }
        }
    )
}

@Composable
private fun BillStageCategory.theme(): SurfaceTheme = when(this) {
    BillStageCategory.Commons -> House.commons.theme()
    BillStageCategory.Lords -> House.lords.theme()
    BillStageCategory.ConsiderationOfAmendments -> houseTheme(colors.house.Parliament)
    BillStageCategory.RoyalAssent -> houseTheme(colors.house.Royal)
}

private val ScreenPaddingModifier = Modifier.padding(Padding.ScreenHorizontal)
