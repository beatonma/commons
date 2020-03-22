package org.beatonma.commons.data

import android.util.Log

private const val TAG = "Result"

data class Result<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T, message: String? = null): Result<T> {
            Log.d(TAG, "SUCCESS: ${message ?: ""}$data")
            return Result(Status.SUCCESS, data, message)
        }

        fun <T> error(message: String?, data: T? = null): Result<T> {
            Log.e(TAG, "ERROR: $message")
            return Result(Status.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Result<T> {
            Log.d(TAG, "LOADING")
            return Result(Status.LOADING, data, null)
        }
    }
}
