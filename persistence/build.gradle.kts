import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.kts.extensions.buildConfigStrings
import org.beatonma.commons.buildsrc.kts.extensions.instrumentationTest
import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
    id(Plugins.COMMONS_ROOM_MODULE)
}

android {
    defaultConfig {
        buildConfigStrings(
            "APPLICATION_ID" to Commons.APPLICATION_ID
        )

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
                arg("room.expandProjection", "true")
            }
        }
    }
}

dependencies {
    instrumentationTest {
        annotationProcessors(
            Dependencies.Dagger.COMPILER,
            Dependencies.Dagger.ANNOTATION_PROCESSOR
        )

        implementations(
            Dependencies.Test.AndroidX.LIVEDATA
        )
    }

    main {
        implementations(
            Dependencies.Kotlin.Coroutines.ANDROID,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.AndroidX.CORE_KTX,

            project(Modules.Core)
        )
    }
}
