import org.beatonma.commons.buildsrc.gradle.main
import org.beatonma.commons.buildsrc.gradle.project

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_COMPOSE_MODULE)
}

dependencies {
    main {
        implementations(
            Dependencies.Kotlin.STDLIB,
            Dependencies.Jetpack.CORE_KTX,
            Dependencies.Jetpack.ACTIVITY_COMPOSE,
            Dependencies.Accompanist.INSETS,
            project(Modules.Core),
            project(Modules.ThemeCore),
        )
    }
}
