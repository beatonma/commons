import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_COMPOSE_MODULE)
}

dependencies {
    main {
        implementations(
            Dependencies.AndroidX.CORE_KTX,
            Dependencies.AndroidX.Compose.ANIMATION,
            Dependencies.AndroidX.Compose.MATERIAL_ICONS_CORE,
            Dependencies.AndroidX.Compose.MATERIAL_ICONS_EXTENDED,
            Dependencies.AndroidX.Compose.RUNTIME
        )
    }
}
