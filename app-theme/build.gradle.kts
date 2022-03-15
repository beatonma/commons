import org.beatonma.commons.buildsrc.gradle.main
import org.beatonma.commons.buildsrc.gradle.project

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_COMPOSE_MODULE)
}

dependencies {
    main {
        implementations(
            Dependencies.Jetpack.Compose.MATERIAL_ICONS_CORE,
            Dependencies.Jetpack.Compose.MATERIAL_ICONS_EXTENDED,
            project(Modules.ComposeCore),
            project(Modules.ThemeCore),
            project(Modules.Core),
        )
    }
}
