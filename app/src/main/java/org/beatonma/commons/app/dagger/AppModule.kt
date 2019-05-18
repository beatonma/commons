package org.beatonma.commons.app.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import org.beatonma.commons.CommonsApplication
import javax.inject.Singleton

@Module
class AppModule(val application: CommonsApplication) {
    @Provides
    @Singleton
    fun providesContext(): Context = application.applicationContext
}
