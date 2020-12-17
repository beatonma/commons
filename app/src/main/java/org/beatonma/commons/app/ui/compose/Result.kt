package org.beatonma.commons.app.ui.compose

import androidx.compose.runtime.Composable
import org.beatonma.commons.app.ui.compose.components.Loading
import org.beatonma.commons.app.ui.compose.components.Warning
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.onError
import org.beatonma.commons.repo.result.onLoading
import org.beatonma.commons.repo.result.onSuccess


@Composable
fun <T> WithResultData(
    result: IoResult<T>,
    onLoading: @Composable () -> Unit = { Loading() },
    onError: @Composable (error: Throwable) -> Unit = { Warning(error = it) },
    onSuccess: @Composable (data: T) -> Unit,
) {
    result
        .onError { onError(it) }
        .onLoading { onLoading() }
        .onSuccess { onSuccess(it) }
}
