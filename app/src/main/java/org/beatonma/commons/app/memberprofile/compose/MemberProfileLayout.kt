package org.beatonma.commons.app.memberprofile.compose

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.AmbientContentColor
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstrainedLayoutReference
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.social.State
import org.beatonma.commons.app.social.compose.AmbientSocialTheme
import org.beatonma.commons.app.social.compose.SocialScaffold
import org.beatonma.commons.app.social.compose.asSocialTheme
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.app.ui.compose.components.Avatar
import org.beatonma.commons.app.ui.compose.components.chips.EmailLink
import org.beatonma.commons.app.ui.compose.components.chips.PhoneLink
import org.beatonma.commons.app.ui.compose.components.chips.Weblink
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.progress
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.components.Card
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.HorizontalSeparator
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillHeight
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.lerp
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.compose.util.withAnnotatedStyle
import org.beatonma.commons.core.extensions.lerp
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reverse
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.data.core.room.entities.member.WebAddress
import org.beatonma.commons.kotlin.extensions.dateRange
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.endOfContent
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding

private const val AVATAR_ASPECT_RATIO = 3F / 2F
private const val Z_LOW = 16F
private const val Z_MEDIUM = 32F
private const val Z_HIGH = 64F
private val defaultParty get() = Party(0, "Unknown party")

private val PartyAmbient: ProvidableAmbient<PartyData> = ambientOf {
    error("Party not set")
}

private data class PartyData(val party: Party, val theme: ComposePartyColors)

@Composable
fun MemberProfileLayout(
    completeMember: CompleteMember,
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
) {
    val profile = completeMember.profile ?: return
    val partyData = (completeMember.party ?: defaultParty).let { PartyData(it, it.theme()) }
    val socialTheme = partyData.theme.asSocialTheme()

    Providers(
        PartyAmbient provides partyData,
        AmbientSocialTheme provides socialTheme,
    ) {
        MemberProfileScaffold(
            profile.name,
            modifier, state,

            contentBefore = {
                Avatar(
                    profile.portraitUrl,
                    Modifier.fillMaxWidth()
                        .aspectRatio(AVATAR_ASPECT_RATIO)
                )
            },
            contentAfter = {
                MemberProfile(completeMember, Modifier.navigationBarsPadding().endOfContent())
            },
        )
    }
}

@Composable
private fun MemberProfileScaffold(
    name: String,
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    scrollState: ScrollState = rememberScrollState(),
    contentBefore: @Composable (Modifier) -> Unit,
    contentAfter: @Composable (Modifier) -> Unit,
) {
    val scrollPosition = remember { mutableStateOf(0F) }

    SocialScaffold(
        modifier.fillMaxSize()
            .onlyWhen(condition = state.value == State.COLLAPSED) {
                verticalScroll(scrollState)
            },
        state = state,
        expandAction = {
            scrollPosition.value = scrollState.value
            scrollState.smoothScrollTo(0F, tween(easing = FastOutLinearInEasing)) { _, _ ->
                state.update(State.EXPANDED)
            }
        }

    ) { transitionState: TransitionState, social: ConstrainedLayoutReference ->

        val (
            before,
            title,
            titlebar,
            after,
        ) = createRefs()
        val progress = transitionState.progress
        val reverseProgress = progress.reverse()

        Box(
            Modifier
                .wrapContentHeight(reverseProgress)
                .constrainAs(before) {
                    top.linkTo(parent.top)
                }
                .drawOpacity(reverseProgress.progressIn(0F, 0.6F))
                .zIndex(0F)
        ) {
            if (progress != 1F) {
                contentBefore(Modifier)
            }
        }

        Box(
            Modifier
                .constrainAs(after) {
                    top.linkTo(social.bottom)
                }
                .drawOpacity(reverseProgress.progressIn(0F, 0.6F))
                .zIndex(0F)
        ) {
            if (progress != 1F) {
                contentAfter(Modifier)
            }
        }

        Box(
            Modifier.constrainAs(titlebar) {
                top.linkTo(title.top)
                bottom.linkTo(social.bottom)
                centerHorizontallyTo(parent)
                height = Dimension.fillToConstraints
            }
                .fillMaxWidth()
                .background(PartyAmbient.current.theme.primary.lerp(
                    colors.background,
                    progress.progressIn(0.5F, .8F))
                )
                .zIndex(Z_LOW)
        )

        Box(
            Modifier.constrainAs(title) {
                top.linkTo(before.bottom)
            }.zIndex(Z_MEDIUM)
                .drawOpacity(reverseProgress)
                .padding(top = 8.dp)
        ) {
            Providers(AmbientContentColor provides PartyAmbient.current.theme.onPrimary) {
                Text(name,
                    style = typography.h4,
                    modifier = Modifier.padding(Padding.ScreenHorizontal))
            }
        }

        // Constraints for the social content
        Modifier
            .constrainAs(social) {
                start.linkTo(parent.start)
                top.linkTo(title.bottom)
            }
            .zIndex(Z_HIGH)
            .wrapContentOrFillHeight(progress.withEasing(FastOutSlowInEasing))
            .padding(start = 12F.lerp(0F, progress).dp, bottom = 8.dp)
    }
}

@Composable
private fun MemberProfile(completeMember: CompleteMember, modifier: Modifier) {
    val profile = completeMember.profile ?: return

    val viewmodel = ViewmodelAmbient.current
    val scope = rememberCoroutineScope()
    val history = remember { mutableStateOf<List<Temporal>>(listOf()) }

    scope.launch {
        history.value = viewmodel.constructHistoryOf(completeMember)
    }

    Surface(color = colors.background) {
        Column(modifier) {
            withNotNull(completeMember.weblinks) { Weblinks(it) }

            CurrentPosition(profile, completeMember.party, completeMember.constituency)

            withNotNull(completeMember.addresses) { ContactInfo(it) }
            withNotNull(completeMember.financialInterests) { FinancialInterests(it) }

            // TODO reimplement TimelineView as composable
//        AndroidView(viewBlock = { context ->
//            TimelineView(context)
//        }, modifier = Modifier.background(MaterialLightGreen200), update = {
//            println("history update")
//            it.setHistory(history.value)
//        })
        }
    }
}

@Composable
private fun Weblinks(weblinks: List<WebAddress>) {
    Links {
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
                        style = typography.h6
                    )
                }
                else {
                    Text(stringResource(R.string.member_inactive))
                    if (constituency != null) {
                        Text(stringResource(R.string.member_former_member_for_constituency,
                            constituency.name).withAnnotatedStyle())
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
fun CollapsibleColumn(state: MutableState<ExpandCollapseState> = rememberExpandCollapseState()) {
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
private inline fun Links(crossinline content: @Composable () -> Unit) {
    ScrollableRow(Modifier.fillMaxWidth().padding(Padding.Links)) {
        content()
    }
}
