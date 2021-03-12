package org.beatonma.commons.app.ui.compose.components.chips

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import org.beatonma.commons.R
import org.beatonma.commons.compose.animation.AutoCollapse
import org.beatonma.commons.compose.components.CollapsibleChip
import org.beatonma.commons.kotlin.extensions.dial

@Composable
fun PhoneLink(
    phoneNumber: String,
    modifier: Modifier = Modifier,
    autoCollapse: Long = AutoCollapse.Default,
) {
    val displayText = AnnotatedString(stringResource(R.string.action_dial_phonenumber, phoneNumber))
    val context = LocalContext.current

    CollapsibleChip(
        displayText,
        contentDescription = displayText.text,
        drawableId = R.drawable.ic_phone_call,
        modifier = modifier,
        autoCollapse = autoCollapse
    ) {
        context.dial(phoneNumber)
    }
}
