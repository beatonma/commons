package org.beatonma.commons.app.search

import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.transition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.beatonma.commons.R
import org.beatonma.commons.app.preview.ProvidePreviewAmbients
import org.beatonma.commons.app.ui.compose.components.PartyDot
import org.beatonma.commons.app.ui.compose.components.Todo
import org.beatonma.commons.app.ui.compose.components.image.DEV_AVATAR
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.collapse
import org.beatonma.commons.compose.animation.isExpanded
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseTransition
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.components.ModalScrim
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.components.SearchTextField
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillWidth
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.snommoc.models.search.ConstituencySearchResult
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.search.PartySearchResult
import org.beatonma.commons.snommoc.models.search.SearchResult
import org.beatonma.commons.theme.compose.Elevation
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.color.CommonsColor
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

private const val KeyframeIsOpaque = 0.2F
private const val KeyframeFillWidth = 0.4F
private const val KeyframeFillStatusBar = 0.6F

class SearchActions(
    val onSubmit: (query: String) -> Unit,
    val onClickMember: (MemberSearchResult) -> Unit,
)

private typealias SearchUiState = ExpandCollapseState

@Composable
@Preview
fun SearchUiPreview() {
    val results: MutableState<List<SearchResult>> = mutableStateOf(
        listOf(
            MemberSearchResult(
                name = "Michael Beaton",
                constituency = ConstituencySearchResult(
                    10, "Highland"
                ),
                party = PartySearchResult(100001, "Independent"),
                parliamentdotuk = 65,
                currentPost = "Developer",
                portraitUrl = DEV_AVATAR,
            ),
        )
    )

    ProvidePreviewAmbients {
        SearchUi(
            results.value,
        )
    }
}

@Composable
fun SearchUi(
    results: List<SearchResult>,
    modifier: Modifier = Modifier,
    state: MutableState<SearchUiState> = rememberExpandCollapseState(),
    transition: TransitionDefinition<SearchUiState> = rememberExpandCollapseTransition(),
    transitionState: TransitionState = transition(transition, toState = state.value),
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    val progress = transitionState[progressKey]

    ModalScrim(
        alpha = progress,
        onClickAction = state::collapse,
    ) {
        Column(
            modifier
                .zIndex(Layer.AlwaysOnTopSurface),
        ) {
            val statusBarProgress = progress.progressIn(KeyframeFillStatusBar, 1F)

            Surface(
                Modifier
                    .wrapContentOrFillWidth(progress.progressIn(0.1F, KeyframeFillWidth))
                    .statusBarsPadding(statusBarProgress.reversed())
                    .align(Alignment.End),
                elevation = progress.lerpBetween(0.dp, Elevation.ModalSurface),
                color = progress.progressIn(0F, KeyframeIsOpaque)
                    .lerpBetween(Color.Transparent, CommonsColor.SearchBar),
                shape = getSearchSurfaceShape(progress.progressIn(KeyframeIsOpaque, 1F)),
                contentColor = progress.lerpBetween(AmbientContentColor.current,
                    CommonsColor.OnSearchBar)
            ) {
                Row(
                    Modifier
                        .statusBarsPadding(statusBarProgress)
                        .padding(Padding.Screen),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (progress > KeyframeFillWidth) {
                        SearchField(
                            focusRequester = focusRequester,
                            modifier = Modifier.weight(10F)
                        )

                        if (progress == 1F && state.isExpanded) {
                            focusRequester.requestFocus()
                        }
                    }
                    SearchIcon(state, Modifier.padding(start = 16.dp))
                }
            }

            if (progress > 0F) {
                SearchResults(
                    results,
                    modifier = Modifier
                        .alpha(progress.progressIn(KeyframeFillStatusBar, 1F))
                        .wrapContentHeight(progress.progressIn(KeyframeFillWidth, 1F))
                )
            }
        }
    }
}

@Composable
private fun SearchField(
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit = AmbientSearchActions.current.onSubmit,
) {
    SearchTextField(
        hint = R.string.search_members_hint,
        onQueryChange = onSubmit,
        modifier = modifier.focusRequester(focusRequester),
        textStyle = AmbientTextStyle.current.copy(color = CommonsColor.OnSearchBar),
    )
}

@Composable
private fun SearchIcon(
    state: MutableState<SearchUiState>,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = { state.toggle() }, modifier = modifier) {
        Icon(Icons.Default.Search)
    }
}

@Composable
fun SearchResults(
    results: List<SearchResult>,
    modifier: Modifier = Modifier,
    actions: SearchActions = AmbientSearchActions.current,
) {
    Surface(
        modifier.zIndex(Layer.AlwaysOnTopSurface)
    ) {
        LazyColumn {
            items(items = results, itemContent = { item ->
                when (item) {
                    is MemberSearchResult -> MemberSearchResult(item, actions.onClickMember)
                    else -> Todo(message = "Unimplemented search result class: ${item.javaClass.canonicalName}")
                }
            })
        }
    }
}

@Composable
private fun MemberSearchResult(
    result: MemberSearchResult,
    onClickMember: (MemberSearchResult) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClickMember(result) }
            .padding(Padding.VerticalListItemLarge),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
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
            }
        )
    }
}

private fun getSearchSurfaceShape(progress: Float): Shape {
    val cornerSize = progress.lerpBetween(48, 0).dp
    return RoundedCornerShape(cornerSize)
}
