package org.beatonma.commons.app.signin.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.FabText
import org.beatonma.commons.app.ui.compose.components.SwipeableFabBottomSheet
import org.beatonma.commons.app.ui.compose.components.Todo
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.components.LinkedText
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.theme.compose.theme.systemui.navigationBarsPadding

/**
 * Chooses the appropriate user UI to display depending on whether they are
 * already signed in.
 */
@Composable
fun ContextualUserAccountUi(
    modifier: Modifier = Modifier.fillMaxSize(),
    userToken: UserToken = AmbientUserToken.current,
) {
    if (userToken == NullUserToken) {
        SignInUi(modifier)
    }
    else {
        UserProfileUi(modifier)
    }
}

@Composable
fun SignInUi(
    modifier: Modifier = Modifier,
    actions: SignInActions = AmbientSignInActions.current,
) {
    SwipeableFabBottomSheet(
        fabContent = { progress ->
            FabText(stringResource(R.string.account_sign_in), progress)
        },
        bottomSheetContent = { progress ->
            CardText(
                Modifier
                    .navigationBarsPadding(scale = progress)
                    .wrapContentSize(
                        horizontalProgress = progress.progressIn(0F, 0.4F),
                        verticalProgress = progress.progressIn(0F, 0.8F)
                    )
                    .drawOpacity(progress.progressIn(0.8F, 1F))
            ) {
                Column(Modifier.fillMaxWidth()) {
                    LinkedText(stringResource(R.string.account_sign_in_rationale),
                        clickable = progress == 1F)

                    AndroidView(
                        viewBlock = { context ->
                            SignInButton(context).apply {
                                setOnClickListener { actions.signIn() }
                            }
                        },
                        Modifier.align(Alignment.End),
                    )
                }
            }
        },
    )
}

@Composable
fun UserProfileUi(modifier: Modifier = Modifier) {
    Todo("UserProfileUi", modifier)
}
