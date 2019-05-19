package org.beatonma.commons.network.dagger

import dagger.Module
import dagger.Provides
import org.beatonma.commons.network.retrofit.CommonsService
import org.beatonma.commons.network.retrofit.TwfyService
import org.beatonma.commons.network.retrofit.WikiService
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkServiceModule {
    @Singleton
    @Provides
    fun provideWikipediaNetworkService(@Named(WebServices.WIKIPEDIA) retrofit: Retrofit): WikiService =
        retrofit.create(WikiService::class.java)

    @Singleton
    @Provides
    fun provideCommonsNetworkService(@Named(WebServices.COMMONS) retrofit: Retrofit): CommonsService =
        retrofit.create(CommonsService::class.java)

    @Singleton
    @Provides
    fun provideTwfyNetworkService(@Named(WebServices.TWFY) retrofit: Retrofit): TwfyService =
        retrofit.create(TwfyService::class.java)
}
