package org.beatonma.commons.app.frontpage

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.party.ProvidePartyImageConfig
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.ResolvedZeitgeistBill
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.ResolvedZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.ResolvedZeitgeistMember
import org.beatonma.commons.data.resolution.description
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.snommoc.models.ZeitgeistReason
import org.beatonma.commons.theme.compose.endOfContent
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding

private fun String?.reason(): ZeitgeistReason? = this?.let { ZeitgeistReason.valueOf(it) }
private fun ResolvedZeitgeistMember.reason() = this.zeitgeistMember.reason.reason()
private fun ResolvedZeitgeistDivision.reason() = this.zeitgeistDivision.reason.reason()
private fun ResolvedZeitgeistBill.reason() = this.zeitgeistBill.reason.reason()

val AmbientZeitgeistActions: ProvidableAmbient<ZeitgeistActions> =
    ambientOf { error("ZeitgeistActions have not been provided") }

internal typealias MemberAction = (MemberProfile) -> Unit
internal typealias DivisionAction = (Division) -> Unit
internal typealias BillAction = (Bill) -> Unit

class ZeitgeistActions(
    val onMemberClick: MemberAction,
    val onDivisionClick: DivisionAction,
    val onBillClick: BillAction,
)

@Composable
inline fun ZeitgeistContent(
    zeitgeist: Zeitgeist,
    actions: ZeitgeistActions = AmbientZeitgeistActions.current,
) {
    ZeitgeistContent(
        zeitgeist = zeitgeist,
        actions.onMemberClick,
        actions.onDivisionClick,
        actions.onBillClick,
    )
}

@Composable
fun ZeitgeistContent(
    zeitgeist: Zeitgeist,
    memberOnClick: MemberAction,
    divisionOnClick: DivisionAction,
    billOnClick: BillAction,
) {
    CommonsTheme {
        Surface(
            color = MaterialTheme.colors.background
        ) {
            ScrollableColumn {
                Text(
                    stringResource(R.string.zeitgeist_title),
                    Modifier.padding(16.dp),
                    style = MaterialTheme.typography.h1)


                ProvidePartyImageConfig {
                    ScrollableMembersLayout {
                        zeitgeist.members.fastForEach {
                            Member(it.member, memberOnClick, it.reason())
                        }
                    }
                }

                zeitgeist.divisions.fastForEach {
                    Division(it, onClick = divisionOnClick, reason = it.reason())
                }

                zeitgeist.bills.fastForEach {
                    Bill(it, onClick = billOnClick, reason = it.reason())
                }

                Text(BuildConfig.APPLICATION_ID, style = typography.caption)

                Spacer(modifier = Modifier
                    .navigationBarsPadding()
                    .endOfContent())
            }
        }
    }
}

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
        overlineText = { Text(dotted(division.house.description(), division.date.formatted())) },
        trailing = { Text(division.passed.toString()) },
        text = { Text(division.description ?: division.title) },
    )
}

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

