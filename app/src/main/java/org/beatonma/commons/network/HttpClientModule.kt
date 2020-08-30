package org.beatonma.commons.network

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.*
import org.beatonma.commons.BuildConfig.*
import org.beatonma.commons.kotlin.extensions.autotag
import org.beatonma.commons.network.dagger.WebService
import javax.inject.Named
import javax.inject.Singleton
import android.os.Build as Device

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

private inline fun Interceptor.Chain.withDefaultHeaders(block: (Request.Builder, HttpUrl) -> Unit): Response {
    return proceed(request().newBuilder().apply {
        header(Http.Header.USER_AGENT, userAgent)
        val originalUrl = request().url()
        Log.d(autotag, "Request: $originalUrl")
        block(this, originalUrl)
    }.build())
}

@Module @InstallIn(ApplicationComponent::class)
class HttpClientModule {
    @Singleton
    @Provides
    @Named(WebService.GENERIC)
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.withDefaultHeaders { _, _ ->

            }
        }.build()

    @Singleton
    @Provides
    @Named(WebService.COMMONS)
    fun provideCommonsHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.withDefaultHeaders { requestBuilder, originalUrl ->
                requestBuilder.addHeader(Http.Header.CONTENT_TYPE, Http.ContentType.JSON)
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
            chain.withDefaultHeaders { requestBuilder, originalUrl ->
                requestBuilder.url(originalUrl.newBuilder().apply {
                    addQueryParameter(TwfyParam.API_KEY, TWFY_API_KEY)
                    addQueryParameter(TwfyParam.OUTPUT_FORMAT, "js")
                }.build())
            }
        }.build()
}
