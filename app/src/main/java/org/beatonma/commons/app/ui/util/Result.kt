package org.beatonma.commons.app.ui.util

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.beatonma.commons.app.ui.components.ErrorUi
import org.beatonma.commons.app.ui.components.LoadingIcon
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.ResponseCode
import org.beatonma.commons.repo.result.onError
import org.beatonma.commons.repo.result.onErrorCode
import org.beatonma.commons.repo.result.onLoading
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.repo.result.onSuccessCode
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

private const val TAG = "Compose.Result"
private typealias OnError = @Composable (error: Throwable?, code: ResponseCode?) -> Unit

@Composable
fun <T> WithResultData(
    result: IoResult<T>,
    onLoading: @Composable () -> Unit = defaultLoading,
    onError: OnError = defaultOnError,
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
    onLoading: @Composable () -> Unit = defaultLoading,
    onError: OnError = defaultOnError,
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


private val defaultOnError: OnError
    get() = { error, code ->
        val modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()

        when {
            error != null -> ErrorUi(error = error, modifier = modifier)
            code != null -> ErrorUi(error = code, modifier = modifier)
        }
    }

private val defaultLoading: @Composable () -> Unit
    get() = {
        LoadingIcon(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding())
    }
