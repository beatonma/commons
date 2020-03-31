package org.beatonma.commons.network

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.beatonma.commons.BuildConfig.*
import org.beatonma.commons.network.dagger.WebService
import org.beatonma.lib.util.kotlin.extensions.autotag
import javax.inject.Named
import javax.inject.Singleton
import android.os.Build as Device

internal object HttpHeader {
    const val USER_AGENT = "User-Agent"
    const val CONTENT_TYPE = "Content-Type"
}

internal object HttpContentType {
    const val JSON = "application/json"
}

internal object CommonsParam {
    const val API_KEY = "key"
}

internal object TwfyParam {
    const val API_KEY = "key"
    const val OUTPUT_FORMAT = "output"
}

private val userAgent: String by lazy {
    "$USER_AGENT_APP/$VERSION_NAME ($USER_AGENT_WEBSITE $USER_AGENT_EMAIL) Android[${Device.MANUFACTURER}/${Device.PRODUCT}] ${Device.VERSION.RELEASE}/${Device.VERSION.SDK_INT}/${APPLICATION_ID}v$VERSION_CODE"
}

private inline fun Interceptor.Chain.withDefaultHeaders(block: (Request.Builder) -> Unit): Response {
    return proceed(request().newBuilder().apply {
        header(HttpHeader.USER_AGENT, userAgent)
        block(this)
    }.build())
}

@Module
class HttpClientModule {
    @Singleton
    @Provides
    @Named(WebService.GENERIC)
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain -> chain.withDefaultHeaders { } }.build()

    @Singleton
    @Provides
    @Named(WebService.COMMONS)
    fun provideCommonsHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.withDefaultHeaders { requestBuilder ->
                requestBuilder.addHeader(HttpHeader.CONTENT_TYPE, HttpContentType.JSON)
                val originalUrl = chain.request().url()
                Log.d(autotag, "Request: $originalUrl")
                requestBuilder.url(originalUrl.newBuilder().apply {
                    addQueryParameter(CommonsParam.API_KEY, COMMONS_API_KEY)
                }.build())
            }
        }.build()

    @Singleton
    @Provides
    @Named(WebService.TWFY)
    fun provideTwfyHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor{ chain ->
            chain.withDefaultHeaders { requestBuilder ->
                val originalUrl = chain.request().url()
                requestBuilder.url(originalUrl.newBuilder().apply {
                    addQueryParameter(TwfyParam.API_KEY, TWFY_API_KEY)
                    addQueryParameter(TwfyParam.OUTPUT_FORMAT, "js")
                }.build())
            }
        }.build()
}