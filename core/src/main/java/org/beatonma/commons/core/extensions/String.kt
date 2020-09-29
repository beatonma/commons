package org.beatonma.commons.core.extensions

/**
 * If the receiver is too long, return a substring of the desired length.
 * If clipping would remove less than [lenience] characters then don't bother - looks messy otherwise.
 */
fun String.clipToLength(
    targetMaxLength: Int,
    lenience: Int = 3,
    continuanceSymbol: Char = '…',
): String {
    return when {
        targetMaxLength < 0 -> this
        this.length <= targetMaxLength + lenience -> this
        else -> "${subSequence(0, targetMaxLength + 1)}$continuanceSymbol"
    }
}
