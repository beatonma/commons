package org.beatonma.commons.app.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.CommonsOutlinedButton
import org.beatonma.commons.app.ui.compose.components.LoadingIcon
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.ConfirmationState
import org.beatonma.commons.compose.components.DoubleConfirmationButton
import org.beatonma.commons.compose.components.doubleConfirmationColors
import org.beatonma.commons.compose.components.fabbottomsheet.BottomSheetText
import org.beatonma.commons.compose.components.rememberConfirmationState
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.CommonsButtons
import org.beatonma.commons.theme.compose.theme.onWarningSurface
import org.beatonma.commons.theme.compose.theme.warningSurface


@Composable
internal fun DeleteAccountUi(
    userToken: UserToken,
    progress: Float,
    profileState: MutableState<ProfileState>,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    confirmDeleteAction: suspend (UserToken) -> Unit,
) {
    println("DeleteAccountUi")
    BottomSheetText(progress, Modifier.testTag("delete_account_ui")) {
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
                    .padding(Padding.EndOfContent),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ConfirmDeletionButton(
                    userToken = userToken,
                    coroutineScope = coroutineScope,
                    confirmDeleteAction = confirmDeleteAction,
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
    confirmDeleteAction: suspend (UserToken) -> Unit,
) {
    val confirmationState = rememberConfirmationState()

    DoubleConfirmationButton(
        modifier = Modifier.testTag("action_confirm_delete"),
        onClick = {
            coroutineScope.launch {
                confirmDeleteAction(userToken)
            }
        },
        state = confirmationState,
        colors = doubleConfirmationColors(
            safeColors = CommonsButtons.contentButtonColors(),
            awaitingConfirmationColors = CommonsButtons.buttonColors(
                contentColor = colors.warningSurface,
                backgroundColor = colors.onWarningSurface),
            confirmedColor = CommonsButtons.contentButtonColors()
        ),
        border = if (confirmationState.value == ConfirmationState.Confirmed) null else ButtonDefaults.outlinedBorder,
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
        colors = CommonsButtons.contentButtonColors(),
        modifier = Modifier.testTag("action_cancel")
    ) {
        ResourceText(R.string.account_delete_cancel_button)
    }
}
