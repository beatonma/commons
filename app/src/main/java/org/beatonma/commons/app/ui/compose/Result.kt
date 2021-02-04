package org.beatonma.commons.app.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.beatonma.commons.app.ui.compose.components.ErrorUi
import org.beatonma.commons.app.ui.compose.components.LoadingIcon
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.ResponseCode
import org.beatonma.commons.repo.result.onError
import org.beatonma.commons.repo.result.onErrorCode
import org.beatonma.commons.repo.result.onLoading
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.repo.result.onSuccessCode

private const val TAG = "Compose.Result"

@Composable
fun <T> WithResultData(
    result: IoResult<T>,
    onLoading: @Composable () -> Unit = { LoadingIcon(Modifier.fillMaxWidth()) },
    onError: @Composable (error: Throwable?, code: ResponseCode?) -> Unit =
        { error, code ->
            when {
                error != null -> ErrorUi(error = error)
                code != null -> ErrorUi(error = code)
            }
        },
    onSuccess: @Composable (data: T) -> Unit,
) {
    result
        .onError { error ->
            Log.w(TAG, error)
            onError(error, ResponseCode(418))
        }
        .onErrorCode { code ->
            Log.w(TAG, "$code")
            onError(null, code)
        }
        .onLoading { onLoading() }
        .onSuccess { onSuccess(it) }
}

@Composable
fun <T> WithResponseCode(
    result: IoResult<T>,
    onLoading: @Composable () -> Unit = { LoadingIcon(Modifier.fillMaxWidth()) },
    onError: @Composable (error: Throwable?, code: ResponseCode?) -> Unit =
        { error, code ->
            when {
                error != null -> ErrorUi(error = error)
                code != null -> ErrorUi(error = code)
            }
        },
    onSuccess: @Composable (ResponseCode) -> Unit,
) {
    result
        .onError { error ->
            Log.w(TAG, error)
            onError(error, ResponseCode(418))
        }
        .onErrorCode { code ->
            Log.w(TAG, "$code")
            onError(null, code)
        }
        .onLoading { onLoading() }
        .onSuccessCode { onSuccess(it) }
}
