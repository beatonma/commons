package org.beatonma.commons.kotlin.extensions

import android.util.Log
import org.beatonma.commons.core.extensions.autotag

fun Any?.log(message: String, loglevel: Char = 'd', tag: String = autotag) {
    when (loglevel) {
        'd' -> Log.d(tag, message)
        'e' -> Log.e(tag, message)
        'i' -> Log.i(tag, message)
        'v' -> Log.v(tag, message)
        'w' -> Log.w(tag, message)
    }
}
