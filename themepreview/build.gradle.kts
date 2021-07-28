import org.beatonma.commons.buildsrc.gradle.main

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_COMPOSE_MODULE)
}

dependencies {
    main {
        implementations(
            Dependencies.Jetpack.CORE_KTX,
            Dependencies.Jetpack.Compose.ANIMATION,
            Dependencies.Jetpack.Compose.MATERIAL_ICONS_CORE,
            Dependencies.Jetpack.Compose.MATERIAL_ICONS_EXTENDED,
            Dependencies.Jetpack.Compose.RUNTIME
        )
    }
}
