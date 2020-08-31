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
            "COMMONS_API_KEY" to local.LocalConfig.Api.Commons.API_KEY,
            "GIT_SHA" to git.sha,
            "TWFY_API_KEY" to local.LocalConfig.Api.Twfy.API_KEY,
            "USER_AGENT_APP" to local.LocalConfig.UserAgent.NAME,
            "USER_AGENT_WEBSITE" to local.LocalConfig.UserAgent.WEBSITE,
            "USER_AGENT_EMAIL" to local.LocalConfig.UserAgent.EMAIL
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
    val annotationProcessors = arrayOf(
        Dependencies.Dagger.ANNOTATION_PROCESSOR,
        Dependencies.Dagger.COMPILER,
        Dependencies.Hilt.AX_KAPT,
        Dependencies.Hilt.KAPT,
        Dependencies.Room.AP
    )

    val implementations = arrayOf(
        Dependencies.AndroidX.CORE_KTX,
        Dependencies.Google.Play.AUTH,

//    val dagger = arrayOf(
        Dependencies.Dagger.ANDROID,
        Dependencies.Dagger.DAGGER,
        Dependencies.Dagger.SUPPORT,
//    )
//    val hilt = arrayOf(
        Dependencies.Hilt.CORE,
        Dependencies.Hilt.LIFECYCLE_VIEWMODEL,
        Dependencies.Hilt.WORK,
//    )
//    val kotlin = arrayOf(
        Dependencies.Kotlin.STDLIB,
        Dependencies.Kotlin.Coroutines.ANDROID,
        Dependencies.Kotlin.Coroutines.CORE,
//    )

//    val retrofit = arrayOf(
        Dependencies.Retrofit.RETROFIT,
        Dependencies.Retrofit.Converter.MOSHI,
        Dependencies.Retrofit.Converter.TEXT,
//    )

//    val room = arrayOf(
        Dependencies.Room.KTX,
        Dependencies.Room.RUNTIME
    )
//    )
//
//    val implementations = arrayOf(
//        dagger,
//        hilt,
//        kotlin,
//        retrofit, // TODO remove
//        room
//    ).flatten()

    annotationProcessors.forEach { kapt(it) }
    implementations.forEach { implementation(it) }

    implementation(project(":core"))
    implementation(project(":snommoc"))
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
