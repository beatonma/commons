import org.beatonma.commons.buildsrc.kts.extensions.coroutines
import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
}

dependencies {
    main {
        implementations(
            *coroutines,
            Dependencies.AndroidX.CORE_KTX
        )
    }
}
