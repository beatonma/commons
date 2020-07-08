package org.beatonma.commons.app.dagger

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module @InstallIn(ApplicationComponent::class)
class LocationModule {
    @SuppressLint("MissingPermission")
    @Singleton
    @Provides
    fun providesLocationProvider(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun providesGeocoder(@ApplicationContext context: Context) = Geocoder(context)
}
