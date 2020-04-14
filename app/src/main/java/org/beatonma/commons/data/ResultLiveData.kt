package org.beatonma.commons.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

fun <T, N> resultLiveData(
    databaseQuery: suspend () -> LiveData<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit
): LiveData<IoResult<T>> = liveData(Dispatchers.IO) {
    emit(IoResult.loading<T>())
    val source = databaseQuery.invoke().map { IoResult.success(it, message = "DB read OK ") }
    emitSource(source)

    val response = networkCall.invoke()
    if (response.status == IoResult.Status.SUCCESS) {
        if (response.data != null) {
            try {
                saveCallResult(response.data)
            }
            catch (e: Exception) {
                emit(IoResult.error<T>("Unable to save network result: $e"))
            }
        }
        else {
            emit(IoResult.error<T>("Null data: ${response.message}"))
        }
    } else if (response.status == IoResult.Status.ERROR) {
        emit(IoResult.networkError<T>(response.message))
        emitSource(source)
    }
}
