package org.beatonma.commons.network

import android.os.Build
import android.os.Build.MANUFACTURER
import android.os.Build.PRODUCT
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.beatonma.commons.BuildConfig.*
import javax.inject.Named
import javax.inject.Singleton

internal const val HTTP_HEADER_USER_AGENT = "User-Agent"
internal const val TWFY_PARAM_API_KEY = "key"
internal const val TWFY_PARAM_OUTPUT_FORMAT = "output"

@Module
class HttpClientModule {
    companion object {
        const val DEFAULT = "DefaultHttpClient"
        const val TWFY = "TwfyHttpClient"
    }

    private val userAgent: String by lazy {
        "$USER_AGENT_APP/$VERSION_NAME ($USER_AGENT_WEBSITE $USER_AGENT_EMAIL) Android[$MANUFACTURER/$PRODUCT] ${Build.VERSION.RELEASE}/${Build.VERSION.SDK_INT}/${APPLICATION_ID}v$VERSION_CODE"
    }

    private inline fun Interceptor.Chain.withDefaultHeaders(block: (Request.Builder) -> Unit): Response {
        return proceed(request().newBuilder().apply {
            header(HTTP_HEADER_USER_AGENT, userAgent)
            block(this)
        }.build())
    }

    @Singleton
    @Provides
    @Named(DEFAULT)
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain -> chain.withDefaultHeaders { } }.build()

    @Singleton
    @Provides
    @Named(TWFY)
    fun provideTwfyHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor{ chain ->
            chain.withDefaultHeaders { requestBuilder ->
                val originalUrl = chain.request().url()
                requestBuilder.url(originalUrl.newBuilder().apply {
                    addQueryParameter(TWFY_PARAM_API_KEY, TWFY_API_KEY)
                    addQueryParameter(TWFY_PARAM_OUTPUT_FORMAT, "js")
                }.build())
            }
        }.build()
}
