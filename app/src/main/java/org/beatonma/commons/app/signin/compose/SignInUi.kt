package org.beatonma.commons.app.signin.compose

import androidx.compose.animation.transition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.BottomSheetText
import org.beatonma.commons.app.ui.compose.components.CommonsOutlinedButton
import org.beatonma.commons.app.ui.compose.components.FabBottomSheet
import org.beatonma.commons.app.ui.compose.components.FabText
import org.beatonma.commons.app.ui.compose.components.image.Avatar
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.LinkedText
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.theme.compose.Padding

/**
 * Chooses the appropriate user UI to display depending on whether they are
 * already signed in.
 */
@Composable
fun ContextualUserAccountUi(userToken: UserToken = AmbientUserToken.current) =
    when (userToken) {
        NullUserToken -> SignInUi()
        else -> UserProfileUi()
    }

@Composable
fun SignInUi(actions: UserAccountActions = AmbientUserProfileActions.current.userAccountActions) =
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
fun UserProfileUi(userToken: UserToken = AmbientUserToken.current) =
    FabBottomSheet(
        fabContent = { progress ->
            FabText(userToken.username, progress)
        },
        bottomSheetContent = { progress ->
            ProfileSheetContent(userToken, progress)
        },
    )

@OptIn(ExperimentalFocus::class)
@Composable
private fun ProfileSheetContent(
    userToken: UserToken,
    progress: Float,
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

            CommonsOutlinedButton(
                onClick = userProfileActions.userAccountActions.signOut,
                modifier = Modifier
                    .align(Alignment.End)
                    .wrapContentHeight(nonEditableVisibility)
                    .alpha(nonEditableVisibility)
                    .padding(Padding.CardButton)
            ) {
                Text(stringResource(R.string.account_sign_out))
            }
        }
    }
}

private val MutableState<EditableState>.isReadOnly get() = value == EditableState.ReadOnly
