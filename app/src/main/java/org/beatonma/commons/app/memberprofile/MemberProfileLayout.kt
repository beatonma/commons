package org.beatonma.commons.app.memberprofile

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.beatonma.commons.R
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.social.*
import org.beatonma.commons.app.timeline.Timeline
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.app.ui.compose.components.LoadingIcon
import org.beatonma.commons.app.ui.compose.components.chips.EmailLink
import org.beatonma.commons.app.ui.compose.components.chips.PhoneLink
import org.beatonma.commons.app.ui.compose.components.chips.Weblink
import org.beatonma.commons.app.ui.compose.components.image.Avatar
import org.beatonma.commons.app.ui.compose.components.party.LocalPartyTheme
import org.beatonma.commons.app.ui.compose.components.party.PartyBackground
import org.beatonma.commons.app.ui.compose.components.party.partyWithTheme
import org.beatonma.commons.compose.ambient.WithContentAlpha
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.isCollapsed
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.components.Card
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.CollapsedColumn
import org.beatonma.commons.compose.components.HorizontalSeparator
import org.beatonma.commons.compose.components.text.OptionalText
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.components.text.ScreenTitle
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.NoConstituency
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.svg.ImageConfig
import org.beatonma.commons.svg.ScaleType
import org.beatonma.commons.theme.compose.Elevation
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.formatting.dateRange
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

private const val AVATAR_ASPECT_RATIO = 3F / 2F
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
            MemberProfileLayout(data, memberHistory, socialViewModel.uiState)
        }
    }
}

@Composable
fun MemberProfileLayout(
    completeMember: CompleteMember,
    history: MemberHistory,
    socialState: MutableState<SocialUiState>,
) {
    val profile = completeMember.profile
    val partyData = partyWithTheme(completeMember.party)
    val socialTheme = partyData.theme.asSocialTheme()

    CompositionLocalProvider(
        LocalPartyTheme provides partyData,
        LocalSocialTheme provides socialTheme,
    ) {
        StickySocialScaffold(
            state = socialState,
            aboveSocial = { headerExpansion, mod ->
                MemberProfileImage(completeMember, headerExpansion, mod)
            },
            social = { socialProgress, headerExpansion, social ->
                TitleBar(profile, headerExpansion, socialProgress, social)
            },
            lazyListContent = {
                MemberProfile(completeMember, history)
            },
            snapToStateAt = 0.5F
        )
    }
}

@Composable
private fun TitleBar(
    profile: MemberProfile,
    headerExpansion: HeaderExpansion,
    socialExpansion: SocialExpansion,
    social: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides LocalPartyTheme.current.onPrimary,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .shadow(1.dp)
                .zIndex(Layer.Low)
                .background(
                    socialExpansion.value
                        .reversed()
                        .lerpBetween(
                            LocalPartyTheme.current.primary,
                            Color.Transparent
                        )
                )
                .statusBarsPadding(
                    headerExpansion.value
                        .reversed()
                        .progressIn(.7F, 1F)
                )
                .testTag(TestTagMemberTitleBar)
        ) {
            MemberName(
                profile.name,
                Modifier
                    .wrapContentHeight(socialExpansion.value)
                    .padding(top = 8.dp)
                    .alpha(socialExpansion.value)
            )
            social()
        }
    }
}

@Composable
private fun MemberProfileImage(
    member: CompleteMember,
    visibility: HeaderExpansion,
    modifier: Modifier
) {
    val portraitUrl = member.profile.portraitUrl
    val imageModifier = Modifier
        .clipToBounds()
        .zIndex(Layer.Low)
        .fillMaxWidth()
        .wrapContentHeight(visibility.value)
        .aspectRatio(AVATAR_ASPECT_RATIO)

    Box(modifier) {
        if (portraitUrl != null) {
            Avatar(
                portraitUrl,
                imageModifier.background(LocalPartyTheme.current.primary)
            )
        } else {
            val scale = 0.5F + (0.33F * visibility.value.reversed().progressIn(0.5F, 1F))
            val imageConfig = remember(scale) {
                ImageConfig(ScaleType.Min, scaleMultiplier = scale)
            }

            PartyBackground(
                member.party,
                imageModifier,
                imageConfig,
                useCache = false,
            )
        }
    }
}

@Composable
private fun MemberName(name: String, modifier: Modifier) {
    ScreenTitle(name, modifier)
}

private fun LazyListScope.MemberProfile(completeMember: CompleteMember, history: MemberHistory) {
    val profile = completeMember.profile

    item {
        Weblinks(completeMember.weblinks)
    }

    item {
        CurrentPosition(profile, completeMember.party, completeMember.constituency)
    }

    item {
        MemberHistory(history)
    }

    item {
        ContactInfo(completeMember.addresses)
    }

    item {
        FinancialInterests(completeMember.financialInterests)
    }
}

@Composable
private fun Weblinks(weblinks: List<WebAddress>) {
    Links(
        Modifier.padding(Padding.ScreenHorizontal)
    ) {
        items(weblinks) { weblink ->
            Weblink(weblink, Modifier.padding(Padding.LinkItem))
        }
    }
}

@Composable
private fun CurrentPosition(
    profile: MemberProfile,
    party: Party?,
    constituency: Constituency,
    onConstituencyClick: ConstituencyAction = LocalMemberProfileActions.current.onConstituencyClick,
) {
    ProfileCard {
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
            .padding(Padding.VerticalListItemLarge),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ResourceText(
            resourceId,
            constituency.name,
            style = typography.h6,
            withAnnotatedStyle = true,
        )

        Icon(Icons.Default.KeyboardArrowRight, null)
    }
}

@Composable
private fun MemberHistory(
    history: MemberHistory
) {
    if (history.isEmpty()) {
        LoadingIcon()
        return
    }

    val state = rememberExpandCollapseState(ExpandCollapseState.Collapsed)
    val transition = updateTransition(targetState = state.value)
    val surfaceColor by animation.animateColor(transition) {
        if (state.isCollapsed) colors.surface else colors.background
    }
    val contentColor by animation.animateColor(transition) {
        if (state.isCollapsed) colors.onSurface else colors.onBackground
    }
    val elevation by animation.animateDp(transition) {
        if (state.isCollapsed) Elevation.Card else 0.dp
    }

    Card(
        Modifier.fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = surfaceColor,
        elevation = elevation,
        contentColor = contentColor,
    ) {
        CollapsedColumn(
            items = history,
            headerBlock = CollapsedColumn.simpleHeader(stringResource(R.string.member_history_title)),
            state = state,
        ) { memberHistory ->
            Timeline(
                memberHistory,
                Modifier.padding(Padding.VerticalListItemLarge),
                barModifier = Modifier.padding(vertical = 6.dp),
                surfaceColor = surfaceColor,
            )
        }
    }
}

@Composable
private fun ContactInfo(addresses: List<PhysicalAddress>) {
    val size = addresses.size
    ProfileCard {
        if (addresses.isEmpty()) {
            EmptyMessage(R.string.member_contact_info_none)
        } else {
            Column {
                val itemModifier = Modifier.padding(Padding.LinkItem)
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
private fun FinancialInterests(interests: List<FinancialInterest>) {
    ProfileCard {
        if (interests.isEmpty()) {
            EmptyMessage(R.string.member_financial_interests_none)
        } else {
            CollapsedColumn(
                items = interests,
                headerBlock = CollapsedColumn.simpleHeader(
                    stringResource(R.string.member_financial_interests),
                    autoPadding = false,
                ),
                scrollable = false,
            ) { interest ->
                Column(
                    Modifier.padding(Padding.VerticalListItem)
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
    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(Padding.Links)
            .then(modifier),
        content = content
    )
}

@Composable
private fun ProfileCard(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Card(modifier.fillMaxWidth(), shape = RectangleShape) {
        CardText(content = content)
    }
}
