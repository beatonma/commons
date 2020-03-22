package org.beatonma.commons.network.dagger

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.beatonma.commons.network.retrofit.CommonsService
import org.beatonma.commons.network.retrofit.TwfyService
import org.beatonma.commons.network.retrofit.WikiService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {
    @Singleton
    @Provides
    @Named(WebService.COMMONS)
    fun provideCommonsRetrofit(
        moshi: Moshi,
        @Named(WebService.COMMONS) httpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(CommonsService.ENDPOINT)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Singleton
    @Provides
    @Named(WebService.TWFY)
    fun provideTwfyRetrofit(@Named(WebService.TWFY) httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(TwfyService.ENDPOINT)
            .build()

    @Singleton
    @Provides
    @Named(WebService.GENERIC)
    fun provideGenericRetrofit(@Named(WebService.GENERIC) httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(CommonsService.ENDPOINT)  // Service uses complete URLs so this is ignored.
            .build()

    @Singleton
    @Provides
    @Named(WebService.WIKIPEDIA)
    fun provideWikipediaRetrofit(@Named(WebService.GENERIC) httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(WikiService.ENDPOINT)
            .build()
}
