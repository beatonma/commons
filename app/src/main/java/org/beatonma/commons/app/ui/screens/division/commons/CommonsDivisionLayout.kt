package org.beatonma.commons.app.ui.screens.division.commons

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.screens.division.DivisionActions
import org.beatonma.commons.app.ui.screens.division.MemberVoteAction
import org.beatonma.commons.app.ui.screens.division.shared.DivisionVotes
import org.beatonma.commons.app.ui.screens.division.shared.DivisionVotesLaunchedEffect
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialScaffold
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.uiDescription
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.components.text.Quote
import org.beatonma.commons.compose.systemui.statusBarsPadding
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.data.core.room.entities.division.CommonsDivision
import org.beatonma.commons.data.core.room.entities.division.CommonsDivisionData
import org.beatonma.commons.data.core.room.entities.division.CommonsDivisionVote
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.themed.titleMedium

internal val LocalDivisionActions: ProvidableCompositionLocal<DivisionActions> =
    compositionLocalOf { DivisionActions() }

@Composable
fun CommonsDivisionLayout(
    viewmodel: CommonsDivisionViewModel,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
) {
    val resultState by viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(resultState) { divisionData ->
        ProvideSocial(
            viewmodel,
            socialViewModel,
            userAccountViewModel,
        ) {
            CommonsDivisionLayout(divisionData)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonsDivisionLayout(
    division: CommonsDivision,
    onMemberClick: MemberVoteAction = LocalDivisionActions.current.onMemberClick,
) {
    val query = rememberText()
    val voteTypeFilters = remember { mutableStateOf(VoteType.values().toSet()) }
    var filteredVotes by remember { mutableStateOf(division.votes) }

    DivisionVotesLaunchedEffect(
        query.value,
        voteTypeFilters.value,
        votes = division.votes,
    ) {
        @Suppress("UNCHECKED_CAST")
        filteredVotes = it as List<CommonsDivisionVote>
    }

    val data = division.data
    val title = stringResource(R.string.division_title)
    val description = data.title

    val scrollState = rememberLazyListState()

    SocialScaffold(
        title = title,
        aboveSocial = null,
        scrollState = scrollState,
        content = { modifier ->
            item {
                CollapsingHeader(data, description, modifier)
            }

            DivisionVotes(
                scrollState,
                division,
                filteredVotes,
                query,
                voteTypeFilters,
                onMemberClick,
                modifier
            )
        }
    )
}

@Composable
private fun CollapsingHeader(
    division: CommonsDivisionData,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.statusBarsPadding(),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(division.house.uiDescription(), style = typography.titleMedium)
            Text(division.date.formatted())
        }

        Quote(description)
    }
}
