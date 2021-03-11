import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
}

dependencies {
    main {
        implementations(
            Dependencies.Test.JUNIT,
            Dependencies.Test.AndroidX.CORE,
            Dependencies.Test.MOCKITO,
            Dependencies.Test.AndroidX.Espresso.CORE,
            Dependencies.Test.AndroidX.RULES,
            Dependencies.Test.AndroidX.RUNNER,
            Dependencies.AndroidX.ANNOTATIONS,
            Dependencies.AndroidX.APPCOMPAT,
            Dependencies.AndroidX.CORE_KTX,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.Kotlin.Coroutines.TEST,
            Dependencies.Kotlin.REFLECT
        )
    }
}
