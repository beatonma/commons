package org.beatonma.commons.app.featured

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.withHsl
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.MinimalBill
import org.beatonma.commons.data.core.room.entities.bill.ResolvedZeitgeistBill
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.ResolvedZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.ResolvedZeitgeistMember
import org.beatonma.commons.data.resolution.description
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.snommoc.models.ZeitgeistReason
import org.beatonma.commons.svg.ImageConfig
import org.beatonma.commons.svg.PathConfig
import org.beatonma.commons.svg.ScaleType
import org.beatonma.commons.svg.VectorGraphic
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsHeightPlus

private fun String?.reason(): ZeitgeistReason? = this?.let { ZeitgeistReason.valueOf(it) }
private fun ResolvedZeitgeistMember.reason() = this.zeitgeistMember.reason.reason()
private fun ResolvedZeitgeistDivision.reason() = this.zeitgeistDivision.reason.reason()
private fun ResolvedZeitgeistBill.reason() = this.zeitgeistBill.reason.reason()

internal val BackgroundPortraitConfig = ambientOf<ImageConfig> { error("No ImageConfig") }
internal val BadgePortraitConfig = ambientOf<ImageConfig> { error("No ImageConfig") }
internal val PartyImageCache =
    ambientOf<MutableMap<ParliamentID, VectorGraphic>> { error("No image cache") }

@Composable
fun ZeitgeistContent(
    zeitgeist: Zeitgeist,
    memberOnClick: (MemberProfile) -> Unit,
    divisionOnClick: (Division) -> Unit,
    billOnClick: (MinimalBill) -> Unit,
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

                provideImageConfigs {
                    LazyRowFor(zeitgeist.members.shuffled()) {
                        Member(it.member, memberOnClick, it.reason())
                    }
                    LazyColumnFor(zeitgeist.divisions.shuffled()) {
                        Division(it, divisionOnClick, it.reason())
                    }
                    LazyColumnFor(zeitgeist.bills.shuffled()) {
                        Bill(it, billOnClick, it.reason())
                    }
                }

                Spacer(modifier = Modifier.navigationBarsHeightPlus(160.dp))
            }
        }
    }
}

@Composable
private fun Division(
    item: ResolvedZeitgeistDivision,
    onClick: (Division) -> Unit,
    reason: ZeitgeistReason?,
    modifier: Modifier = Modifier,
) {
    val division = item.division
    ListItem(
        modifier.clickable(onClick = { onClick(division) }),
        overlineText = { Text(dotted(division.house.description(), division.date.formatted())) },
        trailing = { Text(division.passed.toString()) },
        text = { Text(division.title) },
    )
}

@Composable
private fun Bill(
    item: ResolvedZeitgeistBill,
    onClick: (MinimalBill) -> Unit,
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

@Composable
fun provideImageConfigs(content: @Composable () -> Unit) {
    val backgroundPortraitConfig = remember {
        ImageConfig(ScaleType.Max, Alignment.Center,
            scaleMultiplier = 1.5F,
            offset = Offset(0.5F, 0F),
            pathConfig = PathConfig {
                it.withHsl {
                    if (saturation > 0.1F) {
                        saturation = 1F
                    }
                    lightness = 0.5F
                }
            }
        )
    }
    val badgePortraitConfig = remember {
        ImageConfig(
            ScaleType.Min, Alignment.Center,
            scaleMultiplier = 1.0F,
            offset = Offset(0.5F, 0F),
        )
    }
    val partyImageCache: MutableMap<ParliamentID, VectorGraphic> = remember { mutableMapOf() }

    Providers(
        BackgroundPortraitConfig provides backgroundPortraitConfig,
        BadgePortraitConfig provides badgePortraitConfig,
        PartyImageCache provides partyImageCache,
        children = content
    )
}

@Composable
fun FlexyGrid() {
    TODO("Display member cards with/without portraits together. Group smaller views together.")
}
