package org.beatonma.commons.app.signin

import androidx.compose.animation.transition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.BottomSheetText
import org.beatonma.commons.app.ui.compose.components.CommonsOutlinedButton
import org.beatonma.commons.app.ui.compose.components.FabBottomSheet
import org.beatonma.commons.app.ui.compose.components.FabText
import org.beatonma.commons.app.ui.compose.components.LoadingIcon
import org.beatonma.commons.app.ui.compose.components.WarningButton
import org.beatonma.commons.app.ui.compose.components.image.Avatar
import org.beatonma.commons.app.ui.compose.components.rememberFabBottomSheetState
import org.beatonma.commons.app.ui.compose.components.rememberFabBottomSheetTransition
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.animation.twoStateProgressTransition
import org.beatonma.commons.compose.components.ConfirmationState
import org.beatonma.commons.compose.components.DoubleConfirmationButton
import org.beatonma.commons.compose.components.LinkedText
import org.beatonma.commons.compose.components.ResourceText
import org.beatonma.commons.compose.components.doubleConfirmationColors
import org.beatonma.commons.compose.components.rememberConfirmationState
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.theme.CommonsButtons
import org.beatonma.commons.theme.compose.theme.onWarningSurface
import org.beatonma.commons.theme.compose.theme.warningSurface

/**
 * Chooses the appropriate user UI to display depending on whether they are
 * already signed in.
 */
@Composable
fun ContextualUserAccountFabUi(userToken: UserToken = AmbientUserToken.current) =
    when (userToken) {
        NullUserToken -> SignInFabUi()
        else -> UserProfileFabUi()
    }

@Composable
fun SignInFabUi(actions: UserAccountActions = AmbientUserProfileActions.current.userAccountActions) =
    FabBottomSheet(
        fabContent = { progress ->
            FabText(stringResource(R.string.account_sign_in), progress)
        },
        bottomSheetContent = { progress ->
            BottomSheetText(progress) {
                Column(Modifier.fillMaxWidth()) {
                    val linkedTextSize = progress.progressIn(0F, 0.1F)
                    LinkedText(
                        stringResource(R.string.account_sign_in_rationale),
                        clickable = progress == 1F,
                        modifier = Modifier.wrapContentSize(linkedTextSize)
                    )

                    AndroidView(
                        viewBlock = { context ->
                            SignInButton(context).apply {
                                setOnClickListener { actions.signIn() }
                                setSize(SignInButton.SIZE_STANDARD)
                            }
                        },
                        Modifier.align(Alignment.End),
                    )
                }
            }
        },
    )

@Composable
fun UserProfileFabUi(userToken: UserToken = AmbientUserToken.current) {
    val fabUiState = rememberFabBottomSheetState()
    val transition = rememberFabBottomSheetTransition()
    val transitionState = transition(transition, toState = fabUiState.value)

    val profileState = remember { mutableStateOf(ProfileState.OVERVIEW) }
    val profileTransition =
        remember { twoStateProgressTransition(ProfileState.OVERVIEW, ProfileState.DELETE_ACCOUNT) }
    val profileTransitionState = transition(profileTransition, toState = profileState.value)

    val fabProgress = transitionState[progressKey]
    val profileProgress = profileTransitionState[progressKey]

    FabBottomSheet(
        uiState = fabUiState,
        transition = transition,
        transitionState = transitionState,
        surfaceColor = getSurfaceColor(fabProgress, profileProgress),
        contentColor = getContentColor(fabProgress, profileProgress),
        onDismiss = { profileState.update(ProfileState.OVERVIEW) },
        fabContent = { progress ->
            FabText(userToken.username, progress)
        },
        bottomSheetContent = { progress ->
            if (profileProgress != 1F) {
                ProfileSheetContent(userToken,
                    progress - profileProgress,
                    profileState = profileState
                )
            }

            if (profileProgress > 0F) {
                DeleteAccountUi(userToken, progress = profileProgress, profileState = profileState)
            }
        },
    )
}

@Composable
private fun ProfileSheetContent(
    userToken: UserToken,
    progress: Float,
    profileState: MutableState<ProfileState>,
    userProfileActions: UserProfileActions = AmbientUserProfileActions.current,
    focusRequester: FocusRequester = remember(::FocusRequester),
) {
    val usernameState = remember { mutableStateOf(EditableState.ReadOnly) }
    val usernameTransitionDef = rememberEditableStateTransition()
    val usernameTransition = transition(usernameTransitionDef, usernameState.value)

    val nonEditableVisibility = usernameTransition[readOnlyVisibility]

    BottomSheetText(progress) {
        Column(Modifier.wrapContentHeight().fillMaxWidth()) {
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                Avatar(
                    userToken.photoUrl,
                    Modifier
                        .wrapContentWidth(nonEditableVisibility)
                        .alpha(nonEditableVisibility)
                        .preferredWidth(96.dp)
                        .padding(end = 16.dp)
                        .aspectRatio(1F)
                        .clip(shapes.small)
                )

                Column {
                    EditableUsername(userToken,
                        state = usernameState,
                        focusRequester = focusRequester)

                    if (usernameState.isReadOnly) {
                        Text(
                            dotted(userToken.name, userToken.email),
                            style = typography.caption,
                            modifier = Modifier.padding(Padding.VerticalListItem)
                        )
                    }
                }
            }

            Spacer(Modifier.height(nonEditableVisibility.reversed().lerpBetween(0, 16).dp))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WarningButton(
                    onClick = { profileState.update(ProfileState.DELETE_ACCOUNT) },
                    modifier = Modifier
                        .wrapContentHeight(nonEditableVisibility)
                        .alpha(nonEditableVisibility)
                        .padding(Padding.CardButton),
                ) {
                    ResourceText(R.string.account_delete_account_button)
                }

                CommonsOutlinedButton(
                    onClick = userProfileActions.userAccountActions.signOut,
                    modifier = Modifier
                        .wrapContentHeight(nonEditableVisibility)
                        .alpha(nonEditableVisibility)
                        .padding(Padding.CardButton),
                ) {
                    ResourceText(R.string.account_sign_out)
                }
            }
        }
    }
}

@Composable
private fun DeleteAccountUi(
    userToken: UserToken,
    progress: Float,
    profileState: MutableState<ProfileState>,
    confirmDeleteAction: suspend (UserToken) -> Unit = AmbientUserProfileActions.current.userAccountActions.deleteAccount,
) {
    val confirmationState = rememberConfirmationState()

    BottomSheetText(progress) {
        Column(Modifier.fillMaxWidth().alpha(progress.progressIn(0.6F, 1F))) {
            ResourceText(R.string.account_delete_account_title, style = typography.h4)

            ResourceText(R.string.account_delete_explanation)

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth().padding(Padding.EndOfContent),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DoubleConfirmationButton(
                    onClick = { println("(FAKE) Account deletion confirmed!") },
                    colors = doubleConfirmationColors(
                        safeColors = CommonsButtons.contentButtonColors(),
                        awaitingConfirmationColors = CommonsButtons.buttonColors(
                            contentColor = colors.warningSurface,
                            backgroundColor = colors.onWarningSurface),
                        confirmedColor = CommonsButtons.contentButtonColors()
                    ),
                    confirmationState = confirmationState,
                    border = if (confirmationState.value == ConfirmationState.Confirmed) null else ButtonDefaults.outlinedBorder,
                    safeContent = { ResourceText(R.string.account_delete_confirm_button) },
                    awaitingConfirmationContent = { ResourceText(R.string.account_delete_confirm_twice_button) },
                    confirmedContent = { LoadingIcon() }
                )

                CommonsOutlinedButton(
                    onClick = { profileState.update(ProfileState.OVERVIEW) },
                    colors = CommonsButtons.contentButtonColors()
                ) {
                    ResourceText(R.string.account_delete_cancel_button)
                }
            }
        }
    }
}

private val MutableState<EditableState>.isReadOnly get() = value == EditableState.ReadOnly

private enum class ProfileState {
    OVERVIEW,
    DELETE_ACCOUNT,
}

@Composable
private fun getSurfaceColor(fabProgress: Float, profileProgress: Float): Color {
    val startColor = if (fabProgress == 1F) colors.surface else colors.primary
    val endColor = if (profileProgress == 0F) colors.surface else colors.warningSurface

    return getColor(
        if (profileProgress > 0F) profileProgress else fabProgress,
        startColor,
        endColor
    )
}

@Composable
private fun getContentColor(fabProgress: Float, profileProgress: Float): Color {
    val startColor = if (fabProgress == 1F) colors.onSurface else colors.onPrimary
    val endColor = if (profileProgress == 0F) colors.onSurface else colors.onWarningSurface

    return getColor(
        if (profileProgress > 0F) profileProgress else fabProgress,
        startColor,
        endColor
    )
}

private fun getColor(progress: Float, start: Color, end: Color) =
    progress.progressIn(0F, 0.4F).lerpBetween(start, end)
