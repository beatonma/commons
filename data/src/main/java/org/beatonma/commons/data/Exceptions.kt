package org.beatonma.commons.data

import retrofit2.Response

/**
 * Indicates that a code branch should never be reached because of a previous precondition.
 * e.g. In a Map with non-null values:
 *      - if (key in map) returns true
 *      - map[key] still returns a nullable value
 *
 * These sorts of things are normally handled by kotlin smart-casting, but there are still some exceptions.
 *
 * @param whyNot is required so that if this does somehow get thrown we can check the reasoning.
 */
class ShouldNotHappen(
    whyNot: String,
    throwable: Throwable? = null
): Exception(
    "ShouldNotHappen because: $whyNot",
    throwable
)


class NetworkException(val code: Int, message: String): Exception(message) {
    constructor(response: Response<*>): this(response.code(), response.message())
}
