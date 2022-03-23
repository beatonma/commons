package org.beatonma.commons.app.ui.screens.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.CommonsOutlinedButton
import org.beatonma.commons.app.ui.components.LoadingIcon
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.components.button.ConfirmationState
import org.beatonma.commons.compose.components.button.DoubleConfirmationButton
import org.beatonma.commons.compose.components.button.doubleConfirmationColors
import org.beatonma.commons.compose.components.button.rememberConfirmationState
import org.beatonma.commons.compose.components.fabbottomsheet.BottomSheetText
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.padding.endOfContent
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.theme.onWarningSurface
import org.beatonma.commons.theme.warningSurface
import org.beatonma.commons.themed.themedButtons


@Composable
internal fun DeleteAccountUi(
    userToken: UserToken,
    progress: Float,
    profileState: MutableState<ProfileState>,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    confirmDeleteAction: suspend (UserToken) -> Unit,
) {
    BottomSheetText(progress, Modifier.testTag(UserAccountTestTag.DeleteAccountUI)) {
        Column(
            Modifier
                .fillMaxWidth()
                .alpha(progress.progressIn(0.6F, 1F))
        ) {
            ResourceText(R.string.account_delete_account_title, style = typography.h4)
            ResourceText(R.string.account_delete_explanation)

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .endOfContent(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConfirmDeletionButton(
                    userToken = userToken,
                    coroutineScope = coroutineScope,
                    confirmDeleteAction = confirmDeleteAction,
                    enabled = progress == 1f,
                )

                CancelButton(onClick = { profileState.value = ProfileState.Overview })
            }
        }
    }
}

@Composable
private fun ConfirmDeletionButton(
    userToken: UserToken,
    coroutineScope: CoroutineScope,
    enabled: Boolean,
    confirmDeleteAction: suspend (UserToken) -> Unit,
) {
    var confirmationState by rememberConfirmationState()

    DoubleConfirmationButton(
        onClick = {
            coroutineScope.launch {
                confirmDeleteAction(userToken)
            }
        },
        state = confirmationState,
        onStateChange = { confirmationState = it },
        modifier = Modifier.testTag(UserAccountTestTag.DeleteAccount),
        enabled = enabled,
        colors = doubleConfirmationColors(
            safeColors = themedButtons.buttonColors(
                contentColor = colors.onWarningSurface,
                backgroundColor = colors.warningSurface
            ),
            awaitingConfirmationColors = themedButtons.buttonColors(
                contentColor = colors.warningSurface,
                backgroundColor = colors.onWarningSurface
            ),
            confirmedColor = themedButtons.surfaceButtonColors()
        ),
        border = when (confirmationState) {
            ConfirmationState.Confirmed -> null
            else -> ButtonDefaults.outlinedBorder
        },
        safeContent = { ResourceText(R.string.account_delete_confirm_button) },
        awaitingConfirmationContent = { ResourceText(R.string.account_delete_confirm_twice_button) },
        confirmedContent = { LoadingIcon() }
    )
}

@Composable
private fun CancelButton(
    onClick: () -> Unit,
) {
    CommonsOutlinedButton(
        onClick = onClick,
        colors = themedButtons.surfaceButtonColors(),
        modifier = Modifier.testTag(TestTag.Cancel)
    ) {
        ResourceText(R.string.account_delete_cancel_button)
    }
}
