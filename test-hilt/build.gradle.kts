import org.beatonma.commons.buildsrc.gradle.main

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_HILT_MODULE)
}

dependencies {
    main {
        implementations(
            Dependencies.Test.Jetpack.RUNNER,
            Dependencies.Dagger.Hilt.TESTING
        )
    }
}
