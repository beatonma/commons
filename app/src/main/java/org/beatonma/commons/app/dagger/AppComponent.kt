package org.beatonma.commons.app.dagger

import android.content.Context
import dagger.Component
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.data.CommonsDatabase
import org.beatonma.commons.data.core.dagger.CommonsCoreModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, CommonsCoreModule::class])
interface AppComponent {
    fun context(): Context
    fun commonsDb(): CommonsDatabase
    fun inject(app: CommonsApplication)
}
