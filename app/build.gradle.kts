import local.LocalConfig

val kotlin_version: String by extra

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android-extensions")
    id("com.github.ben-manes.versions")
}
apply {
    plugin("kotlin-android")
    plugin("kotlin-android-extensions")
}


android {
    compileSdkVersion(Commons.Sdk.COMPILE)
    defaultConfig {
        applicationId = Commons.APPLICATION_ID
        versionCode = Git.commitCount(project)
        versionName = Git.tag(project)

        minSdkVersion(Commons.Sdk.MIN)
        targetSdkVersion(Commons.Sdk.TARGET)

        val buildConfigStrings = mapOf(
            "COMMONS_API_KEY" to LocalConfig.Api.Commons.API_KEY,
            "GIT_SHA" to Git.sha(project),
            "TWFY_API_KEY" to LocalConfig.Api.Twfy.API_KEY,
            "USER_AGENT_APP" to LocalConfig.UserAgent.NAME,
            "USER_AGENT_WEBSITE" to LocalConfig.UserAgent.WEBSITE,
            "USER_AGENT_EMAIL" to LocalConfig.UserAgent.EMAIL
        )

        buildConfigStrings.forEach { (key, value) ->
            buildConfigField("String", key, "\"$value\"")
        }

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
//    dataBinding.isEnabled = false
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
        }
        getByName("release") {
            isMinifyEnabled = true
//            postprocessing.apply {
//                isOptimizeCode = true
//                isObfuscate = true
//                isRemoveUnusedCode = true
//                isRemoveUnusedResources = true
//            }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Unit tests
    testImplementation(project(":bmalib:testing"))
    testImplementation(Dependencies.Test.JUNIT)
    testImplementation(Dependencies.Test.MOCKITO)
    testImplementation(Dependencies.Kotlin.REFLECT)
    testImplementation(Dependencies.Test.RETROFIT_MOCK)
    testImplementation(Dependencies.Dagger.DAGGER)
    testImplementation(Dependencies.Test.OKHTTP_MOCK_SERVER)
    kaptTest(Dependencies.Dagger.COMPILER)
    kaptTest(Dependencies.Dagger.ANNOTATION_PROCESSOR)

    // Instrumentation tests
    androidTestImplementation(project(":bmalib:testing"))
    androidTestImplementation(Dependencies.Test.AndroidX.CORE)
    androidTestImplementation(Dependencies.Test.AndroidX.RUNNER)
    androidTestImplementation(Dependencies.Test.AndroidX.ESPRESSO)

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation(Dependencies.Kotlin.STDLIB)
    implementation(Dependencies.Kotlin.Coroutines.CORE)
    implementation(Dependencies.Kotlin.Coroutines.ANDROID)

    // Data
    kapt(Dependencies.Dagger.COMPILER)
    kapt(Dependencies.Dagger.ANNOTATION_PROCESSOR)
    implementation(Dependencies.Dagger.DAGGER)
    implementation(Dependencies.Dagger.ANDROID)
    implementation(Dependencies.Dagger.SUPPORT)

    kapt(Dependencies.Room.AP)
    implementation(Dependencies.Room.RUNTIME)
    implementation(Dependencies.Room.KTX)

//    implementation(Dependencies.Moshi.MOSHI)
//    kapt(Dependencies.Moshi.KAPT_CODEGEN)

    implementation(Dependencies.Retrofit.RETROFIT)
    implementation(Dependencies.Retrofit.Converter.MOSHI)
    implementation(Dependencies.Retrofit.Converter.TEXT)

    // App
    implementation(Dependencies.AndroidX.APPCOMPAT)
    implementation(Dependencies.AndroidX.CONSTRAINTLAYOUT)
    implementation(Dependencies.AndroidX.CORE_KTX)
    implementation(Dependencies.AndroidX.LIFECYCLE_RUNTIME)
    implementation(Dependencies.AndroidX.LIVEDATA_KTX)
    implementation(Dependencies.AndroidX.VIEWMODEL_KTX)
    implementation(Dependencies.AndroidX.NAVIGATION_UI)
    implementation(Dependencies.AndroidX.NAVIGATION_FRAGMENT)

    implementation(Dependencies.Glide.CORE)
    implementation(Dependencies.GOOGLE_MATERIAL)

    implementation(project(":bmalib:graphic-core"))
    implementation(project(":bmalib:paintedview"))
    implementation(project(":bmalib:recyclerview"))
    implementation(project(":bmalib:style"))
    implementation(project(":bmalib:util"))

//    implementation(Dependencies.Bma.ACTIVITY)
//    implementation(Dependencies.Bma.CORE)
//    implementation(Dependencies.Bma.GRAPHIC_CORE)
//    implementation(Dependencies.Bma.PAINTED_VIEW)
//    implementation(Dependencies.Bma.RECYCLERVIEW)
//    implementation(Dependencies.Bma.STYLE)
//    implementation(Dependencies.Bma.UTIL)
}
repositories {
    mavenCentral()
}
