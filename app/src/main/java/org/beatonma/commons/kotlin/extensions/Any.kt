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
fun <T: Any?> T.dump(message: String = "", maxLength: Int = -1): T {
    when (this) {
        is Collection<*> -> {
            if (message.isNotEmpty()) println("$message [$size]:")
            forEach { item -> item.dump("  ", maxLength) }
        }

        is Array<*> -> {
            if (message.isNotEmpty()) println("$message [$size]:")
            forEach { item -> item.dump("  ", maxLength) }
        }

        is IntArray -> {
            println("${if (message.isNotEmpty()) "$message " else ""}[$size]: ${ joinToString(separator = ", ") { "$it" } }".clipToLength(maxLength))
        }

        else -> {
            val str = toString().clipToLength(maxLength)
            val step = 2048
            val length = str.length
            var pos = 0
            while(pos < str.length) {
                println("${if (message.isNotEmpty()) "$message " else "" } ${str.substring(pos, min(length, pos + step))}")
                pos += step
            }
        }
    }

    return this
}
