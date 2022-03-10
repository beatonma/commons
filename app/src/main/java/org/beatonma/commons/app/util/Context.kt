package org.beatonma.commons.app.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.beatonma.commons.core.extensions.autotag

private const val DEFAULT_PREFS = "prefs"

fun Context.getPrefs(
    name: String = DEFAULT_PREFS,
    mode: Int = Context.MODE_PRIVATE,
): SharedPreferences = getSharedPreferences(name, mode)

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text.trim(), duration).show()
}

fun Context?.openUrl(url: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
    ) ?: Log.d(autotag, "openUrl failed: Context is null!")
}

@Composable
fun openUrl(url: String): () -> Unit {
    val context = LocalContext.current
    return { context.openUrl(url) }
}

fun Context?.dial(phoneNumber: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
    ) ?: Log.d(autotag, "dial failed: Context is null!")
}

@Composable
fun dial(phoneNumber: String): () -> Unit {
    val context = LocalContext.current
    return { context.dial(phoneNumber) }
}

fun Context?.sendMail(emailAddress: String) {
    this?.tryStartActivity(
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$emailAddress")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        }
    ) ?: Log.d(autotag, "sendMail failed: Context is null")
}

@Composable
fun sendMail(emailAddress: String): () -> Unit {
    val context = LocalContext.current
    return { context.sendMail(emailAddress) }
}


fun Context.tryStartActivity(
    intent: Intent,
    otherwise: (() -> Unit)? = { println("Failed to resolve activity for intent $intent") },
) {
    try {
        toast(intent.dataString ?: "NO DATA", Toast.LENGTH_SHORT)
//        startActivity(intent)
    } catch (e: Exception) {
        otherwise?.invoke()
    }
}
