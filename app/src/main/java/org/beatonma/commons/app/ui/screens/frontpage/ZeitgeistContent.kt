package org.beatonma.commons.app.ui.screens.frontpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.party.ProvidePartyImageConfig
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.text.OptionalText
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.ResolvedZeitgeistBill
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.ResolvedZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.ResolvedZeitgeistMember
import org.beatonma.commons.data.resolution.uiDescription
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.snommoc.models.ZeitgeistReason
import org.beatonma.commons.theme.compose.formatting.formatted
import org.beatonma.commons.theme.compose.padding.endOfContent
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding

private fun String?.reason(): ZeitgeistReason? = this?.let { ZeitgeistReason.valueOf(it) }
private fun ResolvedZeitgeistMember.reason() = this.zeitgeistMember.reason.reason()
private fun ResolvedZeitgeistDivision.reason() = this.zeitgeistDivision.reason.reason()
private fun ResolvedZeitgeistBill.reason() = this.zeitgeistBill.reason.reason()

val LocalZeitgeistActions: ProvidableCompositionLocal<ZeitgeistActions> =
    compositionLocalOf { error("ZeitgeistActions have not been provided") }

internal typealias MemberAction = (MemberProfile) -> Unit
internal typealias DivisionAction = (Division) -> Unit
internal typealias BillAction = (Bill) -> Unit

class ZeitgeistActions(
    val onMemberClick: MemberAction,
    val onDivisionClick: DivisionAction,
    val onBillClick: BillAction,
)

@Composable
fun ZeitgeistContent(
    zeitgeist: Zeitgeist,
    modifier: Modifier = Modifier,
    actions: ZeitgeistActions = LocalZeitgeistActions.current,
) {
    ZeitgeistContent(
        zeitgeist = zeitgeist,
        actions.onMemberClick,
        actions.onDivisionClick,
        actions.onBillClick,
        modifier,
    )
}

@Composable
private fun ZeitgeistContent(
    zeitgeist: Zeitgeist,
    memberOnClick: MemberAction,
    divisionOnClick: DivisionAction,
    billOnClick: BillAction,
    modifier: Modifier,
) {
    Surface(
        modifier,
        color = MaterialTheme.colors.background
    ) {
        LazyColumn {
            item {
                Text(
                    stringResource(R.string.zeitgeist_title),
                    Modifier.padding(16.dp),
                    style = MaterialTheme.typography.h1
                )
            }

            item {
                ProvidePartyImageConfig {
                    MembersLayout {
                        zeitgeist.members.fastForEach {
                            Member(it.member, memberOnClick, it.reason())
                        }
                    }
                }
            }

            items(zeitgeist.divisions) {
                Division(it, onClick = divisionOnClick, reason = it.reason())
            }

            items(zeitgeist.bills) {
                Bill(it, onClick = billOnClick, reason = it.reason())
            }

            item {
                Text(BuildConfig.APPLICATION_ID, style = typography.caption)
            }

            item {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .endOfContent()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Division(
    item: ResolvedZeitgeistDivision,
    onClick: DivisionAction,
    reason: ZeitgeistReason?,
    modifier: Modifier = Modifier,
) {
    val division = item.division
    ListItem(
        modifier.clickable(onClick = { onClick(division) }),
        overlineText = { Text(division.house.uiDescription() dot division.date.formatted()) },
        trailing = { Text(division.passed.toString()) },
        text = { Text(division.description ?: division.title) },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Bill(
    item: ResolvedZeitgeistBill,
    onClick: BillAction,
    reason: ZeitgeistReason?,
    modifier: Modifier = Modifier,
) {
    val bill = item.bill
    ListItem(
        modifier.clickable(onClick = { onClick(bill) }),
        overlineText = { Text(bill.date.formatted()) },
        secondaryText = { OptionalText(bill.description) },
        text = { Text(bill.title) },
    )
}
