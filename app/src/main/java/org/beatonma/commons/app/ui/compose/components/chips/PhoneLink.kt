package org.beatonma.commons.app.ui.compose.components.chips

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import org.beatonma.commons.R
import org.beatonma.commons.compose.components.CollapsibleChip
import org.beatonma.commons.kotlin.extensions.dial

@Composable
fun PhoneLink(phoneNumber: String, modifier: Modifier = Modifier, autoCollapse: Long = 2500) {
    val displayText = AnnotatedString(stringResource(R.string.action_dial_phonenumber, phoneNumber))
    val context = AmbientContext.current

    CollapsibleChip(
        displayText,
        R.drawable.ic_phone_call,
        R.drawable.ic_close,
        modifier,
        autoCollapse
    ) {
        context.dial(phoneNumber)
    }
}
