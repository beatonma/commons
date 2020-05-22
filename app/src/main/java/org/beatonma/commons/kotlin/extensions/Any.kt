package org.beatonma.commons.kotlin.extensions

import android.util.Log
import java.lang.Integer.min

val Any?.autotag: String
    get() = this?.javaClass?.simpleName ?: "Null"

fun Any?.log(message: String, loglevel: Char = 'd', tag: String = autotag) {
    when (loglevel) {
        'd' -> Log.d(tag, message)
        'e' -> Log.e(tag, message)
        'i' -> Log.i(tag, message)
        'v' -> Log.v(tag, message)
        'w' -> Log.w(tag, message)
    }
}

/**
 * Dump value to log (with optional message) and return `this` value
 * Convenience for `.also { println("$it") }`
 * If the resulting string is very long it will be broken into chunks.
 * Should only be used for debugging purposes
 */
fun <T> T.dump(message: String = ""): T {
    if (this is Collection<*>) {
        if (message.isNotEmpty()) println("$message [$size]:")
        return also {
            forEach { item -> item.dump("  ") }
        }
    }
    val str = toString()
    val step = 2048
    val length = str.length
    var pos = 0
    while(pos < str.length) {
        println("${if (message.isNotEmpty()) "$message " else "" } ${str.substring(pos, min(length, pos + step))}")
        pos += step
    }
    return this
}
