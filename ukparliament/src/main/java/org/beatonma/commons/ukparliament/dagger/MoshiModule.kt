package org.beatonma.commons.ukparliament.dagger

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module @InstallIn(ApplicationComponent::class)
class MoshiModule {
    @Singleton
    @Provides
    @UkParliamentLdaClient
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .build()
}
