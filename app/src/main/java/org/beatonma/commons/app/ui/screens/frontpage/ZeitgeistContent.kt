package org.beatonma.commons.app.ui.screens.frontpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.members.MembersLayout
import org.beatonma.commons.app.ui.uiDescription
import org.beatonma.commons.app.util.findNavigationController
import org.beatonma.commons.app.util.logDebug
import org.beatonma.commons.app.util.navigateTo
import org.beatonma.commons.compose.padding.endOfContent
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ZeitgeistReason
import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill
import org.beatonma.commons.data.core.room.entities.division.ZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.ZeitgeistMember
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.theme.AppIcon
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.theme.house


internal typealias MemberAction = (MemberProfile) -> Unit
internal typealias DivisionAction = (ZeitgeistDivision) -> Unit
internal typealias BillAction = (ZeitgeistBill) -> Unit


@Composable
fun ZeitgeistContent(
    modifier: Modifier = Modifier,
    viewmodel: ZeitgeistViewModel = hiltViewModel(),
    navController: NavController = findNavigationController(),
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val result by viewmodel.livedata.observeAsState(IoLoading)
    var zeitgeist by remember { mutableStateOf(Zeitgeist()) }

    LaunchedEffect(result) {
        result.onSuccess { zeitgeist = it }
    }

    ZeitgeistContent(
        zeitgeist,
        lazyListState,
        navController::navigateTo,
        navController::navigateTo,
        navController::navigateTo,
        modifier,
    )
}

@Composable
private fun ZeitgeistContent(
    zeitgeist: Zeitgeist,
    lazyListState: LazyListState,
    memberOnClick: MemberAction,
    divisionOnClick: DivisionAction,
    billOnClick: BillAction,
    modifier: Modifier,
) {
    Surface(
        modifier,
        color = colors.background
    ) {
        LazyColumn(state = lazyListState) {
            item {
                Spacer(Modifier.height(72.dp))
            }

            item {
                MainTitle()
            }

            item {
                ZeitgeistMembers(zeitgeist.members, onClick = memberOnClick)
            }

            items(zeitgeist.divisions) {
                ZeitgeistDivision(it, onClick = divisionOnClick, reason = it.reason)
            }

            items(zeitgeist.bills) {
                ZeitgeistBill(it, onClick = billOnClick, reason = it.reason)
            }

            item {
                Text(BuildConfig.APPLICATION_ID, style = typography.caption)
            }

            endOfContent()
        }
    }
}

@Composable
private fun MainTitle() {
    Text(
        stringResource(R.string.zeitgeist_title),
        Modifier.padding(16.dp),
        style = typography.h1
    )
}

@Composable
private fun ZeitgeistMembers(
    members: List<ZeitgeistMember>,
    onClick: MemberAction,
) {
    val profiles by remember(members) {
        mutableStateOf(members.map { it.member.profile })
    }

    MembersLayout(
        profiles,
        Modifier
            .fillMaxWidth()
            .heightIn(min = 330.dp),
        onClick = onClick,
        decoration = { profile ->
            val reason = remember {
                members.find {
                    it.member.profile.parliamentdotuk == profile.parliamentdotuk
                }?.reason
            }
            if (reason != null) {
                ReasonIcon(reason)
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ZeitgeistDivision(
    division: ZeitgeistDivision,
    onClick: DivisionAction,
    reason: ZeitgeistReason,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier.clickable(onClick = { onClick(division) }),
        icon = {
            Icon(AppIcon.Division,
                null,
                tint = if (division.house == House.lords) colors.house.Lords else colors.house.Commons)
        },
        overlineText = { Text(division.house.uiDescription() dot division.date.formatted()) },
        text = { Text(division.title) },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ZeitgeistBill(
    bill: ZeitgeistBill,
    onClick: BillAction,
    reason: ZeitgeistReason,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier.clickable(onClick = { onClick(bill) }),
        icon = { Icon(AppIcon.Bill, null) },
        overlineText = { Text(bill.lastUpdate.formatted()) },
        text = { Text(bill.title) },
    )
}

@Composable
private fun ReasonIcon(reason: ZeitgeistReason, modifier: Modifier = Modifier) {
    when (reason) {
        ZeitgeistReason.feature -> FeaturedReasonIcon(modifier)
        ZeitgeistReason.social -> TrendingReasonIcon(modifier)
        ZeitgeistReason.unspecified -> {
            logDebug("ZeitgeistReason is unspecified")
        }
    }
}

@Composable
private fun FeaturedReasonIcon(modifier: Modifier = Modifier) {
    Icon(
        AppIcon.Featured,
        contentDescription = stringResource(id = R.string.content_description_featured),
        modifier = modifier,
    )
}

@Composable
private fun TrendingReasonIcon(modifier: Modifier = Modifier) {
    Icon(
        AppIcon.Trending,
        contentDescription = stringResource(id = R.string.content_description_featured),
        modifier = modifier,
    )
}
