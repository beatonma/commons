package org.beatonma.commons.app.division.compose

import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.SwipeableState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.app.ui.compose.components.LoadingIcon
import org.beatonma.commons.app.ui.compose.components.charts.ChartItem
import org.beatonma.commons.app.ui.compose.components.charts.ChartKeyItem
import org.beatonma.commons.app.ui.compose.components.charts.HorizontalStackedBarChart
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.collapse
import org.beatonma.commons.compose.animation.expand
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseTransition
import org.beatonma.commons.compose.components.SearchTextField
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.mapUpdate
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.compose.util.rememberList
import org.beatonma.commons.compose.util.rememberSet
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.util.toggle
import org.beatonma.commons.compose.util.withAnnotatedStyle
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.core.extensions.map
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.VoteWithParty
import org.beatonma.commons.data.resolution.description
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.kotlin.extensions.withMainContext
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.compose.EndOfContent
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import org.beatonma.commons.theme.compose.theme.politicalVotes
import org.beatonma.commons.theme.compose.theme.quote
import org.beatonma.commons.theme.compose.theme.screenTitle
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding
import kotlin.math.absoluteValue

internal val AmbientDivisionActions: ProvidableAmbient<DivisionActions> =
    ambientOf(defaultFactory = ::DivisionActions)

class DivisionActions(
    val onMemberClick: (ParliamentID) -> Unit = {},
)

@Composable
fun DivisionDetailLayout(
    viewmodel: DivisionDetailViewModel,
) {
    val resultState = viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(resultState.value) {
        DivisionDetailLayout(it)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DivisionDetailLayout(
    divisionWithVotes: DivisionWithVotes,
) {
    val voteTypeFilters = rememberSet<VoteType>()

    val query = rememberText()
    val coroutineScope = rememberCoroutineScope()
    val filteredVotes = rememberList<VoteWithParty>()
    val lazyListState = rememberLazyListState()

    val applyFilter = { queryText: String ->
        coroutineScope.launch {
            val filtered = applyFilter(queryText, voteTypeFilters.value, divisionWithVotes)

            withMainContext { filteredVotes.value = filtered }
        }
    }

    val toggleVoteTypeFilter = { voteType: VoteType ->
        voteTypeFilters.mapUpdate { filters ->
            val exists = filters.contains(voteType)
            if (exists) {
                filters.filterNot { it == voteType }.toSet()
            }
            else {
                filters + setOf(voteType)
            }
        }
        applyFilter("")
    }
    applyFilter("")

    val expandState = rememberExpandCollapseState(ExpandCollapseState.Expanded)
    val swipeState = rememberSwipeableState(
        expandState.value,
        animationSpec = CommonsSpring()
    )

    Column {
        Header(
            divisionWithVotes.division,
            onVoteTypeClick = toggleVoteTypeFilter,
            lazyListState = lazyListState,
            swipeState = swipeState,
        )

        SearchField(
            query = query,
            modifier = Modifier.fillMaxWidth()
                .padding(Padding.ScreenHorizontal),
            onQueryChange = applyFilter
        )

        VoteList(
            filteredVotes.value,
            lazyListState,
            divisionWithVotes.votes.isEmpty()
        )
    }
}

@OptIn(ExperimentalLayout::class, ExperimentalMaterialApi::class)
@Composable
private fun Header(
    division: Division,
    onVoteTypeClick: (VoteType) -> Unit,
    lazyListState: LazyListState,
    expandState: MutableState<ExpandCollapseState> = rememberExpandCollapseState(ExpandCollapseState.Expanded),
    swipeState: SwipeableState<ExpandCollapseState> = rememberSwipeableState(
        expandState.value,
        animationSpec = CommonsSpring()
    ),
    transitionDefinition: TransitionDefinition<ExpandCollapseState> = rememberExpandCollapseTransition(),
) {
    val transition = transition(transitionDefinition, toState = expandState.value)

    if (lazyListState.firstVisibleItemIndex == 0) {
        val itemLayoutInfo = lazyListState.layoutInfo.visibleItemsInfo.getOrNull(0)
        val scrollProgress =
            ((itemLayoutInfo?.offset?.toFloat() ?: 0F) / (itemLayoutInfo?.size?.toFloat()
                ?: 1F)).absoluteValue
        if (scrollProgress > 0F) {
            expandState.collapse()
        }
        else {
            expandState.expand()
        }
    }


    val (title, description) = when (division.house) {
        House.commons -> Pair(stringResource(R.string.division_title), division.title)
        House.lords -> Pair(division.title,
            division.description ?: stringResource(R.string.division_no_description))
    }

    val swipeAnchors = mapOf(
        0F to ExpandCollapseState.Expanded,
        500F to ExpandCollapseState.Collapsed
    )
    val offset = swipeState.offset.value
    val progress = (if (offset.isNaN()) 0F else offset).map(0F, 500F, 0F, 1F)

    val expandProgress = minOf(transition[progressKey], progress)

    Column(
        Modifier.padding(Padding.Screen)
            .statusBarsPadding()
            .swipeable(
                state = swipeState,
                anchors = swipeAnchors,
                orientation = Orientation.Vertical,
                reverseDirection = false,
            )
    ) {
        Column(
            Modifier.wrapContentHeight(expandProgress)
                .alpha(expandProgress)
        ) {
            Text(title, style = typography.screenTitle)

            Text(dotted(division.house.description(), division.date.formatted()))

            Text(
                description,
                style = typography.quote,
                modifier = Modifier.padding(Padding.Card))
        }

        Chart(division, Modifier.fillMaxWidth())

        GraphKey(division, onVoteTypeClick)
    }
}

@Composable
private fun VoteList(
    filteredVotes: List<VoteWithParty>,
    lazyListState: LazyListState,
    isLoading: Boolean,
) {
    LazyColumn(
        state = lazyListState,
    ) {
        if (isLoading) {
            item {
                LoadingIcon(Modifier.fillMaxWidth())
            }
            return@LazyColumn
        }

        items(
            items = filteredVotes,
            itemContent = { vote ->
                Vote(vote)
            }
        )

        item {
            EndOfContent()
        }
    }
}

@Composable
private fun Vote(
    voteWithParty: VoteWithParty,
    modifier: Modifier = Modifier,
    onMemberClick: (ParliamentID) -> Unit = AmbientDivisionActions.current.onMemberClick,
) {
    ListItem(
        modifier = modifier.clickable(onClick = { onMemberClick(voteWithParty.vote.memberId) }),
        icon = { VoteIcon(voteWithParty.vote.voteType) },
        text = { Text(voteWithParty.vote.memberName, maxLines = 2) },
        trailing = { Text(voteWithParty.party?.name ?: "") }
    )
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    query: MutableState<String> = rememberText(),
    onQueryChange: (String) -> Unit,
) =
    SearchTextField(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        hint = R.string.division_search_member_hint,
    )

@Composable
private fun VoteIcon(voteType: VoteType) = VoteIcon(voteType.icon, voteType.color)

@Composable
private fun VoteIcon(
    icon: ImageVector,
    tint: Color,
) {
    Icon(
        icon,
        tint = tint,
        modifier = Modifier
            .padding(Padding.IconSmall)
            .size(Size.IconSmall)
    )
}

@OptIn(ExperimentalLayout::class)
@Composable
private fun GraphKey(
    division: Division,
    onVoteTypeClick: (VoteType) -> Unit,
) {
    val keyModifier = Modifier.padding(Padding.HorizontalListItem)

    FlowRow {
        GraphKeyItem(VoteType.AyeVote,
            division.ayes,
            onClick = onVoteTypeClick,
            modifier = keyModifier)
        GraphKeyItem(VoteType.NoVote,
            division.noes,
            onClick = onVoteTypeClick,
            modifier = keyModifier)
        GraphKeyItem(VoteType.Abstains,
            division.abstentions ?: 0,
            onClick = onVoteTypeClick,
            modifier = keyModifier)
        GraphKeyItem(VoteType.DidNotVote,
            division.didNotVote ?: 0,
            onClick = onVoteTypeClick,
            modifier = keyModifier)
        GraphKeyItem(VoteType.SuspendedOrExpelledVote,
            division.suspendedOrExpelled ?: 0,
            onClick = onVoteTypeClick,
            modifier = keyModifier)
    }
}

@Composable
private fun GraphKeyItem(
    voteType: VoteType,
    voteCount: Int,
    modifier: Modifier = Modifier,
    interactionState: InteractionState = remember(::InteractionState),
    onClick: (VoteType) -> Unit,
) {
    val selected = rememberBoolean(false)
    if (voteCount == 0) return

    ChartKeyItem(
        { VoteIcon(voteType.icon, voteType.color) },
        { Text(stringResource(voteType.descriptionRes, voteCount).withAnnotatedStyle()) },
        modifier = modifier.selectable(
            selected = selected.value,
            onClick = {
                selected.toggle()
                onClick(voteType)
            },
            interactionState = interactionState,
            indication = rememberRipple(),
        ),
    )
}

@Composable
private fun Chart(
    division: Division,
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
) {
    HorizontalStackedBarChart(
        ChartItem(division.ayes, colors.politicalVotes.Aye, ""),
        ChartItem(division.didNotVote ?: 0, colors.politicalVotes.DidNotVote, ""),
        ChartItem(division.abstentions ?: 0, colors.politicalVotes.Abstain, ""),
        ChartItem(division.suspendedOrExpelled ?: 0, colors.politicalVotes.SuspendedOrExpelled, ""),
        ChartItem(division.noes, colors.politicalVotes.No, ""),
        modifier = modifier,
        height = height,
    )
}

private val VoteType.color
    @Composable get() = when (this) {
        VoteType.AyeVote -> colors.politicalVotes.Aye
        VoteType.NoVote -> colors.politicalVotes.No
        VoteType.DidNotVote -> colors.politicalVotes.DidNotVote
        VoteType.Abstains -> colors.politicalVotes.Abstain
        VoteType.SuspendedOrExpelledVote -> colors.politicalVotes.SuspendedOrExpelled
    }


private val VoteType.icon
    @Composable get() = when (this) {
        VoteType.AyeVote -> Icons.Default.AddCircle
        VoteType.NoVote -> Icons.Default.RemoveCircle
        else -> Icons.Default.Cancel
    }

private val VoteType.descriptionRes
    @Composable get() = when (this) {
        VoteType.AyeVote -> R.string.division_ayes
        VoteType.NoVote -> R.string.division_noes
        VoteType.DidNotVote -> R.string.division_abstained
        VoteType.Abstains -> R.string.division_did_not_vote
        VoteType.SuspendedOrExpelledVote -> R.string.division_suspended_or_expelled
    }

private fun applyFilter(
    queryText: String,
    voteTypeFilters: Set<VoteType>,
    division: DivisionWithVotes,
) =
    when {
        voteTypeFilters.isEmpty() -> division.votes
        else -> division.votes.filter { it.vote.voteType in voteTypeFilters }
    }
        .filter { it.vote.memberName.contains(queryText, ignoreCase = true) }
