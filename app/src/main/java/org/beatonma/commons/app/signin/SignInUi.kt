package org.beatonma.commons.app.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import org.beatonma.commons.R
import org.beatonma.commons.compose.components.fabbottomsheet.BottomSheetText
import org.beatonma.commons.compose.components.fabbottomsheet.FabBottomSheet
import org.beatonma.commons.compose.components.fabbottomsheet.FabText
import org.beatonma.commons.compose.components.text.LinkedText
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.core.extensions.progressIn


@Composable
fun SignInFabUi(
    accountActions: UserAccountActions = LocalUserAccountActions.current,
) {
    SignInFabUi(onSignIn = accountActions.signIn)
}

@Composable
fun SignInFabUi(
    onSignIn: () -> Unit
) {
    FabBottomSheet(
        fabClickLabel = stringResource(R.string.content_description_account_sign_in),
        fabContent = { progress ->
            FabText(
                stringResource(R.string.account_sign_in),
                progress,
                Modifier.testTag("sign_in_fab")
            )
        },
        bottomSheetContent = { progress ->
            println("SignInFabUi")
            BottomSheetText(
                progress,
                Modifier.testTag("sign_in_sheet")
            ) {
                Column(Modifier.fillMaxWidth()) {
                    SignInRationale(progress)

                    SignInButton(
                        onClick = onSignIn,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        },
    )
}

@Composable
private fun SignInRationale(visibilityProgress: Float) {
    val linkedTextSize = visibilityProgress.progressIn(0F, 0.1F)
    LinkedText(
        stringResource(R.string.account_sign_in_rationale),
        clickable = visibilityProgress == 1F,
        modifier = Modifier
            .wrapContentSize(linkedTextSize)
            .testTag("account_rationale")
    )
}

@Composable
private fun SignInButton(
    modifier: Modifier,
    onClick: () -> Unit,
) {
    AndroidView(
        factory = { context ->
            com.google.android.gms.common.SignInButton(context).apply {
                setSize(com.google.android.gms.common.SignInButton.SIZE_STANDARD)
                setColorScheme(com.google.android.gms.common.SignInButton.COLOR_AUTO)
                setOnClickListener { onClick() }
            }
        },
        modifier.testTag("google_signin_button"),
    )
}
