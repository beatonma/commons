package org.beatonma.commons.app.ui.views.chip

import android.content.Context
import androidx.annotation.DrawableRes
import org.beatonma.commons.ClickAction
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.data.WeblinkData
import org.beatonma.commons.kotlin.extensions.dial
import org.beatonma.commons.kotlin.extensions.openUrl
import org.beatonma.commons.kotlin.extensions.sendMail
import org.beatonma.commons.kotlin.extensions.stringCompat


data class ChipData(
    val text: String?,
    @DrawableRes val icon: Int,
    val iconTint: Int? = null,
//    val backgroundTint: Int? = null,
    val action: ClickAction?,
) {

    companion object {
        fun forUrl(url: String) = forUrl(WeblinkData(url))

        fun forUrl(data: WeblinkData) = ChipData(
            text = data.displayText,
            icon = data.icon,
            action = { view -> view.context.openUrl(data.url) }
        )

        fun forPhoneNumber(context: Context, phoneNumber: String) = ChipData(
            text = context.stringCompat(R.string.action_dial_phonenumber, phoneNumber),
            icon = R.drawable.ic_phone_call,
            action = { view -> view.context.dial(phoneNumber) }
        )

        fun forEmail(context: Context, address: String) = ChipData(
            text = context.stringCompat(R.string.action_write_email, address),
            icon = R.drawable.ic_email,
            action = { view -> view.context.sendMail(address) }
        )
    }
}
