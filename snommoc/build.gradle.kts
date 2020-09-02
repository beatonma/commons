import com.android.build.gradle.internal.dsl.DefaultConfig

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("com.github.ben-manes.versions")
}

android {
    compileSdkVersion(Commons.Sdk.COMPILE)
    defaultConfig {
        versionCode = Git.commitCount(project)
        versionName = Git.tag(project)

        minSdkVersion(Commons.Sdk.MIN)
        targetSdkVersion(Commons.Sdk.TARGET)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"

        injectStrings(mapOf(
            "COMMONS_API_KEY" to local.LocalConfig.Api.Commons.API_KEY
        ), asBuildConfig = true, asResValue = false)

    }

    compileOptions {
        sourceCompatibility = Versions.JAVA
        targetCompatibility = Versions.JAVA
    }

    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.4"
//        useIR = true

        freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
    }
}

dependencies {

    // Unit tests
    arrayOf(
        Dependencies.Test.JUNIT,
        Dependencies.Test.AndroidX.CORE,
        project(":test")
    ).forEach(::testImplementation)

    val annotationProcessors = arrayOf(
        Dependencies.Dagger.ANNOTATION_PROCESSOR,
        Dependencies.Dagger.COMPILER,
        Dependencies.Hilt.AX_KAPT,
        Dependencies.Hilt.KAPT
    )

    val implementations = arrayOf(
        Dependencies.AndroidX.CORE_KTX,

        Dependencies.Dagger.ANDROID,
        Dependencies.Dagger.DAGGER,
        Dependencies.Dagger.SUPPORT,

        Dependencies.Hilt.CORE,

        Dependencies.Kotlin.STDLIB,
        Dependencies.Kotlin.Coroutines.ANDROID,
        Dependencies.Kotlin.Coroutines.CORE,

        Dependencies.Retrofit.RETROFIT,
        Dependencies.Retrofit.Converter.MOSHI,
        Dependencies.Retrofit.Converter.TEXT,

        project(":core"),
        project(":network-core")
    )


    annotationProcessors.forEach(::kapt)
    implementations.forEach(::implementation)
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}



fun DefaultConfig.injectStrings(mapping: Map<String, String>, asBuildConfig: Boolean, asResValue: Boolean) {
    mapping.forEach { (key, value) ->
        if (asBuildConfig) {
            buildConfigField("String", key.toUpperCase(), "\"$value\"")
        }
        if (asResValue) {
            resValue("string", key.toLowerCase(), value)
        }
    }
}
