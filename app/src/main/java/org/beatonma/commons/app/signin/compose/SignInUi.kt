package org.beatonma.commons.app.signin.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.Avatar
import org.beatonma.commons.app.ui.compose.components.BottomSheetText
import org.beatonma.commons.app.ui.compose.components.CommonsOutlinedButton
import org.beatonma.commons.app.ui.compose.components.FabBottomSheet
import org.beatonma.commons.app.ui.compose.components.FabText
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.LinkedText
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.core.extensions.progressIn
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
fun SignInUi(actions: SignInActions = AmbientUserProfileActions.current.signInActions) =
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
            ProfileFabContent(userToken, progress)
        },
        bottomSheetContent = { progress ->
            ProfileSheetContent(userToken, progress)
        },
    )

@Composable
private fun ProfileFabContent(userToken: UserToken, progress: Float) =
    FabText(userToken.username, progress)

@Composable
private fun ProfileSheetContent(
    userToken: UserToken,
    progress: Float,
    userProfileActions: UserProfileActions = AmbientUserProfileActions.current,
) =
    BottomSheetText(progress) {
        Column(Modifier.wrapContentHeight()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(
                    userToken.photoUrl,
                    Modifier
                        .weight(2F)
                        .padding(end = 8.dp)
                        .aspectRatio(1F)
                        .clip(shapes.small)
                )

                Column(Modifier.weight(7F)) {
                    EditableUsername(userToken)

                    Text(dotted(userToken.name, userToken.email), style = typography.caption)
                }
            }

            CommonsOutlinedButton(
                onClick = userProfileActions.signInActions.signOut,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(Padding.CardButton)
            ) {
                Text(stringResource(R.string.account_sign_out))
            }
        }
    }
