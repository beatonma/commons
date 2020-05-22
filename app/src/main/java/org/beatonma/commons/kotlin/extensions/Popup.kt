package org.beatonma.commons.kotlin.extensions

import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.beatonma.commons.R
import org.beatonma.commons.device.Sdk

fun View.snackbar(
    @StringRes textResId: Int,
    length: Int = Snackbar.LENGTH_LONG,
    action: SnackbarAction? = null
) {
    Snackbar.make(this, textResId, length).apply {
        if (action != null) {
            setAction(action.textResId, action.onclick)
        }
    }.show()
}

fun View.snackbar(
    text: String,
    length: Int = Snackbar.LENGTH_LONG,
    action: SnackbarAction? = null,
) {
    Snackbar.make(this, text, length).apply {
        if (action != null) {
            setAction(action.textResId, action.onclick)
        }
    }.show()
}


fun Fragment.snackbar(
    @StringRes textResId: Int,
    length: Int = Snackbar.LENGTH_LONG,
    action: SnackbarAction? = null,
) {
    view?.let { v ->
        Snackbar.make(v, textResId, length).apply {
            if (action != null) {
                setAction(action.textResId, action.onclick)
            }
        }.show()
    }
}

fun Fragment.snackbar(
    text: String,
    length: Int = Snackbar.LENGTH_LONG,
    action: SnackbarAction? = null,
) {
    view?.let { v ->
        Snackbar.make(v, text, length).apply {
            if (action != null) {
                setAction(action.textResId, action.onclick)
            }
        }.show()
    }
}

fun Fragment.networkErrorSnackbar(
    action: SnackbarAction? = actionOpenWifiSettings
) = snackbar(R.string.error_network, action = action)

data class SnackbarAction(@StringRes val textResId: Int, val onclick: View.OnClickListener)

private val Fragment.actionOpenWifiSettings: SnackbarAction?
    get() = if (Sdk.isQ) {
        SnackbarAction(
            R.string.action_open_wifi_settings, onclick = {
                startActivity(Intent(Settings.Panel.ACTION_WIFI))
            }
        )
    }
    else null
