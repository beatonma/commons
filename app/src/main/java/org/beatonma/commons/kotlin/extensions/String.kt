package org.beatonma.commons.kotlin.extensions

import android.content.Context
import org.beatonma.commons.R

/**
 * If the receiver is too long, return a substring of the desired length.
 * If clipping would remove less than [lenience] characters then don't bother - looks messy otherwise.
 */
fun String.clipToLength(targetMaxLength: Int, lenience: Int = 3, continuanceSymbol: Char = '…'): String {
    return if (this.length <= targetMaxLength + lenience) this
    else "${subSequence(0, targetMaxLength + 1)}$continuanceSymbol"
}

/**
 * Join the given string components with a dot between each pair
 */
fun Context.dotted(vararg components: String?): String =
    components.filterNotNull()
        .joinToString(
            separator = " ${stringCompat(R.string.dot)} "
        )

fun Context.dotted(components: List<String?>): String =
    components.filterNotNull()
        .joinToString (
            separator = " ${stringCompat(R.string.dot)} "
        )
