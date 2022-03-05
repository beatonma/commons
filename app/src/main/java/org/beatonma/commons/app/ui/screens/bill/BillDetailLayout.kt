package org.beatonma.commons.app.ui.screens.bill

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.app.ui.components.Todo
import org.beatonma.commons.app.ui.components.members.MembersLayout
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialScaffold
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.uiDescription
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.app.util.openUrl
import org.beatonma.commons.compose.components.CollapsedColumn
import org.beatonma.commons.compose.components.StickyHeaderRow
import org.beatonma.commons.compose.components.Tag
import org.beatonma.commons.compose.components.text.Caption
import org.beatonma.commons.compose.components.text.PluralResourceText
import org.beatonma.commons.compose.components.text.Quote
import org.beatonma.commons.compose.components.text.ScreenTitle
import org.beatonma.commons.compose.layout.optionalItem
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.systemui.statusBarsPadding
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.pluralResource
import org.beatonma.commons.core.House
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillPublication
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationLink
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.CommonsPadding
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.themed.paddingValues
import org.beatonma.commons.themed.themedPadding
import java.time.LocalDate

internal val LocalBillActions: ProvidableCompositionLocal<BillActions> =
    compositionLocalOf { BillActions() }

@Composable
fun BillDetailLayout(
    viewmodel: BillDetailViewModel,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
) {
    Todo(message = "BillDetailLayout", Modifier.statusBarsPadding())
    val resultState by viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(resultState) { bill: Bill ->
        ProvideSocial(
            viewmodel,
            socialViewModel,
            userAccountViewModel
        ) {
            BillDetailLayout(bill)
        }
    }
}

@Composable
fun BillDetailLayout(
    bill: Bill,
    onSponsorClick: SponsorAction = LocalBillActions.current.onClickProfile,
) {
    SocialScaffold(
        title = { ScreenTitle(bill.data.title) },
        aboveSocial = null,
        belowSocial = null,
    ) { modifier ->
        lazyContent(bill, onSponsorClick, modifier)
    }
}


@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.lazyContent(
    bill: Bill,
    onSponsorClick: SponsorAction,
    modifier: Modifier = Modifier,
) {
    val sectionModifier = modifier
        .padding(CommonsPadding.VerticalListItemLarge)

    item {
        Description(bill.type, bill.sessions, bill, sectionModifier)
    }

    optionalItem(bill.publications) { publications ->
        Publications(publications, sectionModifier)
    }

    optionalItem(bill.sponsors) { sponsors ->
        Sponsors(sponsors, onSponsorClick, sectionModifier)
    }

    optionalItem(bill.stages) { stages ->
        Stages(stages, sectionModifier)
    }
}

@Composable
private fun Description(
    type: BillType,
    sessions: List<ParliamentarySession>,
    bill: Bill,
    modifier: Modifier,
) {
    Column(modifier) {
        Caption(type.name dot sessions.map(ParliamentarySession::name).dotted())
        Quote(bill.data.description)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun Publications(
    publications: List<BillPublication>,
    modifier: Modifier,
) {
    var focussed: BillPublication? by remember { mutableStateOf(null) }

    CollapsedColumn(
        publications,
        headerBlock = CollapsedColumn.simpleHeader(
            pluralResource(R.plurals.bill_publications, publications.size)
        ),
        modifier = modifier,
        scrollable = false,
    ) { publication ->
        ListItem(
            text = { Text(publication.data.title) },
            secondaryText = { Text(publication.data.type dot publication.data.date.formatted()) },
            modifier = Modifier.clickable { focussed = publication }
        )
    }

    withNotNull(focussed) { pub ->
        AlertDialog(
            onDismissRequest = { focussed = null },
            title = { Text(pub.data.type) },
            text = { Publication(pub) },
            buttons = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }
}

@Composable
private fun Sponsors(
    sponsors: List<BillSponsor>,
    onSponsorClick: SponsorAction,
    modifier: Modifier,
) {
    Column(modifier) {
        PluralResourceText(R.plurals.bill_sponsors, sponsors.size)

        MembersLayout(
            sponsors.mapNotNull { it.member },
            onClick = { onSponsorClick(it) },
            showImages = false,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Stages(
    stages: List<BillStage>,
    modifier: Modifier,
) {
//    ComponentTitle(pluralResource(R.plurals.bill_stages, stages.size))
    val groupedStages by remember(stages) { mutableStateOf(buildStageGroups(stages)) }

    StickyHeaderRow(
        items = groupedStages,
        headerForItem = { it },
        itemContent = { item ->
            val theme = item.house.theme()

            CompositionLocalProvider(
                LocalContentColor provides theme.onSurface
            ) {
                item.stages.forEach { stage ->
                    Column(
                        Modifier
                            .padding(paddingValues(horizontal = 12.dp, top = 6.dp, bottom = 16.dp))
                            .clip(shapes.small)
                            .background(theme.onSurface.copy(alpha = .1F))
                            .padding(8.dp)
                    ) {
                        Text(stage.description, style = typography.h5)
                        Text(stage.sittings.map(LocalDate::formatted).dotted(),
                            style = typography.caption)
                    }
                }
            }
        },
        headerContent = { group ->
            withNotNull(group?.house) { house ->
                Text(
                    house.uiDescription(),
                    Modifier
                        .padding(top = 8.dp)
                        .padding(start = 8.dp, end = 32.dp),
                    color = house.theme().onSurface,
                    maxLines = 1,
                    style = typography.overline,
                )
            }
        },
        modifier = modifier,
        groupModifier = { stage ->
            stage ?: return@StickyHeaderRow Modifier
            Modifier
                .padding(start = 4.dp, end = 8.dp)
                .clip(shapes.small)
                .background(stage.house.theme().surface)
        }
    )
}

@Composable
private fun Publication(publication: BillPublication) {
    Column {
        publication.links.forEach { link ->
            PublicationLink(link)
        }
    }
}

@Composable
private fun PublicationLink(link: BillPublicationLink) {
    val onClick = openUrl(link.url)

    val label = when (link.contentType) {
        "application/pdf" -> "PDF"
        else -> null
    }

    Column(
        Modifier
            .clickable(onClick = onClick)
            .padding(themedPadding.VerticalListItem)
    ) {
        Text(link.title, style = typography.h5)

        Row(verticalAlignment = Alignment.CenterVertically) {
            withNotNull(label) {
                Tag(it, color = colors.error, contentColor = colors.onError)
            }
            Text(link.url)
        }
    }
}

private fun buildStageGroups(stages: List<BillStage>): List<StageGroup> {
    val groups = mutableListOf<StageGroup>()
    var house: House? = null
    val currentGroup = mutableListOf<BillStage>()

    stages.fastForEach { stage ->
        when {
            house == null -> {
                house = stage.house
                currentGroup += stage
            }
            stage.house == house -> {
                currentGroup += stage
            }
            else -> {
                groups += StageGroup(house!!, currentGroup.toList())
                currentGroup.clear()
                house = stage.house
                currentGroup += stage
            }
        }
    }

    if (house != null && currentGroup.isNotEmpty()) {
        groups += StageGroup(house!!, currentGroup.toList())
    }

    return groups
}

private data class StageGroup(
    val house: House,
    val stages: List<BillStage>,
)
