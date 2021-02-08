import org.beatonma.commons.buildsrc.kts.extensions.main
import org.beatonma.commons.buildsrc.kts.extensions.unitTest

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
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
            Dependencies.AndroidX.CORE_KTX,

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
