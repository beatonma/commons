import org.beatonma.commons.buildsrc.gradle.main

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_COMPOSE_MODULE)
}

dependencies {
    main {
        implementations(
            Dependencies.Jetpack.Compose.TEST,
            Dependencies.Jetpack.Compose.TEST_JUNIT
        )
    }
}
