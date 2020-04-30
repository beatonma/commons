package org.beatonma.commons.kotlin

/**
 * Return the result of the first function that does not throw an exception.
 * onException will be called with each encountered exception, if defined.
 */
fun <T> takeFirstSuccessful(vararg throwingBlocks:() -> T, onException: ((Exception) -> Unit)? = null): T? {
    if (throwingBlocks.isEmpty()) return null
    return try {
        throwingBlocks.first().invoke()
    }
    catch (e: Exception) {
        onException?.invoke(e)
        takeFirstSuccessful(
            *throwingBlocks.drop(1).toTypedArray(),
            onException = onException
        )
    }
}
