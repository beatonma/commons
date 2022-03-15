package org.beatonma.commons.app.ui.screens.memberprofile

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.CollapsedTimeline
import org.beatonma.commons.app.ui.components.LoadingIcon
import org.beatonma.commons.app.ui.components.chips.EmailLink
import org.beatonma.commons.app.ui.components.chips.PhoneLink
import org.beatonma.commons.app.ui.components.chips.Weblink
import org.beatonma.commons.app.ui.components.image.Avatar
import org.beatonma.commons.app.ui.components.party.LocalPartyTheme
import org.beatonma.commons.app.ui.components.party.PartyBackground
import org.beatonma.commons.app.ui.components.party.partyWithTheme
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.LocalSocialTheme
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialScaffold
import org.beatonma.commons.app.ui.screens.social.SocialUiState
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.screens.social.asSocialTheme
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.Layer
import org.beatonma.commons.compose.ambient.WithContentAlpha
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateColor
import org.beatonma.commons.compose.animation.animateDp
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.components.Card
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.CollapsedColumn
import org.beatonma.commons.compose.components.HorizontalSeparator
import org.beatonma.commons.compose.components.text.OptionalText
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.components.text.ScreenTitle
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.NoConstituency
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.data.core.room.entities.member.WebAddress
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.AppIcon
import org.beatonma.commons.theme.CommonsPadding
import org.beatonma.commons.theme.formatting.dateRange
import org.beatonma.commons.themed.themedElevation

private const val AVATAR_ASPECT_RATIO = 1f
internal const val TestTagMemberTitleBar = "title_bar"

internal val LocalMemberProfileActions: ProvidableCompositionLocal<MemberProfileActions> =
    compositionLocalOf { MemberProfileActions() }

@Composable
fun MemberProfileLayout(
    viewmodel: MemberProfileViewModel,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
) {
    val memberData by viewmodel.getMemberData().collectAsState(IoLoading)

    ProvideSocial(
        viewmodel,
        socialViewModel,
        userAccountViewModel,
    ) {
        WithResultData(memberData) { data ->
            val memberHistory by viewmodel.getMemberHistory(data).collectAsState(listOf())
            MemberProfileLayout(
                data,
                memberHistory,
                socialViewModel.uiState.value,
            ) { socialViewModel.uiState.value = it }
        }
    }
}

@Composable
fun MemberProfileLayout(
    completeMember: CompleteMember,
    history: MemberHistory,
    socialState: SocialUiState,
    onSocialStateChange: (SocialUiState) -> Unit,
) {
    val profile = completeMember.profile
    val partyData = partyWithTheme(completeMember.party)
    val socialTheme = partyData.theme.asSocialTheme()

    CompositionLocalProvider(
        LocalPartyTheme provides partyData,
        LocalSocialTheme provides socialTheme,
    ) {
        SocialScaffold(
            title = { TitleBar(profile) },
            socialUiState = socialState,
            onStateChange = onSocialStateChange,
            aboveSocial = {
                MemberProfileImage(completeMember)
            },
            belowSocial = null,
        ) { mod: Modifier ->
            MemberProfile(completeMember, history, mod)
        }
    }
}

@Composable
private fun TitleBar(
    profile: MemberProfile,
) {
    CompositionLocalProvider(
        LocalContentColor provides LocalPartyTheme.current.onPrimary,
    ) {
        ScreenTitle(profile.name)
    }
}

@Composable
private fun MemberProfileImage(
    member: CompleteMember,
    modifier: Modifier = Modifier,
) {
    val portraitUrl = member.profile.portraitUrl
    val imageModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(AVATAR_ASPECT_RATIO)
        .zIndex(Layer.Low)

    Box(modifier) {
        if (portraitUrl != null) {
            Avatar(
                portraitUrl,
                imageModifier.background(LocalPartyTheme.current.primary)
            )
        } else {
//            val scale = 0.5F + (0.33F * scroll.reversed().progressIn(0.5F, 1F))
//            val imageConfig = remember(scale) {
//                ImageConfig(ScaleType.Min, scaleMultiplier = scale)
//            }

            PartyBackground(
                member.party,
                imageModifier,
//                imageConfig,
//                useCache = false,
            )
        }
    }
}


private fun LazyListScope.MemberProfile(
    completeMember: CompleteMember,
    history: MemberHistory,
    modifier: Modifier
) {
    val profile = completeMember.profile

    item {
        Weblinks(completeMember.weblinks, modifier)
    }

    item {
        CurrentPosition(profile, completeMember.party, completeMember.constituency, modifier)
    }

    item {
        MemberHistory(history, modifier)
    }

    item {
        ContactInfo(completeMember.addresses, modifier)
    }

    item {
        FinancialInterests(completeMember.financialInterests, modifier)
    }
}

@Composable
private fun Weblinks(weblinks: List<WebAddress>, modifier: Modifier) {
    Links(modifier) {
        itemsIndexed(weblinks) { i, weblink ->
            val itemModifier = Modifier
                .onlyWhen(i == 0) { padding(start = 4.dp) }
                .padding(CommonsPadding.LinkItem)

            Weblink(weblink, itemModifier)
        }
    }
}

@Composable
private fun CurrentPosition(
    profile: MemberProfile,
    party: Party?,
    constituency: Constituency,
    modifier: Modifier,
    onConstituencyClick: ConstituencyAction = LocalMemberProfileActions.current.onConstituencyClick,
) {
    ProfileCard(modifier) {
        Column {
            ResourceText(R.string.member_status, style = typography.overline)

            if (profile.active == true) {
                ActivePosition(
                    profile = profile,
                    constituency = constituency,
                    party = party,
                    onConstituencyClick = onConstituencyClick
                )
            } else {
                InactivePosition(
                    profile = profile,
                    constituency = constituency,
                    onConstituencyClick = onConstituencyClick
                )
            }
        }
    }
}

@Composable
private fun ActivePosition(
    profile: MemberProfile,
    constituency: Constituency,
    party: Party?,
    onConstituencyClick: ConstituencyAction,
) {
    OptionalText(
        profile.currentPost,
        maxLines = 3,
        style = typography.h6,
    )

    when {
        profile.isLord == true -> {
            ResourceText(R.string.member_house_of_lords)
        }
        constituency != NoConstituency -> {
            ConstituencyLink(true, constituency, onConstituencyClick)
        }
        else -> {
            OptionalText(party?.name dot constituency.name)
        }
    }
}

@Composable
private fun InactivePosition(
    profile: MemberProfile,
    constituency: Constituency,
    onConstituencyClick: ConstituencyAction,
) {
    ResourceText(R.string.member_inactive)

    if (profile.isLord == true) {
        ResourceText(R.string.member_former_house_of_lords)
    }
    if (constituency != NoConstituency) {
        ConstituencyLink(false, constituency, onConstituencyClick)
    }
}

@Composable
private fun ConstituencyLink(
    isActive: Boolean,
    constituency: Constituency,
    onConstituencyClick: ConstituencyAction,
) {
    val resourceId = when {
        isActive -> R.string.member_member_for_constituency
        else -> R.string.member_former_member_for_constituency
    }

    Row(
        modifier = Modifier
            .clickable(
                onClickLabel = stringResource(
                    R.string.content_description_view_constituency,
                    constituency.name
                ),
                onClick = { onConstituencyClick(constituency) }
            )
            .fillMaxWidth()
            .padding(CommonsPadding.VerticalListItemLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ResourceText(
            resourceId,
            constituency.name,
            style = typography.h6,
            withAnnotatedStyle = true,
        )

        Icon(AppIcon.ArrowRight, null)
    }
}

@Composable
private fun MemberHistory(
    history: MemberHistory,
    modifier: Modifier,
) {
    if (history.isEmpty()) {
        LoadingIcon()
        return
    }

    var state by rememberExpandCollapseState(ExpandCollapseState.Collapsed)
    val transition = updateTransition(targetState = state, label = "MemberHistoryTransition")

    val surfaceColor by animateColor(transition) { s ->
        if (s.isCollapsed) colors.surface else colors.background
    }
    val contentColor by animateColor(transition) { s ->
        if (s.isCollapsed) colors.onSurface else colors.onBackground
    }
    val elevation by animateDp(transition) { s ->
        if (s.isCollapsed) themedElevation.Card else 0.dp
    }

    Card(
        modifier.fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = surfaceColor,
        elevation = elevation,
        contentColor = contentColor,
    ) {
        CollapsedTimeline(
            stringResource(R.string.member_history_title),
            history,
            collapseState = state,
            onCollapseStateChange = { state = it },
            modifier = Modifier.padding(CommonsPadding.VerticalListItemLarge),
            barModifier = Modifier.padding(vertical = 6.dp),
            surfaceColor = surfaceColor,
            contentColor = contentColor,
        )
    }
}

@Composable
private fun ContactInfo(
    addresses: List<PhysicalAddress>,
    modifier: Modifier,
) {
    val size = addresses.size
    ProfileCard(modifier) {
        if (addresses.isEmpty()) {
            EmptyMessage(R.string.member_contact_info_none)
        } else {
            Column {
                val itemModifier = Modifier.padding(CommonsPadding.LinkItem)
                addresses.fastForEachIndexed { i, address ->
                    Text(address.description, style = typography.overline)

                    OptionalText(
                        if (address.address == "Constituency Office") {
                            null
                        } else {
                            address.address.replace(", ", "\n")
                        },
                        maxLines = Int.MAX_VALUE
                    )
                    OptionalText(address.postcode)

                    val phone = address.phone
                    val email = address.email
                    if (email != null || phone != null) {
                        Links {
                            if (phone != null) {
                                item {
                                    PhoneLink(phone, itemModifier)
                                }
                            }
                            if (email != null) {
                                item {
                                    EmailLink(email, itemModifier)
                                }
                            }
                        }
                    }

                    if (i < (size - 1)) {
                        HorizontalSeparator(modifier = Modifier.height(2.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FinancialInterests(
    interests: List<FinancialInterest>,
    modifier: Modifier,
) {
    ProfileCard(modifier) {
        if (interests.isEmpty()) {
            EmptyMessage(R.string.member_financial_interests_none)
        } else {
            CollapsedColumn(
                items = interests,
                headerBlock = CollapsedColumn.simpleHeader(
                    stringResource(R.string.member_financial_interests),
                    autoPadding = false,
                ),
                lazy = false,
            ) { interest ->
                Column(
                    Modifier.padding(CommonsPadding.VerticalListItem)
                ) {
                    Text(interest.category, style = typography.overline)
                    Text(interest.description)
                    WithContentAlpha(ContentAlpha.medium) {
                        OptionalText(
                            dateRange(interest.dateCreated, interest.dateAmended),
                            style = typography.body2,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyMessage(resource: Int) {
    ResourceText(resource, style = typography.h6)
}

@Composable
private fun Links(modifier: Modifier = Modifier, content: LazyListScope.() -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides colors.onSurface
    ) {
        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(CommonsPadding.Links)
                .then(modifier),
            content = content
        )
    }
}

@Composable
private fun ProfileCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Card(modifier.fillMaxWidth(), shape = RectangleShape) {
        CardText(content = content)
    }
}
