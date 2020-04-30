package org.beatonma.commons.network.dagger

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import org.beatonma.commons.network.retrofit.converters.DateAdapter
import org.beatonma.commons.network.retrofit.converters.DeEnvelopeFactory
import java.util.*
import javax.inject.Singleton

@Module
class MoshiModule {
    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(DeEnvelopeFactory())
            .add(Date::class.java, DateAdapter())
            .build()
}
