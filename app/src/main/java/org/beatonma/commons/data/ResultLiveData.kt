package org.beatonma.commons.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

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
    if (response is SuccessResult) {
        if (response.data != null) {
            try {
                saveCallResult(response.data)
            }
            catch (e: Exception) {
                emit(LocalError<T>("Unable to save network result: $e"))
            }
        }
        else {
            emit(UnexpectedValueError<T>("Null data: ${response.message}"))
        }
    } else if (response is IoError) {
        emit(NetworkError<T>(response.message))
        emitSource(source)
    }
}


fun <T> resultLiveDataNoCache(
    networkCall: suspend () -> IoResult<T>,
): LiveDataIoResult<T> = liveData(Dispatchers.IO) {
    emit(LoadingResult<T>())
    val response = networkCall.invoke()
    if (response is SuccessResult) {
        if (response.data != null) {
            emit(SuccessResult(response.data, "Network "))
        }
        else {
            emit(UnexpectedValueError<T>("Null data: ${response.message}"))
        }
    } else if (response is IoError) {
        emit(NetworkError<T>(response.message))
    }
}
