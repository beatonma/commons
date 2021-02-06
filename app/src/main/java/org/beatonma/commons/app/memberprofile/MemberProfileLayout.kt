package org.beatonma.commons.app.memberprofile

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstrainedLayoutReference
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.beatonma.commons.R
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.social.AmbientSocialTheme
import org.beatonma.commons.app.social.AmbientSocialUiState
import org.beatonma.commons.app.social.AmbientSocialUiTransition
import org.beatonma.commons.app.social.CollapseExpandProgress
import org.beatonma.commons.app.social.ProvideSocial
import org.beatonma.commons.app.social.SocialScaffold
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.asSocialTheme
import org.beatonma.commons.app.social.rememberSocialUiState
import org.beatonma.commons.app.social.socialTransitionState
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.app.ui.compose.components.chips.EmailLink
import org.beatonma.commons.app.ui.compose.components.chips.PhoneLink
import org.beatonma.commons.app.ui.compose.components.chips.Weblink
import org.beatonma.commons.app.ui.compose.components.image.Avatar
import org.beatonma.commons.app.ui.compose.components.party.AmbientPartyTheme
import org.beatonma.commons.app.ui.compose.components.party.PartyBackground
import org.beatonma.commons.app.ui.compose.components.party.partyWithTheme
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.Card
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.HorizontalSeparator
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.withNotNull
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillHeight
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.lerp
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.compose.util.withAnnotatedStyle
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.core.extensions.lerpTo
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.data.core.room.entities.member.WebAddress
import org.beatonma.commons.kotlin.extensions.dateRange
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.svg.ImageConfig
import org.beatonma.commons.svg.ScaleType
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.endOfContent
import org.beatonma.commons.theme.compose.pdp
import org.beatonma.commons.theme.compose.theme.screenTitle
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding

private const val AVATAR_ASPECT_RATIO = 3F / 2F

internal val AmbientMemberProfileActions: ProvidableAmbient<MemberProfileActions> =
    ambientOf { MemberProfileActions() }

@Composable
fun MemberProfileLayout(
    viewmodel: ComposeMemberProfileViewModel,
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
            MemberProfileLayout(data)
        }
    }
}

@Composable
fun MemberProfileLayout(
    completeMember: CompleteMember,
    modifier: Modifier = Modifier,
    state: MutableState<SocialUiState> = AmbientSocialUiState.current,
) {
    val profile = completeMember.profile
    val partyData = partyWithTheme(completeMember.party)
    val socialTheme = partyData.theme.asSocialTheme()

    Providers(
        AmbientPartyTheme provides partyData,
        AmbientSocialTheme provides socialTheme,
    ) {
        MemberProfileScaffold(
            profile.name,
            modifier, state,

            contentBefore = {
                MemberProfileImage(completeMember)
            },
            contentAfter = {
                MemberProfile(completeMember,
                    Modifier
                        .navigationBarsPadding()
                        .endOfContent())
            },
        )
    }
}

@Composable
fun MemberProfileImage(member: CompleteMember) {
    val portraitUrl = member.profile.portraitUrl
    val imageModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(AVATAR_ASPECT_RATIO)

    if (portraitUrl != null) {
        Avatar(
            portraitUrl,
            imageModifier
        )
    }
    else {
        PartyBackground(
            member.party,
            imageModifier,
            ImageConfig(
                ScaleType.Min,
                scaleMultiplier = 0.5F
            ),
            useCache = false,
        )
    }
}

@Composable
private fun MemberProfile(completeMember: CompleteMember, modifier: Modifier) {
    val profile = completeMember.profile

    Surface(color = colors.background) {
        Column(modifier) {
            Weblinks(completeMember.weblinks)

            CurrentPosition(profile, completeMember.party, completeMember.constituency)

            ContactInfo(completeMember.addresses)
            FinancialInterests(completeMember.financialInterests)
        }
    }
}

@Composable
private fun Weblinks(weblinks: List<WebAddress>) {
    Links(Modifier.padding(Padding.ScreenHorizontal)) {
        weblinks.fastForEach { weblink ->
            Weblink(
                weblink,
                Modifier.padding(Padding.LinkItem)
            )
        }
    }
}

@Composable
private fun CurrentPosition(
    profile: MemberProfile,
    party: Party?,
    constituency: Constituency?,
    onConstituencyClick: ConstituencyAction = AmbientMemberProfileActions.current.onConstituencyClick,
    modifier: Modifier = Modifier,
) {
    Card(modifier.fillMaxWidth(), shape = RectangleShape) {
        CardText {
            Column {
                if (profile.active == true) {
                    OptionalText(profile.currentPost, style = typography.h6)

                    val status = if (profile.isMp == true && constituency != null) {
                        stringResource(R.string.member_member_for_constituency,
                            constituency.name)
                    }
                    else if (profile.isLord == true) {
                        stringResource(R.string.member_house_of_lords)
                    }
                    else {
                        dotted(party?.name, constituency?.name)
                    }

                    OptionalText(
                        status.withAnnotatedStyle(),
                        style = typography.h6,
                        maxLines = 3,
                        modifier = Modifier.withNotNull(constituency) {
                            clickable { onConstituencyClick(it) }
                        }
                    )
                }
                else {
                    Text(stringResource(R.string.member_inactive))
                    if (constituency != null) {
                        Text(
                            stringResource(
                                R.string.member_former_member_for_constituency,
                                constituency.name
                            ).withAnnotatedStyle(),
                            modifier = Modifier.clickable { onConstituencyClick(constituency) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactInfo(
    addresses: List<PhysicalAddress>,
    modifier: Modifier = Modifier,
) {
    val size = addresses.size
    Card(modifier.fillMaxWidth(), shape = RectangleShape) {
        CardText {
            if (addresses.isEmpty()) {
                EmptyMessage(R.string.member_contact_info_none)
            }
            else {
                Column {
                    val itemModifier = Modifier.padding(Padding.LinkItem)
                    addresses.fastForEachIndexed { i, address ->
                        Text(address.description, style = typography.overline)
                        OptionalText(
                            if (address.address == "Constituency Office") {
                                null
                            }
                            else {
                                address.address.replace(", ", "\n")
                            },
                            maxLines = Int.MAX_VALUE
                        )
                        OptionalText(address.postcode)

                        val phone = address.phone
                        val email = address.email
                        if (email != null || phone != null) {
                            Links {
                                if (phone != null) PhoneLink(phone, itemModifier)
                                if (email != null) EmailLink(email, itemModifier)
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
}

@Composable
private fun FinancialInterests(interests: List<FinancialInterest>, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxWidth(), shape = RectangleShape) {
        CardText {
            if (interests.isEmpty()) {
                EmptyMessage(R.string.member_financial_interests_none)
            }
            else {
                Column {
                    interests.fastForEach { interest ->
                        Text(interest.description)
                        Text(interest.category)
                        OptionalText(dateRange(interest.dateCreated, interest.dateAmended))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyMessage(resource: Int) {
    EmptyMessage(stringResource(resource))
}

@Composable
private fun EmptyMessage(text: String) {
    Text(text, style = typography.h6)
}

@Composable
private fun Links(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    ScrollableRow(modifier
        .fillMaxWidth()
        .padding(Padding.Links)) {
        content()
    }
}

@Composable
private fun MemberProfileScaffold(
    name: String,
    modifier: Modifier = Modifier,
    state: MutableState<SocialUiState> = rememberSocialUiState(),
    scrollState: ScrollState = rememberScrollState(),
    contentBefore: @Composable (Modifier) -> Unit,
    contentAfter: @Composable (Modifier) -> Unit,
) {
    val scrollPosition = remember { mutableStateOf(0F) }
    val transState = socialTransitionState(toState = state.value)

    Providers(
        AmbientSocialUiTransition provides transState,
    ) {
        SocialScaffold(
            modifier.onlyWhen(transState[CollapseExpandProgress] == 0F) {
                verticalScroll(scrollState)
            },
            uiState = state,
            transitionState = transState,
            expandAction = {
                scrollPosition.value = scrollState.value
                scrollState.smoothScrollTo(0F, tween(easing = FastOutLinearInEasing)) { _, _ ->
                    state.update(SocialUiState.Expanded)
                }
            }

        ) { transitionState: TransitionState, social: ConstrainedLayoutReference ->

            val (
                before,
                title,
                titlebar,
                after,
            ) = createRefs()
            val progress = transitionState[CollapseExpandProgress]
            val reverseProgress = progress.reversed()

            if (progress != 1F) {
                Box(
                    Modifier
                        .constrainAs(before) {
                            top.linkTo(parent.top)
                        }
                        .wrapContentHeight(reverseProgress)
                        .alpha(reverseProgress.progressIn(0F, 0.6F))
                        .zIndex(Layer.Bottom)
                ) {
                    contentBefore(Modifier)
                }
            }

            if (progress != 1F) {
                Box(
                    Modifier
                        .constrainAs(after) {
                            top.linkTo(social.bottom)
                        }
                        .alpha(reverseProgress.progressIn(0F, 0.6F))
                        .zIndex(Layer.Bottom)
                ) {
                    contentAfter(Modifier)
                }
            }

            Box(
                Modifier
                    .constrainAs(titlebar) {
                        top.linkTo(title.top)
                        bottom.linkTo(social.bottom)
                        centerHorizontallyTo(parent)
                        height = Dimension.fillToConstraints
                    }
                    .fillMaxWidth()
                    .background(AmbientPartyTheme.current.theme.primary.lerp(
                        colors.background,
                        progress.progressIn(0.5F, .8F))
                    )
                    .zIndex(Layer.Low)
                    .shadow(1.dp)
                    .semantics { testTag = "title_bar" }
            )

            Box(
                Modifier
                    .constrainAs(title) {
                        top.linkTo(before.bottom)
                    }
                    .zIndex(Layer.Middle)
                    .alpha(reverseProgress.progressIn(.85F, 1F))
                    .wrapContentHeight(reverseProgress)
                    .padding(top = 8.dp)
            ) {
                Providers(AmbientContentColor provides AmbientPartyTheme.current.theme.onPrimary) {
                    Text(name,
                        style = typography.screenTitle,
                        modifier = Modifier.padding(Padding.ScreenHorizontal))
                }
            }

            // Constraints for the social content
            Modifier
                .constrainAs(social) {
                    start.linkTo(parent.start)
                    top.linkTo(title.bottom)
                }
                .zIndex(Layer.High)
                .wrapContentOrFillHeight(progress.withEasing(FastOutSlowInEasing))
                .padding(start = 12.lerpTo(0, progress).pdp, bottom = 8.lerpTo(0, progress).pdp)
                .semantics { testTag = "social_content" }
        }
    }
}
