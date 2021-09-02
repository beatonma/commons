import org.beatonma.commons.buildsrc.gradle.buildConfigStrings
import org.beatonma.commons.buildsrc.gradle.instrumentationTest
import org.beatonma.commons.buildsrc.gradle.main
import org.beatonma.commons.buildsrc.gradle.project

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_HILT_MODULE)
    id(Plugins.Commons.COMMONS_ROOM_MODULE)
    id(Plugins.Kotlin.PARCELIZE)
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
        implementations(
            Dependencies.Test.Jetpack.ARCH_CORE,
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
