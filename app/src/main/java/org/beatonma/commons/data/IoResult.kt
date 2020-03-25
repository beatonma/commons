package org.beatonma.commons.data

import android.util.Log

private const val TAG = "IoResult"

data class IoResult<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        ERROR,
        NETWORK_ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T, message: String? = null): IoResult<T> {
            Log.d(TAG, "SUCCESS: ${message ?: ""}$data")
            return IoResult(Status.SUCCESS, data, message)
        }

        fun <T> networkError(message: String?, data: T? = null): IoResult<T> {
            Log.e(TAG, "NETWORK ERROR: $message")
            return IoResult(Status.NETWORK_ERROR, data, message)
        }

        fun <T> error(message: String?, data: T? = null): IoResult<T> {
            Log.e(TAG, "ERROR: $message")
            return IoResult(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): IoResult<T> {
            Log.d(TAG, "LOADING")
            return IoResult(Status.LOADING, data, null)
        }
    }
}
