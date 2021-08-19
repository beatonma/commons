import org.beatonma.commons.buildsrc.gradle.main

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
}

dependencies {
    main {
        implementations(
            Dependencies.Test.JUNIT,
            Dependencies.Test.Jetpack.CORE,
            Dependencies.Test.MOCKITO,
            Dependencies.Test.Jetpack.Espresso.CORE,
            Dependencies.Test.Jetpack.RULES,
            Dependencies.Test.Jetpack.RUNNER,
            Dependencies.Jetpack.ANNOTATIONS,
            Dependencies.Jetpack.APPCOMPAT,
            Dependencies.Jetpack.CORE_KTX,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.Kotlin.Coroutines.TEST,
            Dependencies.Kotlin.REFLECT
        )
    }
}
