package org.beatonma.commons.kotlin.extensions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import org.beatonma.commons.core.extensions.autotag
import org.beatonma.commons.device.Sdk

private const val DEFAULT_PREFS = "prefs"

fun Context.getPrefs(
    name: String = DEFAULT_PREFS,
    mode: Int = Context.MODE_PRIVATE,
): SharedPreferences = getSharedPreferences(name, mode)

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text.trim(), duration).show()
}

@Suppress("DEPRECATION")
fun Context?.stringCompat(@StringRes @PluralsRes resId: Int, vararg formatArgs: Any?, quantity: Int = Int.MIN_VALUE): String = when {
    this == null -> ""
    quantity != Int.MIN_VALUE -> resources.getQuantityString(resId, quantity, *formatArgs)
    Sdk.isMarshmallow -> getString(resId, *formatArgs)
    else -> resources.getString(resId, *formatArgs)
}



fun Context?.openUrl(url: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
    ) ?: Log.d(autotag, "openUrl failed: Context is null!")
}

fun Context?.dial(phoneNumber: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
    ) ?: Log.d(autotag, "dial failed: Context is null!")
}

fun Context?.sendMail(emailAddress: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$emailAddress")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        }
    ) ?: Log.d(autotag, "sendMail failed: Context is null")
}


fun Context.tryStartActivity(
    intent: Intent,
    otherwise: (() -> Unit)? = { println("Failed to resolve activity for intent $intent") }
) {
    try {
        toast(intent.dataString ?: "NO DATA", Toast.LENGTH_SHORT)
//        startActivity(intent)
    } catch (e: Exception) {
        otherwise?.invoke()
    }
}
