package org.beatonma.commons.app.ui.compose

import androidx.compose.runtime.Composable
import org.beatonma.commons.app.ui.compose.components.Loading
import org.beatonma.commons.app.ui.compose.components.Warning
import org.beatonma.commons.repo.result.IoError
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.ThrowableResult
import org.beatonma.commons.repo.result.isError
import org.beatonma.commons.repo.result.isLoading
import org.beatonma.commons.repo.result.isSuccess
import org.beatonma.commons.repo.result.onError
import org.beatonma.commons.repo.result.onLoading
import org.beatonma.commons.repo.result.onSuccess

@Composable
fun <T> WithResultData(
    result: IoResult<T>,
    onLoading: @Composable () -> Unit = { Loading() },
    onError: @Composable (ioError: IoError<T, *>) -> Unit = { Warning(error = it.error) },
    onSuccess: @Composable (result: IoResult<T>, data: T) -> Unit,
) {
    when {
        result.isError -> {
            onError(result as IoError<T, *>)
        }

        result.isLoading -> {
            onLoading()
        }

        result.isSuccess -> {
            val data = result.data
            if (data == null) {
                onError(result as IoError<T, *>)
            }
            else {
                onSuccess(result, data)
            }
        }
    }
}

@Composable
fun <T> WithResultData(
    result: ThrowableResult<T>,
    onLoading: @Composable () -> Unit = { Loading() },
    onError: @Composable (error: Throwable) -> Unit = { Warning(error = it) },
    onSuccess: @Composable (data: T) -> Unit,
) {
    result
        .onError { onError(it) }
        .onLoading { onLoading() }
        .onSuccess { onSuccess(it) }
}
