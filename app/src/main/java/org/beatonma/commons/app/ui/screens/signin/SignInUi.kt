package org.beatonma.commons.app.ui.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.beatonma.commons.R
import org.beatonma.commons.compose.components.fabbottomsheet.BottomSheetText
import org.beatonma.commons.compose.components.fabbottomsheet.FabBottomSheet
import org.beatonma.commons.compose.components.fabbottomsheet.FabText
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.util.HtmlText
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.theme.textPrimaryDark
import org.beatonma.commons.themed.buttons


@Composable
fun SignInFabUi(
    accountActions: UserAccountActions = LocalUserAccountActions.current,
) {
    SignInFabUi(onSignIn = accountActions.signIn)
}

@Composable
private fun SignInFabUi(onSignIn: () -> Unit) {
    FabBottomSheet(
        fabClickLabel = stringResource(R.string.content_description_account_sign_in),
        fabContent = { progress ->
            FabText(
                stringResource(R.string.account_sign_in),
                progress,
            )
        },
        bottomSheetContent = { progress ->
            BottomSheetText(
                progress,
                Modifier.testTag(UserAccountTestTag.SignInSheet)
            ) {
                Column(Modifier.fillMaxWidth()) {
                    SignInRationale(progress)

                    SignInButton(
                        onClick = onSignIn,
                        modifier = Modifier.align(Alignment.End)
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        },
    )
}

@Composable
private fun SignInRationale(visibilityProgress: Float) {
    val linkedTextSize = visibilityProgress.progressIn(0F, 0.1F)

    HtmlText(
        stringResource(R.string.account_sign_in_rationale),
        clickable = visibilityProgress == 1f,
        modifier = Modifier
            .wrapContentSize(linkedTextSize)
            .testTag(UserAccountTestTag.SignInRationale)
    )
}

@Composable
private fun SignInButton(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    GoogleSignInButton(onClick, modifier)
}


/**
 * Spec: https://developers.google.com/identity/branding-guidelines
 */
@Composable
private fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Button(
        onClick,
        modifier
            .height(40.dp)
            .testTag(UserAccountTestTag.SignInGoogleButton),
        colors = buttons.buttonColors(contentColor = colors.textPrimaryDark,
            backgroundColor = Color.White),
    ) {
        GoogleIcon()
        Spacer(Modifier.width(24.dp))
        Text(
            stringResource(R.string.account_sign_in_google),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun GoogleIcon() {
    Image(
        painterResource(R.drawable.googleg_standard_color_18),
        contentDescription = stringResource(R.string.account_sign_in_google),
        modifier = Modifier
            .size(18.dp)
            .background(Color.White)
    )
}
