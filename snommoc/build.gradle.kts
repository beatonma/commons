import org.beatonma.commons.buildsrc.config.Commons
import org.beatonma.commons.buildsrc.gradle.buildConfigStrings
import org.beatonma.commons.buildsrc.gradle.injectStrings
import org.beatonma.commons.buildsrc.gradle.instrumentationTest
import org.beatonma.commons.buildsrc.gradle.main
import org.beatonma.commons.buildsrc.gradle.project
import org.beatonma.commons.buildsrc.gradle.unitTest
import org.beatonma.commons.buildsrc.local.LocalConfig

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_HILT_MODULE)
    id(Plugins.Kotlin.PARCELIZE)
}

android {
    defaultConfig {
        testInstrumentationRunner = "org.beatonma.commons.testhilt.HiltTestRunner"

        buildConfigStrings(
            "COMMONS_API_KEY" to LocalConfig.Api.Commons.API_KEY,
        )

        injectStrings(
            "BASE_URL" to Commons.BASE_URL,
            asBuildConfig = true,
            asResValue = true,
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
            Dependencies.Dagger.Hilt.TESTING,
            Dependencies.Test.Jetpack.RUNNER,
        )
    }

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
