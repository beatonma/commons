import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
}

dependencies {
    main {
        implementations(
            project(Modules.Core),
            project(Modules.Persistence),
            project(Modules.Repository),
            project(Modules.Snommoc)
        )
    }
}
