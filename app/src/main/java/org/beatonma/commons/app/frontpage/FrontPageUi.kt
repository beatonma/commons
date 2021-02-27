package org.beatonma.commons.app.frontpage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.beatonma.commons.app.search.SearchUi
import org.beatonma.commons.app.signin.ContextualUserAccountFabUi
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.snommoc.models.search.SearchResult
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsHeight

@Composable
fun FrontPageUi(result: IoResult<Zeitgeist>, searchResults: List<SearchResult>) {
    val searchUiState = rememberExpandCollapseState()

    Box {
        Column {
            Spacer(modifier = Modifier.statusBarsHeight())
            ZeitgeistResult(result)
        }

        SearchUi(
            searchResults,
            Modifier.align(Alignment.TopEnd),
            state = searchUiState,
        )

        ContextualUserAccountFabUi()
    }
}

@Composable
fun ZeitgeistResult(result: IoResult<Zeitgeist>) {
    WithResultData(result) { data ->
        ZeitgeistContent(data)
    }
}