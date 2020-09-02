package org.beatonma.commons.test.extensions.assertions


/**
 * Handy for testing tests!
 * Ensure that code that should throw an AssertionError actually does
 */
inline fun assertThrowsAssertionError(
    message: String? = null,
    block: () -> Unit
) {
    var throwsError = false
    try {
        block.invoke()
    } catch (e: AssertionError) {
        throwsError = true
        println("Expected AssertionError [${e.message ?: "no message"}] was thrown correctly")
    }
    if (!throwsError) {
        throw AssertionError("Expected an AssertionError but none was thrown: $message")
    }
}
