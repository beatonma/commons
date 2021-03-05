import org.beatonma.commons.buildsrc.kts.extensions.instrumentationTest
import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_COMPOSE_MODULE)
}

dependencies {
    instrumentationTest {
        implementations(
            project(Modules.TestCompose)
        )
    }
    
    main {
        implementations(
            Dependencies.AndroidX.CORE_KTX,
            Dependencies.AndroidX.CONSTRAINTLAYOUT_COMPOSE,
            project(Modules.Core),
            project(Modules.Theme)
        )
    }
}
