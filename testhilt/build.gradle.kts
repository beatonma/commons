import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
}

dependencies {
    main {
        implementations(
            Dependencies.Test.AndroidX.RUNNER,
            Dependencies.Dagger.Hilt.TESTING
        )
    }
}
