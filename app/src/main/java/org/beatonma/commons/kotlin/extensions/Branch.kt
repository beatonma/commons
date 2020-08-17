package org.beatonma.commons.kotlin.extensions

inline fun <T, R> withNotNull(obj: T?, block: (T) -> R) {
    if (obj != null) {
        block(obj)
    }
}
