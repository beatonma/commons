package org.beatonma.commons.app.ui.screens.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.EmptyList
import org.beatonma.commons.app.ui.components.Todo
import org.beatonma.commons.app.ui.components.party.PartyDot
import org.beatonma.commons.app.util.findNavigationController
import org.beatonma.commons.compose.Layer
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansionAsState
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.components.ListItem
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.components.text.OptionalText
import org.beatonma.commons.compose.layout.itemsOrEmpty
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillWidth
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.systemui.statusBarsPadding
import org.beatonma.commons.compose.util.RequestFocusWhen
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.data.core.room.entities.member.NoParty
import org.beatonma.commons.preview.InAppPreview
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.sampledata.SampleSearchResults
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.search.SearchResult
import org.beatonma.commons.theme.AppIcon
import org.beatonma.commons.theme.onSearchBar
import org.beatonma.commons.theme.searchBar
import org.beatonma.commons.theme.searchBarColors
import org.beatonma.commons.themed.animation
import org.beatonma.commons.themed.elevation
import org.beatonma.commons.themed.padding

internal typealias SearchUiState = ExpandCollapseState

internal object SearchTestTag {
    const val Bar = "search_bar"
    const val Field = TestTag.SearchField
    const val Icon = "search_icon"
    const val Result = "search_result"
}


enum class SearchDisplayType {
    Transparent,
    Opaque,
    ;

    val isTransparent: Boolean
        get() = this == Transparent
}

class SearchActions(
    val onSubmit: (query: String) -> Unit,
    val onClickMember: (MemberSearchResult) -> Unit,
)

val LocalSearchActions: ProvidableCompositionLocal<SearchActions> =
    compositionLocalOf { error("SearchActions have not been registered") }

@Composable
fun SearchUi(
    modifier: Modifier = Modifier,
    displayType: SearchDisplayType = SearchDisplayType.Transparent,
    hint: String = stringResource(R.string.search_hint),
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    val viewmodel = hiltViewModel<SearchViewModel>()
    val navController = findNavigationController()
    val result by viewmodel.livedata.observeAsState()
    var searchResults by remember {
        mutableStateOf(listOf<SearchResult>())
    }
    var state by rememberExpandCollapseState()

    BackHandler(enabled = state.isExpanded) {
        state = ExpandCollapseState.Collapsed
    }

    LaunchedEffect(result) {
        result?.onSuccess {
            searchResults = it
        }
    }

    CompositionLocalProvider(
        LocalSearchActions provides SearchActions(
            onSubmit = { query -> viewmodel.submit(query) },
            onClickMember = { navController.navigate(it.toUri()) }
        )
    ) {
        SearchUi(
            state,
            { state = it },
            searchResults,
            modifier,
            hint,
            displayType,
            focusRequester,
        )
    }
}

@Composable
private fun SearchUi(
    state: SearchUiState,
    onStateChange: (SearchUiState) -> Unit,
    results: List<SearchResult>,
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.search_hint),
    displayType: SearchDisplayType = SearchDisplayType.Transparent,
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    val expansionProgress by state.animateExpansionAsState()

    SearchLayout(
        state,
        { onStateChange(state.toggle()) },
        results,
        modifier,
        hint,
        displayType,
        focusRequester,
        onBackgroundClick = { onStateChange(ExpandCollapseState.Collapsed) },
        expansionProgress,
    )
}

@Composable
private fun SearchLayout(
    state: SearchUiState,
    toggleState: () -> Unit,
    results: List<SearchResult>,
    modifier: Modifier,
    hint: String,
    displayType: SearchDisplayType,
    focusRequester: FocusRequester,
    onBackgroundClick: () -> Unit,
    expansionProgress: Float,
    onSubmit: (String) -> Unit = LocalSearchActions.current.onSubmit,
) {
    ModalScrim(
        visible = expansionProgress > KeyFrameScrimVisible,
        onClickAction = onBackgroundClick,
        onClickLabel = stringResource(R.string.content_description_close_overlay),
        modifier = Modifier.testTag(TestTag.ModalScrim)
    ) {
        Column(
            modifier.zIndex(Layer.ModalSurface),
        ) {
            var query by rememberSaveable { mutableStateOf("") }
            val animation = searchBarAnimation(
                state,
                displayType,
                expansionProgress
            )

            SearchBar(
                query,
                onQueryChange = {
                    query = it
                    onSubmit(it)
                },
                hint = hint,
                animation = animation,
                focusRequester = focusRequester,
                toggleState = toggleState
            )

            if (expansionProgress > 0F) {
                SearchResults(
                    query,
                    results,
                    animation,
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    hint: String,
    animation: SearchBarAnimation,
    focusRequester: FocusRequester,
    toggleState: () -> Unit,
) {
    Surface(
        Modifier
            .align(Alignment.End)
            .wrapContentOrFillWidth(animation.widthProgress)
            .statusBarsPadding(animation.statusBarProgress.reversed())
            .testTag(SearchTestTag.Bar),
        elevation = animation.elevation,
        shape = animation.shape,
        color = animation.surfaceColor,
        contentColor = animation.contentColor
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .statusBarsPadding(animation.statusBarProgress),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (animation.showSearchField) {
                SearchField(
                    query = query,
                    onQueryChange = onQueryChange,
                    hint = hint,
                    focusRequester = focusRequester,
                    modifier = Modifier
                        .weight(10F)
                        .padding(end = 16.dp)
                )
            }

            SearchIcon(
                onClick = toggleState,
                modifier = Modifier
            )

            RequestFocusWhen(focusRequester, animation.expansionProgress == 1F)
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    hint: String,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    org.beatonma.commons.compose.components.text.SearchField(
        query,
        onQueryChange,
        hint = hint,
        modifier = modifier
            .focusRequester(focusRequester)
            .testTag(SearchTestTag.Field),
        colors = colors.searchBarColors,
    )
}

@Composable
private fun SearchIcon(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val contentDescription = stringResource(R.string.search_hint)

    IconButton(
        onClick = onClick,
        modifier = modifier.testTag(SearchTestTag.Icon)
    ) {
        Icon(
            AppIcon.Search,
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun SearchResults(
    query: String,
    results: List<SearchResult>,
    animation: SearchBarAnimation,
    actions: SearchActions = LocalSearchActions.current,
) {
    Surface(
        Modifier
            .alpha(animation.resultsAlpha)
            .wrapContentHeight(animation.resultsHeightProgress)
            .zIndex(Layer.AlwaysOnTopSurface)
    ) {
        LazyColumn(Modifier.fillMaxWidth()) {
            itemsOrEmpty(
                results,
                emptyContent = {
                    if (query.isNotBlank()) {
                        EmptyList(message = stringResource(R.string.search_results_empty))
                    }
                },
            ) { item ->
                when (item) {
                    is MemberSearchResult -> MemberSearchResult(item, actions.onClickMember)
                    else -> Todo("Unimplemented search result class: ${item.javaClass.canonicalName}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MemberSearchResult(
    result: MemberSearchResult,
    onClickMember: (MemberSearchResult) -> Unit,
) {
    ListItem(
        icon = {
            PartyDot(result.party?.parliamentdotuk ?: NoParty.parliamentdotuk)
        },
        text = {
            Text(result.name, fontWeight = FontWeight.Bold)
        },
        secondaryText = {
            Column {
                OptionalText(result.party?.name dot result.constituency?.name, maxLines = 1)
                OptionalText(text = result.currentPost, maxLines = 1)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickMember(result) }
            .padding(padding.VerticalListItem)
            .testTag(SearchTestTag.Result),
    )
}

@Composable
private fun searchBarAnimation(
    state: SearchUiState,
    displayType: SearchDisplayType,
    expansionProgress: Float,
): SearchBarAnimation {
    val collapsedBackgroundColor by animation.animateColorAsState(
        if (displayType.isTransparent) colors.background else colors.searchBar
    )
    val collapsedContentColor by animation.animateColorAsState(
        if (displayType.isTransparent) LocalContentColor.current else colors.onSearchBar
    )
    val statusBarProgress by animation.animateFloatAsState(
        when (displayType) {
            SearchDisplayType.Opaque -> 1f
            else -> expansionProgress.progressIn(KeyframeFillStatusBar, 1F)
        }
    )
    val _elevation by animation.animateDpAsState(
        when (displayType) {
            SearchDisplayType.Opaque -> elevation.ModalSurface
            else -> expansionProgress.lerpBetween(0.dp, elevation.ModalSurface)
        }
    )

    return SearchBarAnimation(
        expansionProgress,
        statusBarProgress = statusBarProgress,
        widthProgress = expansionProgress.progressIn(0.1F, KeyframeFillWidth),
        elevation = _elevation,
        surfaceColor = expansionProgress
            .progressIn(0F, KeyframeIsOpaque)
            .lerpBetween(
                collapsedBackgroundColor,
                colors.searchBar
            ),
        contentColor = expansionProgress.lerpBetween(collapsedContentColor, colors.onSearchBar),
        shape = getSearchSurfaceShape(state,
            displayType,
            expansionProgress.progressIn(KeyframeIsOpaque, 1F)),
        resultsAlpha = expansionProgress.progressIn(KeyframeFillStatusBar, 1F),
        resultsHeightProgress = expansionProgress.progressIn(KeyframeFillWidth, 1F),
    )
}

private class SearchBarAnimation(
    val expansionProgress: Float,
    val statusBarProgress: Float,
    val widthProgress: Float,
    val elevation: Dp,
    val surfaceColor: Color,
    val contentColor: Color,
    val shape: Shape,
    val resultsAlpha: Float,
    val resultsHeightProgress: Float,
) {
    val showSearchField: Boolean = expansionProgress > KeyframeFillWidth
}

private const val KeyframeIsOpaque = 0.2F
private const val KeyframeFillWidth = 0.4F
private const val KeyframeFillStatusBar = 0.6F
private const val KeyFrameScrimVisible = 0.5f

@Composable
private fun getSearchSurfaceShape(
    state: SearchUiState,
    displayType: SearchDisplayType,
    expansionProgress: Float,
): Shape {
    val bottomStartPercent = expansionProgress.lerpBetween(50, 0)
    val otherPercent by animation.animateIntAsState(targetValue = when {
        state.isExpanded -> 0
        displayType.isTransparent -> bottomStartPercent
        else -> 50 - bottomStartPercent
    })

    return RoundedCornerShape(otherPercent, otherPercent, otherPercent, bottomStartPercent)
}

@Preview
@Composable
fun SearchTestLayout() {
    var state: SearchUiState by rememberExpandCollapseState()
    val results: List<SearchResult> by remember { mutableStateOf(SampleSearchResults) }
    val onSubmit: (String) -> Unit = {}
    val onClickMember: (MemberSearchResult) -> Unit = {}

    val searchActions = remember {
        SearchActions(
            onSubmit = onSubmit,
            onClickMember = onClickMember,
        )
    }
    InAppPreview {
        CompositionLocalProvider(
            LocalSearchActions provides searchActions,
        ) {
            ProvideWindowInsets {
                SearchUi(
                    state,
                    { state = it },
                    results = results,
                )
            }
        }
    }
}
