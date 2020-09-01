package org.beatonma.commons.network.core

import retrofit2.Response

class NetworkException(val code: Int, message: String): Exception(message) {
    constructor(response: Response<*>): this(response.code(), response.message())
}
