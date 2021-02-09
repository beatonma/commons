package org.beatonma.commons.ukparliament.dagger

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.beatonma.commons.ukparliament.converters.NestedPayloadFactory
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
class UkParliamentMoshiModule {
    @Singleton
    @Provides
    @UkParliamentLdaClient
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(NestedPayloadFactory())
            .build()
}
