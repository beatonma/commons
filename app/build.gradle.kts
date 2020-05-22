import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.DefaultConfig
import data.ParliamentDotUkPartyIDs
import local.LocalConfig
import java.text.SimpleDateFormat
import java.util.*

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
            "USER_AGENT_EMAIL" to LocalConfig.UserAgent.EMAIL,
            "GOOGLE_SIGNIN_CLIENT_ID" to LocalConfig.OAuth.Google.WEB_CLIENT_ID,
            "GOOGLE_MAPS_API_KEY" to LocalConfig.Api.Google.MAPS
        )

        buildConfigStrings.forEach { (key, value) ->
            buildConfigField("String", key, "\"$value\"")
        }

        manifestPlaceholders = mapOf(
            "googleMapsApiKey" to LocalConfig.Api.Google.MAPS
        )

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
            val date = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            applicationVariants.all {
                val variant = this
                variant.outputs
                    .map { it as BaseVariantOutputImpl }
                    .forEach { output ->
                        output.outputFileName = output.outputFileName
                            .replace("app-", "commons-")
                            .replace(
                                "-release",
                                "-release-$date-${Git.commitCount(project)}-${Git.tag(project)}-${Git.sha(project)}")
                    }
            }
//            postprocessing.apply {
//                isOptimizeCode = true
//                isObfuscate = true
//                isRemoveUnusedCode = true
//                isRemoveUnusedResources = true
//            }
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
    arrayOf(
        bmalib("testing"),
        Dependencies.Test.AndroidX.CORE,
        Dependencies.Test.JUNIT,
        Dependencies.Test.MOCKITO,
        Dependencies.Kotlin.REFLECT,
        Dependencies.Test.RETROFIT_MOCK,
        Dependencies.Dagger.DAGGER,
        Dependencies.Test.OKHTTP_MOCK_SERVER
    ).forEach { testImplementation(it) }

    // Instrumentation tests
    testAnnotationProcessors.forEach { kaptAndroidTest(it) }
    arrayOf(
        bmalib("testing"),
        Dependencies.Test.AndroidX.CORE,
        Dependencies.Test.AndroidX.RUNNER,
        Dependencies.Test.AndroidX.ESPRESSO,
        Dependencies.Test.AndroidX.LIVEDATA
    ).forEach { androidTestImplementation(it) }

    val kotlin = arrayOf(
        Dependencies.Kotlin.STDLIB,
        Dependencies.Kotlin.Coroutines.CORE,
        Dependencies.Kotlin.Coroutines.ANDROID,
        Dependencies.Kotlin.Coroutines.PLAY
    )

    val annotationProcessors = arrayOf(
        Dependencies.Dagger.COMPILER,
        Dependencies.Dagger.ANNOTATION_PROCESSOR,
        Dependencies.Room.AP,
        Dependencies.Glide.COMPILER
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
        Dependencies.AndroidX.RECYCLERVIEW,
        Dependencies.AndroidX.LIFECYCLE_RUNTIME,
        Dependencies.AndroidX.LIVEDATA_KTX,
        Dependencies.AndroidX.VIEWMODEL_KTX,
        Dependencies.AndroidX.NAVIGATION_UI,
        Dependencies.AndroidX.NAVIGATION_FRAGMENT
    )

    val compose = arrayOf(
        Dependencies.AndroidX.COMPOSE_LAYOUT,
        Dependencies.AndroidX.COMPOSE_TOOLING,
        Dependencies.AndroidX.COMPOSE_MATERIAL
    )

    val retrofit = arrayOf(
        Dependencies.Retrofit.RETROFIT,
        Dependencies.Retrofit.Converter.MOSHI,
        Dependencies.Retrofit.Converter.TEXT
    )

    val glide = arrayOf(
        Dependencies.Glide.CORE
    )

    val google = arrayOf(
        Dependencies.Google.MATERIAL,
        Dependencies.Google.Play.AUTH,
        Dependencies.Google.Play.LOCATION,
        Dependencies.Google.Play.MAPS,
        Dependencies.Google.Play.MAPS_UTIL
    )

    val implementations = arrayOf(
        androidx,
//        compose,
        dagger,
        google,
        kotlin,
        glide,
        retrofit,
        room
    ).flatten()

    annotationProcessors.forEach { kapt(it) }
    implementations.forEach { implementation(it) }

    val bmalib = arrayOf(
        "graphic-core",
        "paintedview",
        "recyclerview",
        "style"
    )
    bmalib.forEach { implementation(bmalib(it)) }

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
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
