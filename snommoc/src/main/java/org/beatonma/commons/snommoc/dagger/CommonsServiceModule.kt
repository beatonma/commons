package org.beatonma.commons.snommoc.dagger

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import org.beatonma.commons.network.core.Http
import org.beatonma.commons.snommoc.BuildConfig
import org.beatonma.commons.snommoc.CommonsService
import org.beatonma.commons.snommoc.Contract
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import withDefaultHeaders
import javax.inject.Singleton


@Module @Suppress("unused")
@InstallIn(ApplicationComponent::class)
class CommonsServiceModule {
    @Singleton
    @Provides
    @SnommocClient
    fun provideCommonsHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.withDefaultHeaders { requestBuilder, originalUrl ->
                requestBuilder.addHeader(Http.Header.CONTENT_TYPE, Http.ContentType.JSON)
                requestBuilder.url(originalUrl.newBuilder().apply {
                    addQueryParameter(Contract.API_KEY, BuildConfig.COMMONS_API_KEY)
                }.build())
            }
        }.build()

    @Singleton
    @Provides
    @SnommocClient
    fun provideCommonsRetrofit(
        moshi: Moshi,
        @SnommocClient httpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(CommonsService.BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Singleton
    @Provides
    fun providesCommonsService(@SnommocClient retrofit: Retrofit): CommonsService =
        retrofit.create(CommonsService::class.java)
}
