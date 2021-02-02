package org.beatonma.commons.ukparliament.dagger

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import org.beatonma.commons.network.core.Http
import org.beatonma.commons.network.core.ktx.withDefaultHeaders
import org.beatonma.commons.ukparliament.api.UkParliamentService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class UkParliamentServiceModule {
    @Singleton
    @Provides
    @UkParliamentLdaClient
    fun providesUkParliamentLdaHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.withDefaultHeaders { requestBuilder, _ ->
                    requestBuilder.addHeader(Http.Header.CONTENT_TYPE, Http.ContentType.JSON)
                }
            }.build()

    @Singleton
    @Provides
    @UkParliamentLdaClient
    fun providesUkParliamentLdaRetrofit(
        @UkParliamentLdaClient moshi: Moshi,
        @UkParliamentLdaClient httpClient: OkHttpClient,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(UkParliamentService.BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Singleton
    @Provides
    fun providesUkParliamentService(@UkParliamentLdaClient retrofit: Retrofit): UkParliamentService =
        retrofit.create(UkParliamentService::class.java)
}
