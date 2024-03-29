package org.beatonma.commons.app.ui.components.chips

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import org.beatonma.commons.R
import org.beatonma.commons.app.util.sendMail
import org.beatonma.commons.compose.animation.AutoCollapse
import org.beatonma.commons.compose.components.CollapsibleChip
import org.beatonma.commons.theme.AppIcon

@Composable
fun EmailLink(
    address: String,
    modifier: Modifier = Modifier,
    autoCollapse: Long = AutoCollapse.Default,
) {
    val displayText = AnnotatedString(
        stringResource(
            R.string.action_write_email,
            address
        )
    )

    CollapsibleChip(
        displayText,
        contentDescription = displayText.text,
        icon = AppIcon.Email,
        modifier = modifier,
        autoCollapse = autoCollapse,
        confirmAction = sendMail(address)
    )
}
