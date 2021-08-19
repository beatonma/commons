package org.beatonma.commons.app.frontpage

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import org.beatonma.commons.app.search.SearchUi
import org.beatonma.commons.app.search.SearchUiState
import org.beatonma.commons.app.signin.UserAccountFabUi
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.snommoc.models.search.SearchResult

@Composable
fun FrontPageUi(
    result: IoResult<Zeitgeist>,
    searchResults: List<SearchResult>,
    searchUiState: MutableState<SearchUiState>,
) {
    Box {
        WithResultData(result) { data ->
            ZeitgeistContent(data, Modifier.statusBarsPadding())
        }

        SearchUi(
            searchResults,
            Modifier.align(Alignment.TopEnd),
            state = searchUiState,
        )

        UserAccountFabUi()
    }
}
