import org.beatonma.commons.buildsrc.gradle.buildConfigStrings
import org.beatonma.commons.buildsrc.gradle.instrumentationTest
import org.beatonma.commons.buildsrc.gradle.main
import org.beatonma.commons.buildsrc.gradle.project

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_HILT_MODULE)
}

android {
    defaultConfig {
        testInstrumentationRunner = "org.beatonma.commons.testhilt.HiltTestRunner"

        buildConfigStrings(
            "APPLICATION_ID" to org.beatonma.commons.buildsrc.config.Commons.APPLICATION_ID
        )
    }
}

dependencies {
    instrumentationTest {
        annotationProcessors(
            Dependencies.Dagger.AP_COMPILER,
            Dependencies.Dagger.AP_ANDROID,
        )

        implementations(
            project(Modules.Test),
            project(Modules.TestHilt),
            project(Modules.SampleData),
            Dependencies.Dagger.Hilt.TESTING,
            Dependencies.Room.RUNTIME,
            Dependencies.Test.Jetpack.ARCH_CORE,
            Dependencies.Test.Jetpack.RUNNER,
        )
    }


    main {
        implementations(
            Dependencies.Kotlin.Coroutines.ANDROID,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.Jetpack.CORE_KTX,
            Dependencies.Retrofit.Converter.MOSHI,

            project(Modules.Core),
            project(Modules.NetworkCore),
            project(Modules.Persistence),
            project(Modules.Snommoc),
            project(Modules.UkParliament),
        )
    }
}
