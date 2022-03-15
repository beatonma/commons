package org.beatonma.commons.app.ui.components.chips

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import org.beatonma.commons.R
import org.beatonma.commons.app.util.dial
import org.beatonma.commons.compose.animation.AutoCollapse
import org.beatonma.commons.compose.components.CollapsibleChip
import org.beatonma.commons.theme.AppIcon

@Composable
fun PhoneLink(
    phoneNumber: String,
    modifier: Modifier = Modifier,
    autoCollapse: Long = AutoCollapse.Default,
) {
    val displayText = AnnotatedString(
        stringResource(
            R.string.action_dial_phonenumber,
            phoneNumber
        )
    )

    CollapsibleChip(
        displayText,
        contentDescription = displayText.text,
        icon = AppIcon.Phone,
        modifier = modifier,
        autoCollapse = autoCollapse,
        confirmAction = dial(phoneNumber),
    )
}
