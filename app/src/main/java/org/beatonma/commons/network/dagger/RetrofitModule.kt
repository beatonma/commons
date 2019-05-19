package org.beatonma.commons.network.dagger

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.beatonma.commons.network.retrofit.CommonsService
import org.beatonma.commons.network.retrofit.TwfyService
import org.beatonma.commons.network.retrofit.WikiService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class RetrofitModule {
    @Singleton
    @Provides
    @Named(WebServices.COMMONS)
    fun provideCommonsRetrofit(@Named(WebServices.DEFAULT) httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(CommonsService.ENDPOINT)
            .build()

    @Singleton
    @Provides
    @Named(WebServices.WIKIPEDIA)
    fun provideWikipediaRetrofit(@Named(WebServices.DEFAULT) httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(WikiService.ENDPOINT)
            .build()

    @Singleton @Provides
    @Named(WebServices.TWFY)
    fun provideTwfyRetrofit(@Named(WebServices.TWFY) httpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .client(httpClient)
                .baseUrl(TwfyService.ENDPOINT)
                .build()

}
