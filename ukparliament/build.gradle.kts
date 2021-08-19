import org.beatonma.commons.buildsrc.gradle.main
import org.beatonma.commons.buildsrc.gradle.project
import org.beatonma.commons.buildsrc.gradle.unitTest

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_HILT_MODULE)
}

android {
    defaultConfig {
        testInstrumentationRunner = "org.beatonma.commons.testhilt.HiltTestRunner"
    }
}

dependencies {
    unitTest {
        implementations(
            Dependencies.Test.OKHTTP_MOCK_SERVER
        )
    }

    main {
        implementations(
            Dependencies.Jetpack.CORE_KTX,

            Dependencies.Kotlin.Coroutines.ANDROID,
            Dependencies.Kotlin.Coroutines.CORE,

            Dependencies.Retrofit.RETROFIT,
            Dependencies.Retrofit.Converter.MOSHI,
            Dependencies.Retrofit.Converter.TEXT,

            project(Modules.Core),
            project(Modules.NetworkCore)
        )
    }
}
