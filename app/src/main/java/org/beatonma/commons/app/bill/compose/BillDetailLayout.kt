package org.beatonma.commons.app.bill.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.social.ProvideSocial
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.StickySocialScaffold
import org.beatonma.commons.app.ui.colors.HouseTheme
import org.beatonma.commons.app.ui.colors.houseTheme
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.components.CollapsedColumn
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.components.PluralText
import org.beatonma.commons.compose.components.ResourceText
import org.beatonma.commons.compose.components.Tag
import org.beatonma.commons.compose.layout.optionalItem
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.resolution.description
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.components.Caption
import org.beatonma.commons.theme.compose.components.ComponentTitle
import org.beatonma.commons.theme.compose.components.Quote
import org.beatonma.commons.theme.compose.components.ScreenTitle
import org.beatonma.commons.theme.compose.formatting.formatted
import org.beatonma.commons.theme.compose.theme.house
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding
import org.beatonma.commons.theme.compose.util.dot
import org.beatonma.commons.theme.compose.util.dotted

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
        Stages(completeBill.getAnnotatedBillStages())//, sectionModifier, horizontalItemModifier)
    }
}

@Composable
private fun Publications(
    publications: List<BillPublication>,
    modifier: Modifier,
) {
    CollapsedColumn(
        publications,
        headerBlock = CollapsedColumn.simpleHeader(stringResource(R.string.bill_publications), "${publications.size}"),
        modifier = modifier,
    ) { publication, itemModifier ->
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
        headerForItem = { it.house },
        headerContent = { house, headerModifier ->
            val theme = house.theme()
            ComponentTitle(
                house.description(),
                headerModifier
                    .background(theme.surface),
                color = theme.onSurface,
                maxLines = 1,
            )
        },
        itemContent = { item ->
            val theme = item.theme()
            val stage = item.stage.stage
            val sittings = item.stage.sittings

            Providers(AmbientContentColor provides theme.onSurface) {
                Column(
                    Modifier
                        .background(theme.surface)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .clip(shapes.small)
                        .background(theme.onSurface.copy(alpha = .1F))
                        .padding(8.dp)
                ) {
                    Text(stage.type)
                    PluralText(R.plurals.bill_sittings, quantity = sittings.size)
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


private val ScreenPaddingModifier = Modifier.padding(Padding.ScreenHorizontal)

@Composable
private fun AnnotatedBillStage.theme(): HouseTheme = when {
    this.house != null -> this.house.theme()
    this.isConsiderationOfAmendments() -> houseTheme(colors.house.Parliament)
    this.isRoyalAssent() -> houseTheme(colors.house.Royal)
    else -> houseTheme()
}

@Composable
fun <T, H> StickyHeaderRow(
    items: List<T>,
    headerForItem: (T) -> H?,
    headerContent: @Composable (H, Modifier) -> Unit,
    itemContent: @Composable LazyItemScope.(T) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val scrollController = rememberScrollableController { delta ->
        coroutineScope.launch { state.scroll { scrollBy(delta) } }
        delta
    }

    Column(
        modifier
    ) {
        StickyHeader(
            items,
            headerForItem,
            state,
            Modifier.scrollable(
                orientation = Orientation.Horizontal,
                controller = scrollController,
                reverseDirection = true,
            ),
            content = headerContent
        )

        LazyRow(
            state = state,
        ) {
            this.items(items, itemContent)
        }
    }
}

@Composable
fun <T, H> StickyHeader(
    items: List<T>,
    headerIdentifierForItem: (T) -> H?,
    state: LazyListState,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable (H, Modifier) -> Unit,
) {
    val itemsInfo = state.layoutInfo.visibleItemsInfo

    // Get header identifier for each visible item.
    val headers: List<H?> = itemsInfo.map { headerIdentifierForItem(items[it.index]) }
    if (headers.isEmpty()) return

    // Build list of positions where the header identifier changes.
    val headerPositions = mutableListOf<Int>()
    var previousHeader: H? = null
    headers.fastForEachIndexed { index, h ->
        if (h != null && h != previousHeader) {
            headerPositions += index
            previousHeader = h
        }
    }

    println("Headers at positions $headerPositions")
    headerPositions.forEachIndexed { index, h ->
        val info = itemsInfo[index]
        println("[$index] index:${info.index} | offset:${info.offset} | width:${info.size}")
    }

    // Construct Composable representation for each header.
    val headerContent = @Composable {
        headerPositions.fastForEach { index ->
            val h = headers[index] ?: return@fastForEach

            content(h, Modifier)
        }
    }

    Layout(
        content = headerContent,
        modifier = modifier,
    ) { measurables, constraints ->
        val xPositions = mutableListOf<Int>()

        var headerHeight: Int? = null
        val placeables = measurables.mapIndexed { index, measurable ->
            val headerIndex = headerPositions[index]
            val nextHeaderIndex = headerPositions.getOrNull(index + 1) ?: -1

            val info = itemsInfo[headerIndex]
            val nextInfo = itemsInfo.getOrNull(nextHeaderIndex)

            val x = when (nextInfo) {
                null -> info.offset.coerceAtLeast(0)
                else -> info.offset.coerceAtMost(nextInfo.offset)
            }
            xPositions += x

            val itemWidth = if (nextInfo == null) constraints.maxWidth - x
            else nextInfo.offset - info.offset

            measurable.measure(
                constraints.copy(
                    minWidth = itemWidth,
                    maxWidth = itemWidth,
                    minHeight = headerHeight ?: constraints.minHeight,
                    maxHeight = headerHeight ?: constraints.maxHeight
                )
            ).also {
                // The height of the first header dictates the height of all others.
                if (headerHeight == null) {
                    headerHeight = it.height
                }
            }
        }

        val width: Int = constraints.maxWidth
        val height: Int = placeables.maxOf(Placeable::height)

        layout(width, height) {
            placeables.forEachIndexed { index, p ->
                val x = xPositions[index]
                val y = verticalAlignment.align(p.height, height)
                p.placeRelative(x, y)
            }
        }
    }
}
