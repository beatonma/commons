package org.beatonma.commons.app.ui.screens.bill

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.colors.SurfaceTheme
import org.beatonma.commons.app.ui.colors.houseTheme
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.app.ui.screens.bill.viewmodel.AnnotatedBillStage
import org.beatonma.commons.app.ui.screens.bill.viewmodel.BillStageCategory
import org.beatonma.commons.app.ui.screens.frontpage.MemberLayout
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialScaffold
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.uiDescription
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.components.CollapsedColumn
import org.beatonma.commons.compose.components.StickyHeaderRow
import org.beatonma.commons.compose.components.Tag
import org.beatonma.commons.compose.components.text.Caption
import org.beatonma.commons.compose.components.text.ComponentTitle
import org.beatonma.commons.compose.components.text.PluralResourceText
import org.beatonma.commons.compose.components.text.Quote
import org.beatonma.commons.compose.components.text.ScreenTitle
import org.beatonma.commons.compose.layout.optionalItem
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.pluralResource
import org.beatonma.commons.core.House
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithProfile
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.CommonsPadding
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.theme.house
import org.beatonma.commons.themed.paddingValues

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
    SocialScaffold(
        title = {
            ScreenTitle(bill.bill.title)
        },
        aboveSocial = { },
        belowSocial = null,
    ) { modifier ->
        lazyContent(bill, onSponsorClick, modifier)
    }
}


@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.lazyContent(
    completeBill: CompleteBill,
    onSponsorClick: SponsorAction,
    modifier: Modifier = Modifier,
) {
    val sectionModifier = modifier
        .padding(CommonsPadding.ScreenHorizontal)
        .padding(CommonsPadding.VerticalListItemLarge)
    val horizontalItemModifier = Modifier.padding(CommonsPadding.HorizontalListItem)

    item {
        Description(completeBill.type, completeBill.session, completeBill.bill, sectionModifier)
    }

    optionalItem(completeBill.publications) { publications ->
        Publications(publications, sectionModifier)
    }

    optionalItem(completeBill.sponsors) { sponsors ->
        Sponsors(sponsors, onSponsorClick, sectionModifier, horizontalItemModifier)
    }

    optionalItem(completeBill.getAnnotatedBillStages()) { stages ->
        Stages(stages, sectionModifier)
    }
}

@Composable
private fun Description(
    type: BillType,
    session: ParliamentarySession,
    bill: Bill,
    modifier: Modifier,
) {
    Column(modifier) {
        Caption(type.name dot session.name)
        Quote(bill.description)
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
            pluralResource(R.plurals.bill_publications, publications.size)
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
    sponsors: List<BillSponsorWithProfile>,
    onSponsorClick: SponsorAction,
    modifier: Modifier,
    itemModifier: Modifier,
) {
    Column(modifier) {
        PluralResourceText(R.plurals.bill_sponsors, sponsors.size)
        LazyRow {
            items(sponsors) { sponsor ->
                MemberLayout(
                    sponsor,
                    onClick = { onSponsorClick(sponsor) },
                    modifier = itemModifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Stages(
    stages: List<AnnotatedBillStage>,
    modifier: Modifier,
) {
    ComponentTitle(pluralResource(R.plurals.bill_stages, stages.size))

    StickyHeaderRow(
        items = stages,
        headerForItem = AnnotatedBillStage::category,
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
                        PluralResourceText(R.plurals.bill_sittings, sittings.size)
                    }
                    Caption(
                        sittings
                            .sortedBy { it.date }
                            .map { it.date.formatted() }
                            .dotted()
                    )
                }
            }
        },
        headerContent = { category ->
            category ?: return@StickyHeaderRow

            ComponentTitle(
                category.uiDescription(),
                Modifier
                    .padding(CommonsPadding.VerticalListItemLarge)
                    .padding(start = 8.dp, end = 32.dp),
                color = category.theme().onSurface,
                maxLines = 1,
                autoPadding = false,
            )
        },
        modifier = modifier,
        groupModifier = { category ->
            category ?: return@StickyHeaderRow Modifier
            Modifier
                .padding(8.dp)
                .clip(shapes.small)
                .background(category.theme().surface)
        }
    )
}

@Composable
private fun BillStageCategory.theme(): SurfaceTheme = when (this) {
    BillStageCategory.Commons -> House.commons.theme()
    BillStageCategory.Lords -> House.lords.theme()
    BillStageCategory.ConsiderationOfAmendments -> houseTheme(colors.house.Parliament)
    BillStageCategory.RoyalAssent -> houseTheme(colors.house.Royal)
}
