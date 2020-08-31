package org.beatonma.commons.snommoc.annotations

import java.lang.annotation.Inherited

@Inherited
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
/**
 * An active user session is required.
 */
annotation class SignInRequired

class NotSignedIn(
    message: String? = null,
    cause: Throwable? = null,
): Throwable(
    message = "User must be signed in${if (message == null) "" else ": $message"}",
    cause = cause,
)
