plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(Commons.Sdk.COMPILE)
    defaultConfig {
        applicationId = Commons.APPLICATION_ID
        versionCode = Commons.VERSION_CODE
        versionName = Commons.VERSION_NAME

        minSdkVersion(Commons.Sdk.MIN)
        targetSdkVersion(Commons.Sdk.TARGET)

        val api_twfy: String by project
        buildConfigField("String", "TWFY_API_KEY", api_twfy)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
            rootProject.file("proguard").listFiles()
                ?.filter { it.name.startsWith("proguard") }
                ?.toTypedArray()
                ?.let { proguardFiles(*it) }
        }
    }
    compileOptions {
        sourceCompatibility = Versions.JAVA
        targetCompatibility = Versions.JAVA
    }
    dataBinding.isEnabled = true
}

dependencies {
    // Unit tests
    testImplementation(Dependencies.Test.BMA_TESTING)
    testImplementation(Dependencies.Test.JUNIT)
    testImplementation(Dependencies.Test.MOCKITO)
    testImplementation(Dependencies.Kotlin.REFLECT)

    // Instrumentation tests
    androidTestImplementation(Dependencies.Test.BMA_TESTING)
    androidTestImplementation(Dependencies.Test.ANDROIDX_TEST_CORE)
    androidTestImplementation(Dependencies.Test.ANDROIDX_TEST_RUNNER)
    androidTestImplementation(Dependencies.Test.ANDROIDX_TEST_ESPRESSO)

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(Dependencies.Kotlin.STDLIB)
    implementation(Dependencies.Kotlin.COROUTINES_CORE)
    implementation(Dependencies.Kotlin.COROUTINES_ANDROID)

    // Data
    kapt(Dependencies.Dagger.COMPILER)
    kapt(Dependencies.Dagger.AP)
    implementation(Dependencies.Dagger.DAGGER)
    implementation(Dependencies.Dagger.SUPPORT)

    kapt(Dependencies.Room.AP)
    implementation(Dependencies.Room.RUNTIME)
    implementation(Dependencies.Room.KTX)

    implementation(Dependencies.Retrofit.RETROFIT)
    implementation(Dependencies.Retrofit.RETROFIT_CONVERTER_GSON)

    // App
    implementation(Dependencies.AndroidX.APPCOMPAT)
    implementation(Dependencies.AndroidX.CONSTRAINTLAYOUT)
    implementation(Dependencies.AndroidX.CORE_KTX)
    implementation(Dependencies.AndroidX.LIFECYCLE_EXT)

    implementation(Dependencies.GLIDE)
    implementation(Dependencies.GOOGLE_MATERIAL)

    implementation(Dependencies.Bma.ACTIVITY)
    implementation(Dependencies.Bma.CORE)
    implementation(Dependencies.Bma.GRAPHIC_CORE)
    implementation(Dependencies.Bma.PAINTED_VIEW)
    implementation(Dependencies.Bma.RECYCLERVIEW)
    implementation(Dependencies.Bma.STYLE)
    implementation(Dependencies.Bma.UTIL)
}