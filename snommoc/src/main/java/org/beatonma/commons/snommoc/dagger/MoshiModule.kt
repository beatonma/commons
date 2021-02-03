package org.beatonma.commons.snommoc.dagger

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.beatonma.commons.core.House
import org.beatonma.commons.snommoc.converters.DateAdapter
import org.beatonma.commons.snommoc.converters.DateTimeAdapter
import org.beatonma.commons.snommoc.converters.DeEnvelopeFactory
import org.beatonma.commons.snommoc.converters.HouseAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Singleton

@Module @InstallIn(ApplicationComponent::class)
class MoshiModule {
    @Singleton
    @Provides
    @SnommocClient
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(DeEnvelopeFactory())
            .add(LocalDate::class.java, DateAdapter())
            .add(LocalDateTime::class.java, DateTimeAdapter())
            .add(House::class.java, HouseAdapter())
            .build()
}
