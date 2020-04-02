import com.android.build.gradle.internal.dsl.DefaultConfig
import data.ParliamentDotUkPartyIDs
import local.LocalConfig

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.github.ben-manes.versions")
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

        // Party colors as @color resources and build config constants
        AllPartyThemes.forEach { (key, theme) ->
            injectPartyTheme(key, theme)
        }

        // Party IDs as build config constants
        ParliamentDotUkPartyIDs.forEach { (party, parliamentdotuk) ->
            buildConfigField("int", "${party}_PARLIAMENTDOTUK".toUpperCase(), "$parliamentdotuk")
        }

        testApplicationId = "org.beatonma.commons.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
                arg("room.expandProjection", "true")
            }
        }
    }

    buildFeatures {
//        compose = true
        viewBinding = true
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
        }
        getByName("release") {
//            isMinifyEnabled = true
            postprocessing.apply {
                isOptimizeCode = true
                isObfuscate = true
                isRemoveUnusedCode = true
                isRemoveUnusedResources = true
            }
//            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
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
        languageVersion = "1.4"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE
    }
}

dependencies {
    val testAnnotationProcessors = arrayOf(
        Dependencies.Dagger.COMPILER,
        Dependencies.Dagger.ANNOTATION_PROCESSOR
    )

    // Unit tests
    testAnnotationProcessors.forEach { kaptTest(it) }
    testImplementation(bmalib("testing"))
    testImplementation(Dependencies.Test.JUNIT)
    testImplementation(Dependencies.Test.MOCKITO)
    testImplementation(Dependencies.Kotlin.REFLECT)
    testImplementation(Dependencies.Test.RETROFIT_MOCK)
    testImplementation(Dependencies.Dagger.DAGGER)
    testImplementation(Dependencies.Test.OKHTTP_MOCK_SERVER)

    // Instrumentation tests
    androidTestImplementation(bmalib("testing"))
    androidTestImplementation(Dependencies.Test.AndroidX.CORE)
    androidTestImplementation(Dependencies.Test.AndroidX.RUNNER)
    androidTestImplementation(Dependencies.Test.AndroidX.ESPRESSO)
    androidTestImplementation(Dependencies.Test.AndroidX.LIVEDATA)

    val kotlin = arrayOf(
        Dependencies.Kotlin.STDLIB,
        Dependencies.Kotlin.Coroutines.CORE,
        Dependencies.Kotlin.Coroutines.ANDROID
    )

    val annotationProcessors = arrayOf(
        Dependencies.Dagger.COMPILER,
        Dependencies.Dagger.ANNOTATION_PROCESSOR,
        Dependencies.Room.AP
    )

    val dagger = arrayOf(
        Dependencies.Dagger.DAGGER,
        Dependencies.Dagger.ANDROID,
        Dependencies.Dagger.SUPPORT
    )

    val room = arrayOf(
        Dependencies.Room.RUNTIME,
        Dependencies.Room.KTX
    )

    val androidx = arrayOf(
        Dependencies.AndroidX.APPCOMPAT,
        Dependencies.AndroidX.CONSTRAINTLAYOUT,
        Dependencies.AndroidX.CORE_KTX,
        Dependencies.AndroidX.LIFECYCLE_RUNTIME,
        Dependencies.AndroidX.LIVEDATA_KTX,
        Dependencies.AndroidX.VIEWMODEL_KTX,
        Dependencies.AndroidX.NAVIGATION_UI,
        Dependencies.AndroidX.NAVIGATION_FRAGMENT,
        Dependencies.AndroidX.COMPOSE_LAYOUT,
        Dependencies.AndroidX.COMPOSE_TOOLING,
        Dependencies.AndroidX.COMPOSE_MATERIAL
    )

    val retrofit = arrayOf(
        Dependencies.Retrofit.RETROFIT,
        Dependencies.Retrofit.Converter.MOSHI,
        Dependencies.Retrofit.Converter.TEXT
    )

    val other = arrayOf(
        Dependencies.Glide.CORE,
        Dependencies.GOOGLE_MATERIAL
    )

    val implementations = arrayOf(
        androidx,
        dagger,
        kotlin,
        other,
        retrofit,
        room
    ).flatten()

    annotationProcessors.forEach { kapt(it) }
    implementations.forEach { implementation(it) }

    val bmalib = arrayOf(
        "graphic-core",
        "paintedview",
        "recyclerview",
        "style",
        "util"
    )
    bmalib.forEach { implementation(bmalib(it)) }

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}


fun DefaultConfig.injectPartyTheme(name: String, theme: PartyColors) {
    buildConfigField("int", "COLOR_PARTY_${name}_PRIMARY".toUpperCase(), "${theme._primaryInt}")
    buildConfigField("int", "COLOR_PARTY_${name}_ACCENT".toUpperCase(), "${theme._accentInt}")
    buildConfigField("int",
        "COLOR_PARTY_${name}_PRIMARY_TEXT".toUpperCase(),
        "${theme._primaryTextInt}")
    buildConfigField("int",
        "COLOR_PARTY_${name}_ACCENT_TEXT".toUpperCase(),
        "${theme._accentTextInt}")
    resValue("color", "party_${name}_primary", theme.primary.toString())
    resValue("color", "party_${name}_accent", theme.accent.toString())
    resValue("color", "party_${name}_primary", theme.primaryText.toString())
    resValue("color", "party_${name}_accent", theme.accentText.toString())
}

fun bmalib(artifact: String) = project(":bmalib:$artifact")
