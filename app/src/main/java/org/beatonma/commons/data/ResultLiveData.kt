package org.beatonma.commons.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers


private const val TAG = "resultLiveData"

fun <T, N> resultLiveData(
    databaseQuery: suspend () -> LiveData<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit
): LiveDataIoResult<T> = liveData(Dispatchers.IO) {
    emit(LoadingResult<T>())
    val source: LiveDataIoResult<T> = databaseQuery.invoke().map {
        SuccessResult(it, "DB read OK ")
    }
    emitSource(source)

    val response = networkCall.invoke()
    when (response) {
        is SuccessResult -> {
            if (response.data != null) {
                try {
                    saveCallResult(response.data)
                }
                catch (e: Exception) {
                    emit(LocalError<T>("Unable to save network result: $e", e))
                }
            }
            else {
                emit(UnexpectedValueError<T>("Null data: ${response.message}", null))
            }
        }

        is IoError -> {
            emit(NetworkError<T>(response.message, response.error))
            emitSource(source)
        }
    }
}


fun <T, N> resultLiveDataLocalPreferred(
    databaseQuery: suspend () -> LiveData<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit
): LiveDataIoResult<T> = liveData(Dispatchers.IO) {
    emit(LoadingResult<T>())
    val source: LiveDataIoResult<T> = databaseQuery.invoke().map {
        SuccessResult(it, "DB read OK ")
    }
    emitSource(source)

    if (source.value != null) {
        // If database returned usable data then no need to make the network call
        Log.i(TAG, "Local data is usable - skipping network call")
        return@liveData
    }

    val response = networkCall.invoke()
    when (response) {
        is SuccessResult -> {
            if (response.data != null) {
                try {
                    saveCallResult(response.data)
                }
                catch (e: Exception) {
                    emit(LocalError<T>("Unable to save network result: $e", e))
                }
            }
            else {
                emit(UnexpectedValueError<T>("Null data: ${response.message}", null))
            }
        }

        is IoError -> {
            emit(NetworkError<T>(response.message, response.error))
            emitSource(source)
        }
    }
}


fun <T> resultLiveDataNoCache(
    networkCall: suspend () -> IoResult<T>,
): LiveDataIoResult<T> = liveData(Dispatchers.IO) {
    emit(LoadingResult<T>())
    val response = networkCall.invoke()
    when (response) {
        is SuccessResult -> {
            if (response.data != null) {
                emit(SuccessResult(response.data, "Network OK"))
            }
            else {
                emit(UnexpectedValueError<T>("Null data: ${response.message}", null))
            }
        }

        is IoError -> {
            emit(NetworkError<T>(response.message, response.error))
        }
    }
}
