package org.beatonma.commons.core.extensions

inline fun <T, R> withNotNull(obj: T?, block: (T) -> R) {
    if (obj != null) {
        block(obj)
    }
}
