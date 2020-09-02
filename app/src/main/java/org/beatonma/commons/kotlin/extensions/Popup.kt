package org.beatonma.commons.kotlin.extensions

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.beatonma.commons.ClickAction
import org.beatonma.commons.R
import org.beatonma.commons.device.Sdk
import org.beatonma.commons.repo.result.NetworkError

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
        anchorView = this@snackbar
    }.show()
}


fun Fragment.snackbar(
    @StringRes textResId: Int,
    length: Int = Snackbar.LENGTH_LONG,
    action: SnackbarAction? = null,
    anchor: View? = null,
) {
    activity?.findViewById<CoordinatorLayout>(R.id.coordinator)?.let { v ->
        Snackbar.make(v, textResId, length).apply {
            if (action != null) {
                setAction(action.textResId, action.onclick)
            }
            if (anchor != null) {
                anchorView = anchor
            }
        }.show()
    }
}

fun Fragment.snackbar(
    text: String,
    length: Int = Snackbar.LENGTH_LONG,
    action: SnackbarAction? = null,
    anchor: View? = null,
) {
    activity?.findViewById<CoordinatorLayout>(R.id.coordinator)?.let { v ->
        Snackbar.make(v, text, length).apply {
            if (action != null) {
                setAction(action.textResId, action.onclick)
            }
            if (anchor != null) {
                anchorView = anchor
            }
        }.show()
    }
}

fun Fragment.networkErrorSnackbar(
    error: NetworkError,
    action: SnackbarAction? = actionOpenWifiSettings
) {
    Log.w(autotag, error.toString())
    snackbar(R.string.error_network, action = action)
}

data class SnackbarAction(@StringRes val textResId: Int, val onclick: ClickAction)

private val Fragment.actionOpenWifiSettings: SnackbarAction?
    get() = if (Sdk.isQ) {
        SnackbarAction(
            R.string.action_open_wifi_settings, onclick = {
                startActivity(Intent(Settings.Panel.ACTION_WIFI))
            }
        )
    }
    else null
