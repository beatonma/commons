package org.beatonma.commons.data.core.dagger

import dagger.Component
import org.beatonma.commons.data.CommonsDatabase

@Component(
    modules = [
        CommonsCoreModule::class
    ]
)
interface CommonsCoreComponent {
    fun commonsDataSource(): CommonsDatabase
}
