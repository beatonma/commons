package org.beatonma.commons.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers

fun <T, N> resultLiveData(
    databaseQuery: suspend () -> LiveData<T>,
    networkCall: suspend () -> Result<N>,
    saveCallResult: suspend (N) -> Unit
): LiveData<Result<T>> = liveData(Dispatchers.IO) {
    emit(Result.loading<T>())
    val source = databaseQuery.invoke().map { Result.success(it, message = "DB read OK ") }
    emitSource(source)

    val response = networkCall.invoke()
    if (response.status == Result.Status.SUCCESS) {
        if (response.data != null) {
            saveCallResult(response.data)
        }
        else {
            emit(Result.error<T>("Null data: ${response.message}"))
        }
    } else if (response.status == Result.Status.ERROR) {
        emit(Result.error<T>("Network error: ${response.message}"))
        emitSource(source)
    }
}
