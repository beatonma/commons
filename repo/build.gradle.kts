import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.kts.extensions.coroutines
import org.beatonma.commons.buildsrc.kts.extensions.injectStrings

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
}

android {
    defaultConfig {
        testInstrumentationRunner = "org.beatonma.commons.repo.androidTest.HiltTestRunner"

        injectStrings(
            "APPLICATION_ID" to Commons.APPLICATION_ID,
            asBuildConfig = true,
            asResValue = false
        )
    }
}

dependencies {
    val testAnnotationProcessors = arrayOf(
        Dependencies.Dagger.COMPILER,
        Dependencies.Dagger.ANNOTATION_PROCESSOR
    )
    val androidTestImplementations = arrayOf(
        project(":test"),
        Dependencies.Hilt.TESTING,
        Dependencies.Room.RUNTIME,
        Dependencies.Test.AndroidX.LIVEDATA,
        Dependencies.Test.AndroidX.RUNNER
    )

    val implementations = arrayOf(
        Dependencies.AndroidX.CORE_KTX,

        Dependencies.Hilt.LIFECYCLE_VIEWMODEL,

        *coroutines,

        Dependencies.Retrofit.Converter.MOSHI,

        project(":core"),
        project(":network-core"),
        project(":data"),
        project(":snommoc")
    )

    testAnnotationProcessors.forEach(::kaptAndroidTest)
    androidTestImplementations.forEach(::androidTestImplementation)

    implementations.forEach(::implementation)
}
