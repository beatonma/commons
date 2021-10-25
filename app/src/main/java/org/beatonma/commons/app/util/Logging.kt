package org.beatonma.commons.app.util

import android.util.Log
import org.beatonma.commons.core.extensions.autotag


fun <T> T?.logWarning(message: String, tag: String = autotag) =
    Log.w(tag, message)

fun <T> T?.logDebug(message: String, tag: String = autotag) =
    Log.d(tag, message)

fun <T> T?.logInfo(message: String, tag: String = autotag) =
    Log.i(tag, message)

fun <T> T?.logError(message: String, tag: String = autotag) =
    Log.i(tag, message)


fun logWarning(message: String, tag: String = "Warning") =
    Log.w(tag, message)

fun logDebug(message: String, tag: String = "Debug") =
    Log.d(tag, message)

fun logInfo(message: String, tag: String = "Info") =
    Log.i(tag, message)

fun logError(message: String, tag: String = "Error") =
    Log.i(tag, message)
