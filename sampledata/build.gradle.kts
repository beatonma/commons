import org.beatonma.commons.buildsrc.gradle.main

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
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
