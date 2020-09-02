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
    val git = Git.resolveData(project)

    compileSdkVersion(Commons.Sdk.COMPILE)
    defaultConfig {
        versionCode = git.commitCount
        versionName = git.tag

        minSdkVersion(Commons.Sdk.MIN)
        targetSdkVersion(Commons.Sdk.TARGET)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"

        injectStrings(mapOf(
            "VERSION_NAME" to git.tag,
            "APPLICATION_ID" to Commons.APPLICATION_ID,
            "GIT_SHA" to git.sha
        ), asBuildConfig = true, asResValue = false)

        injectInts(mapOf(
            "VERSION_CODE" to git.commitCount
        ), asBuildConfig = true, asResValue = false)

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
                arg("room.expandProjection", "true")
            }
        }
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
    val testAnnotationProcessors = arrayOf(
        Dependencies.Dagger.COMPILER,
        Dependencies.Dagger.ANNOTATION_PROCESSOR
    )
    val testImplementations = arrayOf(
        project(":test"),
        Dependencies.Test.JUNIT,
        Dependencies.Hilt.TESTING,
        Dependencies.Test.AndroidX.LIVEDATA
    )

    val annotationProcessors = arrayOf(
        Dependencies.Dagger.ANNOTATION_PROCESSOR,
        Dependencies.Dagger.COMPILER,
        Dependencies.Hilt.AX_KAPT,
        Dependencies.Hilt.KAPT,
        Dependencies.Room.AP
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

        Dependencies.Room.KTX,
        Dependencies.Room.RUNTIME,

        project(":core")
    )

    testAnnotationProcessors.forEach(::kaptAndroidTest)
    testImplementations.forEach(::androidTestImplementation)

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

fun DefaultConfig.injectInts(mapping: Map<String, Int>, asBuildConfig: Boolean, asResValue: Boolean) {
    mapping.forEach { (key, value) ->
        if (asBuildConfig) {
            buildConfigField("int", key.toUpperCase(), "$value")
        }
        if (asResValue) {
            resValue("integer", key.toLowerCase(), "$value")
        }
    }
}
