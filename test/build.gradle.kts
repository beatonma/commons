plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Commons.Sdk.COMPILE)
    defaultConfig {
        versionCode = Git.commitCount(project)
        versionName = Git.tag(project)

        minSdkVersion(Commons.Sdk.MIN)
        targetSdkVersion(Commons.Sdk.TARGET)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    compileOptions {
        sourceCompatibility = Versions.JAVA
        targetCompatibility = Versions.JAVA
    }
}

dependencies {
    implementation(Dependencies.Test.AndroidX.CORE)
    implementation(Dependencies.Test.JUNIT)
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
