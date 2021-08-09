package org.beatonma.commons.app.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.insets.ProvideWindowInsets
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.Todo
import org.beatonma.commons.app.ui.compose.components.party.PartyDot
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansionAsState
import org.beatonma.commons.compose.animation.collapse
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.components.text.OptionalText
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillWidth
import org.beatonma.commons.compose.util.RequestFocusWhen
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.sampledata.SampleSearchResults
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.search.SearchResult
import org.beatonma.commons.theme.compose.Elevation
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.commons.theme.compose.theme.onSearchBar
import org.beatonma.commons.theme.compose.theme.searchBar
import org.beatonma.commons.theme.compose.theme.searchBarColors
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

internal typealias SearchUiState = ExpandCollapseState

internal object SearchTestTag {
    const val Bar = "search_bar"
    const val Field = TestTag.SearchField
    const val Icon = "search_icon"
    const val Result = "search_result"
}

class SearchActions(
    val onSubmit: (query: String) -> Unit,
    val onClickMember: (MemberSearchResult) -> Unit,
)

val LocalSearchActions: ProvidableCompositionLocal<SearchActions> =
    compositionLocalOf { error("SearchActions have not been registered") }

@Composable
fun SearchUi(
    results: List<SearchResult>,
    modifier: Modifier = Modifier,
    state: MutableState<SearchUiState> = rememberExpandCollapseState(),
    hint: String = stringResource(R.string.search_hint),
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    val expansionProgress by state.value.animateExpansionAsState()

    SearchLayout(
        hint = hint,
        toggleState = { state.toggle() },
        results = results,
        modifier = modifier,
        focusRequester = focusRequester,
        onBackgroundClick = state::collapse,
        expansionProgress = expansionProgress,
    )
}

@Composable
private fun SearchLayout(
    hint: String,
    toggleState: () -> Unit,
    results: List<SearchResult>,
    modifier: Modifier,
    focusRequester: FocusRequester,
    onBackgroundClick: () -> Unit,
    expansionProgress: Float,
) {
    ModalScrim(
        visible = expansionProgress > 0F,
        onClickAction = onBackgroundClick,
        onClickLabel = stringResource(R.string.content_description_close_overlay),
        modifier = Modifier.testTag(TestTag.ModalScrim)
    ) {
        Column(
            modifier
                .zIndex(Layer.AlwaysOnTopSurface),
        ) {
            val animation = searchBarAnimation(expansionProgress = expansionProgress)

            SearchBar(
                hint = hint,
                animation = animation,
                modifier = Modifier.align(Alignment.End),
                focusRequester = focusRequester,
                toggleState = toggleState
            )

            if (expansionProgress > 0F) {
                SearchResults(
                    results,
                    modifier = Modifier
                        .alpha(animation.resultsAlpha)
                        .wrapContentHeight(animation.resultsHeightProgress)
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    hint: String,
    animation: SearchBarAnimation,
    modifier: Modifier,
    focusRequester: FocusRequester,
    toggleState: () -> Unit,
) {
    Surface(
        modifier
            .wrapContentOrFillWidth(animation.widthProgress)
            .statusBarsPadding(animation.statusBarProgress.reversed())
            .testTag(SearchTestTag.Bar),
        elevation = animation.elevation,
        color = animation.surfaceColor,
        shape = animation.shape,
        contentColor = animation.contentColor
    ) {
        Row(
            Modifier
                .padding(Padding.SearchBar)
                .statusBarsPadding(animation.statusBarProgress),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (animation.showSearchField) {
                SearchField(
                    hint = hint,
                    focusRequester = focusRequester,
                    modifier = Modifier.weight(10F)
                )
            }

            SearchIcon(
                onClick = toggleState,
                modifier = Modifier.padding(start = 16.dp)
            )

            RequestFocusWhen(focusRequester, animation.expansionProgress == 1F)
        }
    }
}

@Composable
private fun SearchField(
    hint: String,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit = LocalSearchActions.current.onSubmit,
) {
    org.beatonma.commons.compose.components.text.SearchField(
        hint = hint,
        onQueryChange = onSubmit,
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
            Icons.Default.Search,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun SearchResults(
    results: List<SearchResult>,
    modifier: Modifier,
    actions: SearchActions = LocalSearchActions.current,
) {
    Surface(
        modifier.zIndex(Layer.AlwaysOnTopSurface)
    ) {
        LazyColumn {
            items(results) { item ->
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
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PartyDot(
                    result.party?.parliamentdotuk ?: -1,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(result.name)
            }
        },
        trailing = {
            OptionalText(
                result.currentPost ?: result.constituency?.name ?: result.party?.name,
                textAlign = TextAlign.End,
                maxLines = 2,
                modifier = Modifier.width(120.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickMember(result) }
            .padding(Padding.VerticalListItemLarge)
            .testTag(SearchTestTag.Result),
    )
}

@Composable
private fun searchBarAnimation(expansionProgress: Float) =
    SearchBarAnimation(
        expansionProgress,
        statusBarProgress = expansionProgress.progressIn(KeyframeFillStatusBar, 1F),
        widthProgress = expansionProgress.progressIn(0.1F, KeyframeFillWidth),
        elevation = expansionProgress.lerpBetween(0.dp, Elevation.ModalSurface),
        surfaceColor = expansionProgress
            .progressIn(0F, KeyframeIsOpaque)
            .lerpBetween(Color.Transparent, colors.searchBar),
        contentColor = expansionProgress.lerpBetween(LocalContentColor.current, colors.onSearchBar),
        shape = getSearchSurfaceShape(expansionProgress.progressIn(KeyframeIsOpaque, 1F)),
        resultsAlpha = expansionProgress.progressIn(KeyframeFillStatusBar, 1F),
        resultsHeightProgress = expansionProgress.progressIn(KeyframeFillWidth, 1F),
    )

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

private fun getSearchSurfaceShape(progress: Float): Shape {
    val cornerSize = progress.lerpBetween(48, 0).dp
    return RoundedCornerShape(cornerSize)
}

@Preview
@Composable
fun SearchTestLayout() {
    val state: MutableState<SearchUiState> = rememberExpandCollapseState()
    val results: MutableState<List<SearchResult>> = rememberListOf(SampleSearchResults)
    val onSubmit: (String) -> Unit = {}
    val onClickMember: (MemberSearchResult) -> Unit = {}

    val searchActions = remember {
        SearchActions(
            onSubmit = onSubmit,
            onClickMember = onClickMember,
        )
    }

    CommonsTheme {
        CompositionLocalProvider(
            LocalSearchActions provides searchActions,
        ) {
            ProvideWindowInsets {
                SearchUi(
                    results = results.value,
                    state = state,
                )
            }
        }
    }
}
