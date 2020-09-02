package org.beatonma.commons.test.extensions


/**
 * If the receiver is too long, return a substring of the desired length.
 * If clipping would remove less than [lenience] characters then don't bother - looks messy otherwise.
 */
private fun String.clipToLength(targetMaxLength: Int, lenience: Int = 3, continuanceSymbol: Char = '…'): String {
    return when {
        targetMaxLength < 0 -> this
        this.length <= targetMaxLength + lenience -> this
        else -> "${subSequence(0, targetMaxLength + 1)}$continuanceSymbol"
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
                println("${if (message.isNotEmpty()) "$message " else "" } ${str.substring(pos,
                    Integer.min(length, pos + step)
                )}")
                pos += step
            }
        }
    }

    return this
}
