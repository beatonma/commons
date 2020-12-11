package org.beatonma.commons.app.ui.compose.components.chips

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import org.beatonma.commons.R
import org.beatonma.commons.compose.animation.AutoCollapse
import org.beatonma.commons.compose.components.CollapsibleChip
import org.beatonma.commons.kotlin.extensions.sendMail

@Composable
fun EmailLink(
    address: String,
    modifier: Modifier = Modifier,
    autoCollapse: Long = AutoCollapse.Default,
) {
    val displayText = AnnotatedString(stringResource(R.string.action_write_email, address))
    val context = AmbientContext.current

    CollapsibleChip(
        displayText,
        R.drawable.ic_email,
        R.drawable.ic_close,
        modifier,
        autoCollapse
    ) {
        context.sendMail(address)
    }
}
