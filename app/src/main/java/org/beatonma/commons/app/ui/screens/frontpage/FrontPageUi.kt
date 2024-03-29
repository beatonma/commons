package org.beatonma.commons.app.ui.screens.frontpage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.beatonma.commons.app.ui.screens.search.SearchDisplayType
import org.beatonma.commons.app.ui.screens.search.SearchUi
import org.beatonma.commons.app.ui.screens.signin.UserAccountFabUi

@Composable
fun FrontPageUi() {
    val zeitgeistListState = rememberLazyListState()
    var searchDisplayType by remember { mutableStateOf(SearchDisplayType.Transparent) }

    Box {
        ZeitgeistContent(lazyListState = zeitgeistListState)

        SearchUi(
            modifier = Modifier.align(Alignment.TopEnd),
            displayType = searchDisplayType
        )

        UserAccountFabUi()
    }

    LaunchedEffect(zeitgeistListState.firstVisibleItemScrollOffset) {
        searchDisplayType = when {
            // First item is a spacer, search icon becomes opaque when scrolled to/past header.
            zeitgeistListState.firstVisibleItemIndex > 0 -> SearchDisplayType.Opaque
            else -> SearchDisplayType.Transparent
        }
    }
}
