import org.beatonma.commons.buildsrc.gradle.buildConfigStrings
import org.beatonma.commons.buildsrc.gradle.instrumentationTest
import org.beatonma.commons.buildsrc.gradle.main
import org.beatonma.commons.buildsrc.gradle.project

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_HILT_MODULE)
    id(Plugins.Commons.COMMONS_ROOM_MODULE)
}

android {
    defaultConfig {
        buildConfigStrings(
            "APPLICATION_ID" to org.beatonma.commons.buildsrc.config.Commons.APPLICATION_ID
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
            Dependencies.Dagger.AP_COMPILER,
            Dependencies.Dagger.AP_ANDROID
        )

        implementations(
            Dependencies.Test.Jetpack.LIVEDATA
        )
    }

    main {
        implementations(
            Dependencies.Kotlin.Coroutines.ANDROID,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.Jetpack.CORE_KTX,

            project(Modules.Core)
        )
    }
}
