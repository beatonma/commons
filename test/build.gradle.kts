plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
}

dependencies {
    implementation(Dependencies.Test.MOCKITO)
    implementation(Dependencies.Test.AndroidX.Espresso.CORE)

    implementation(Dependencies.Test.AndroidX.RULES)
    implementation(Dependencies.Test.AndroidX.RUNNER)

    implementation(Dependencies.AndroidX.ANNOTATIONS)
    implementation(Dependencies.AndroidX.APPCOMPAT)
    implementation(Dependencies.AndroidX.CORE_KTX)

    implementation(Dependencies.Kotlin.Coroutines.CORE)
    implementation(Dependencies.Kotlin.REFLECT)
}
