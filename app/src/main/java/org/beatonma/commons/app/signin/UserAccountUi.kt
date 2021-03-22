package org.beatonma.commons.app.signin

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.CommonsOutlinedButton
import org.beatonma.commons.app.ui.compose.components.WarningButton
import org.beatonma.commons.app.ui.compose.components.image.Avatar
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.animation.AnimatedVisibility
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.components.fabbottomsheet.BottomSheetText
import org.beatonma.commons.compose.components.fabbottomsheet.FabBottomSheet
import org.beatonma.commons.compose.components.fabbottomsheet.FabText
import org.beatonma.commons.compose.components.fabbottomsheet.animateExpansionAsState
import org.beatonma.commons.compose.components.fabbottomsheet.rememberFabBottomSheetState
import org.beatonma.commons.compose.components.text.Caption
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.data.accessibility.contentDescription
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.onWarningSurface
import org.beatonma.commons.theme.compose.theme.warningSurface

/**
 * Chooses the appropriate user UI to display depending on whether they are
 * already signed in.
 */
@Composable
fun UserAccountFabUi(
    userToken: UserToken = LocalUserToken.current,
    accountActions: UserAccountActions = LocalPlatformUserAccountActions.current.userAccountActions
) =
    when (userToken) {
        NullUserToken -> SignInFabUi(accountActions)
        else -> UserProfileFabUi(userToken, accountActions)
    }

internal enum class ProfileState {
    Overview,
    DeleteAccount,
}

@Composable
internal fun UserProfileFabUi(
    userToken: UserToken = LocalUserToken.current,
    accountActions: UserAccountActions,
) {
    val fabState = rememberFabBottomSheetState()
    val profileState = remember { mutableStateOf(ProfileState.Overview) }

    val fabProgress by fabState.value.animateExpansionAsState()
    val profileStateProgress by profileState.value.animateAsState()

    FabBottomSheet(
        fabClickLabel = userToken.contentDescription,
        state = fabState,
        surfaceColor = getSurfaceColor(fabProgress, profileStateProgress),
        contentColor = getContentColor(fabProgress, profileStateProgress),
        onDismiss = { profileState.value = ProfileState.Overview },
        fabContent = { progress ->
            FabText(
                userToken.username,
                progress,
                Modifier.testTag("user_account_fab")
            )
        },
        bottomSheetContent = { progress ->
            if (profileStateProgress != 1F) {
                ProfileSheetContent(
                    userToken = userToken,
                    progress = progress - profileStateProgress,
                    profileState = profileState,
                    userAccountActions = accountActions,
                )
            }

            if (profileStateProgress > 0F) {
                DeleteAccountUi(
                    userToken,
                    progress = profileStateProgress,
                    profileState = profileState,
                    confirmDeleteAction = accountActions.deleteAccount
                )
            }
        },
    )
}

@Composable
private fun ProfileSheetContent(
    userToken: UserToken,
    progress: Float,
    profileState: MutableState<ProfileState>,
    userAccountActions: UserAccountActions,
) {
    val usernameState = remember { mutableStateOf(EditableState.ReadOnly) }
    val isReadOnly = usernameState.value == EditableState.ReadOnly
    val readOnlyVisibility by animation.animateFloatAsState(
        if (isReadOnly) 1F else 0F
    )

    ProfileSheetContent(
        userToken = userToken,
        progress = progress,
        readOnlyVisibility = readOnlyVisibility,
        usernameState = usernameState,
        isReadOnly = isReadOnly,
        onProfileStateChange = { profileState.value = it },
        userAccountActions = userAccountActions,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ProfileSheetContent(
    userToken: UserToken,
    progress: Float,
    readOnlyVisibility: Float,
    usernameState: MutableState<EditableState>,
    isReadOnly: Boolean,
    onProfileStateChange: (ProfileState) -> Unit,
    userAccountActions: UserAccountActions,
) {
    BottomSheetText(
        progress,
        Modifier.testTag("user_account_sheet"),
        handleImeInsets = true,
    ) {
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                animation.AnimatedVisibility(isReadOnly) {
                    Avatar(
                        userToken.photoUrl,
                        Modifier
                            .padding(end = 16.dp)
                            .size(96.dp)
                            .clip(shapes.small)
                    )
                }

                Column {
                    Username(
                        userToken,
                        usernameState,
                        userAccountActions.renameAccount
                    )

                    animation.AnimatedVisibility(isReadOnly, horizontal = false) {
                        Caption(
                            dotted(userToken.name, userToken.email),
                            Modifier.padding(Padding.VerticalListItem)
                        )
                    }
                }
            }

            animation.AnimatedVisibility(isReadOnly, horizontal = false) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DeleteAccountButton(
                        onClick = { onProfileStateChange(ProfileState.DeleteAccount) }
                    )

                    SignOutButton(
                        onClick = userAccountActions.signOut
                    )
                }
            }
        }
    }
}

@Composable
private fun Username(
    userToken: UserToken,
    usernameState: MutableState<EditableState>,
    onSubmitRename: suspend (UserToken, String) -> RenameResult,
) {
    EditableUsername(
        userToken,
        state = usernameState,
        onSubmitRename = onSubmitRename,
    )
}

@Composable
private fun SignOutButton(
    onClick: () -> Unit,
) {
    CommonsOutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .padding(Padding.CardButton)
            .testTag("action_sign_out"),
    ) {
        ResourceText(R.string.account_sign_out)
    }
}

@Composable
private fun DeleteAccountButton(
    onClick: () -> Unit,
) {
    WarningButton(
        onClick = onClick,
        modifier = Modifier
            .padding(Padding.CardButton)
            .testTag("action_delete_account"),
    ) {
        ResourceText(R.string.account_delete_account_button)
    }
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

@Composable
private fun ProfileState.animateAsState() = animation.animateFloatAsState(
    when (this) {
        ProfileState.Overview -> 0F
        ProfileState.DeleteAccount -> 1F
    }
)

private fun getColor(progress: Float, start: Color, end: Color) =
    progress.progressIn(0F, 0.4F).lerpBetween(start, end)
