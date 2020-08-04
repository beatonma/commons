package org.beatonma.commons.kotlin.extensions

fun <T, R> withNotNull(obj: T?, block: (T) -> R) {
    if (obj != null) {
        block(obj)
    }
}
