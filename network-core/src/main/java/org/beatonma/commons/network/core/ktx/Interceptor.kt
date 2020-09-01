import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.beatonma.commons.network.core.Http
import org.beatonma.commons.network.core.userAgent

inline fun Interceptor.Chain.withDefaultHeaders(block: (Request.Builder, HttpUrl) -> Unit): Response {
    return proceed(request().newBuilder().apply {
        header(Http.Header.USER_AGENT, userAgent)
        val originalUrl = request().url()
        Log.d(javaClass.simpleName, "Request: $originalUrl")
        block(this, originalUrl)
    }.build())
}
