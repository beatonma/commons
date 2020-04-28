package org.beatonma.commons.app.dagger

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocationModule {
    @SuppressLint("MissingPermission")
    @Singleton
    @Provides
    fun providesLocationProvider(context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Singleton
    @Provides
    fun providesGeocoder(context: Context) = Geocoder(context)
}
