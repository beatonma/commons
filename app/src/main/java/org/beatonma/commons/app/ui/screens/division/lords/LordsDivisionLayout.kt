package org.beatonma.commons.app.ui.screens.division.lords

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.beatonma.commons.app.ui.screens.division.commons.LocalDivisionActions
import org.beatonma.commons.app.ui.screens.division.shared.DivisionVotes
import org.beatonma.commons.app.ui.screens.division.shared.DivisionVotesLaunchedEffect
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialScaffold
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.util.asAnnotatedHtml
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.core.LordsVoteType
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.division.LordsDivision
import org.beatonma.commons.data.core.room.entities.division.LordsDivisionVote
import org.beatonma.commons.repo.result.IoLoading


@Composable
fun LordsDivisionLayout(
    viewmodel: LordsDivisionViewModel,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
) {
    val result by viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(result) { division ->
        ProvideSocial(
            viewmodel,
            socialViewModel,
            userAccountViewModel,
        ) {
            LordsDivisionLayout(division)
        }
    }
}

@Composable
fun LordsDivisionLayout(
    division: LordsDivision,
    onMemberClick: (ParliamentID) -> Unit = LocalDivisionActions.current.onMemberClick,
) {
    val data = division.data
    val query = rememberText()
    val voteFilter = remember {
        mutableStateOf(
            LordsVoteType.values().toSet()
        )
    }
    var filteredVotes: List<LordsDivisionVote> by rememberListOf(division.votes)

    val listState = rememberLazyListState()

    DivisionVotesLaunchedEffect(
        query.value,
        voteFilter.value,
        votes = division.votes,
    ) {
        @Suppress("UNCHECKED_CAST")
        filteredVotes = it as List<LordsDivisionVote>
    }

    println(data.description.asAnnotatedHtml())
    SocialScaffold(
        scrollState = listState,
        title = data.title,
        aboveSocial = null,
    ) { modifier ->
        DivisionVotes(
            listState,
            division,
            filteredVotes,
            query,
            voteFilter,
            onMemberClick,
            modifier
        )
    }
}
