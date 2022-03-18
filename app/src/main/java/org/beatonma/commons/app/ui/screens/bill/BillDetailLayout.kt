package org.beatonma.commons.app.ui.screens.bill

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.app.ui.components.Todo
import org.beatonma.commons.app.ui.components.members.MembersLayout
import org.beatonma.commons.app.ui.screens.bill.components.Publications
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialScaffold
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.uiDescription
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.components.DialogScaffold
import org.beatonma.commons.compose.components.StickyHeaderRow
import org.beatonma.commons.compose.components.text.Caption
import org.beatonma.commons.compose.components.text.PluralResourceText
import org.beatonma.commons.compose.components.text.Quote
import org.beatonma.commons.compose.components.text.ScreenTitle
import org.beatonma.commons.compose.layout.optionalItem
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.systemui.statusBarsPadding
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.core.House
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.CommonsPadding
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.themed.paddingValues
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
        DialogScaffold {
            ProvideSocial(
                viewmodel,
                socialViewModel,
                userAccountViewModel
            ) {
                BillDetailLayout(bill)
            }
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
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

    optionalItem(bill.sponsors) { sponsors ->
        Sponsors(sponsors, onSponsorClick, sectionModifier)
    }

    optionalItem(bill.stages) { stages ->
        Stages(stages, sectionModifier)
    }

    optionalItem(bill.publications) { publications ->
        Publications(publications, sectionModifier)
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
                        Text(stage.description, style = typography.h6)
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

private fun buildStageGroups(stages: List<BillStage>): List<StageGroup> {
    val groups = mutableListOf<StageGroup>()
    var house: House? = null
    val currentGroup = mutableListOf<BillStage>()

    stages.reversed().fastForEach { stage ->
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
