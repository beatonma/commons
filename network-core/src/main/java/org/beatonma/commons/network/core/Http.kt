package org.beatonma.commons.network.core

@Suppress("unused")
object Http {
    object Method {
        const val DELETE = "DELETE"
    }

    object Header {
        const val USER_AGENT = "User-Agent"
        const val CONTENT_TYPE = "Content-Type"
    }

    object ContentType {
        const val JSON = "application/json"
    }

    object Status {
        const val OK_200 = 200
        const val OK_NO_CONTENT_204 = 204
        const val MULTIPLE_CHOICE_300 = 300
        const val BAD_REQUEST_400 = 400
        const val UNAUTHORIZED_401 = 401
        const val FORBIDDEN_403 = 403
        const val INTERNAL_SERVER_ERROR_500 = 500

        fun isSuccess(status: Int) = status in OK_200 until MULTIPLE_CHOICE_300
        fun isClientError(status: Int) = status in BAD_REQUEST_400 until INTERNAL_SERVER_ERROR_500
        fun isServerError(status: Int) = status >= INTERNAL_SERVER_ERROR_500
        fun isError(status: Int) = status >= BAD_REQUEST_400
    }
}
