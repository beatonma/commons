import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.kts.extensions.buildConfigStrings
import org.beatonma.commons.buildsrc.kts.extensions.coroutines
import org.beatonma.commons.buildsrc.kts.extensions.instrumentationTest
import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
}

android {
    defaultConfig {
        testInstrumentationRunner = "org.beatonma.commons.repo.androidTest.HiltTestRunner"

        buildConfigStrings(
            "APPLICATION_ID" to Commons.APPLICATION_ID
        )
    }
}

dependencies {
    instrumentationTest {
        annotationProcessors(
            Dependencies.Dagger.COMPILER,
            Dependencies.Dagger.ANNOTATION_PROCESSOR
        )

        implementations(
            project(":test"),
            Dependencies.Hilt.TESTING,
            Dependencies.Room.RUNTIME,
            Dependencies.Test.AndroidX.LIVEDATA,
            Dependencies.Test.AndroidX.RUNNER
        )
    }

    main {
        implementations(
            *coroutines,
            Dependencies.AndroidX.CORE_KTX,
            Dependencies.Hilt.LIFECYCLE_VIEWMODEL,
            Dependencies.Retrofit.Converter.MOSHI,

            project(":core"),
            project(":network-core"),
            project(":persistence"),
            project(":snommoc")
        )
    }
}
