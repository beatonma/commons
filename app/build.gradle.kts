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
    id("dagger.hilt.android.plugin")
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

        injectStrings(mapOf(
            "COMMONS_API_KEY" to LocalConfig.Api.Commons.API_KEY,
            "GIT_SHA" to Git.sha(project),
            "TWFY_API_KEY" to LocalConfig.Api.Twfy.API_KEY,
            "USER_AGENT_APP" to LocalConfig.UserAgent.NAME,
            "USER_AGENT_WEBSITE" to LocalConfig.UserAgent.WEBSITE,
            "USER_AGENT_EMAIL" to LocalConfig.UserAgent.EMAIL,
            "GOOGLE_SIGNIN_CLIENT_ID" to LocalConfig.OAuth.Google.WEB_CLIENT_ID,
            "GOOGLE_MAPS_API_KEY" to LocalConfig.Api.Google.MAPS
        ), asBuildConfig = true, asResValue = true)

        injectInts(mapOf(
            "SOCIAL_COMMENT_MAX_LENGTH" to Commons.Social.MAX_COMMENT_LENGTH,
            "THEME_TEXT_DARK" to TEXT_DARK,
            "THEME_TEXT_LIGHT" to TEXT_LIGHT
        ), asBuildConfig = true, asResValue = true)

        manifestPlaceholders.putAll(mapOf(
            "googleMapsApiKey" to LocalConfig.Api.Google.MAPS
        ))

        // Party colors as @color resources and build config constants
        AllPartyThemes.forEach { (key, theme) ->
            injectPartyTheme(key, theme)
        }

        // Party IDs as build config constants
        ParliamentDotUkPartyIDs.forEach { (party, parliamentdotuk) ->
            buildConfigField("int", "${party}_PARLIAMENTDOTUK".toUpperCase(), "$parliamentdotuk")
        }

        testApplicationId = "org.beatonma.commons.test"
        testInstrumentationRunner = "org.beatonma.commons.androidTest.HiltTestRunner"

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

        freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/licenses/*")
        exclude("**/attach_hotspot_windows.dll")
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
        Dependencies.Dagger.DAGGER,
        Dependencies.Kotlin.REFLECT,
        Dependencies.Kotlin.Coroutines.TEST,
        Dependencies.Test.JUNIT,
        Dependencies.Test.MOCKITO,
        Dependencies.Test.RETROFIT_MOCK,
        Dependencies.Test.OKHTTP_MOCK_SERVER,
        Dependencies.Test.AndroidX.CORE
    ).forEach { testImplementation(it) }

    // Instrumentation tests
    testAnnotationProcessors.forEach { kaptAndroidTest(it) }
    arrayOf(
        bmalib("testing"),
        Dependencies.Hilt.TESTING,
        Dependencies.Kotlin.Coroutines.TEST,
        Dependencies.Test.AndroidX.CORE,
        Dependencies.Test.AndroidX.LIVEDATA,
        Dependencies.Test.AndroidX.RULES,
        Dependencies.Test.AndroidX.RUNNER,
        Dependencies.Test.AndroidX.Espresso.CONTRIB,
        Dependencies.Test.AndroidX.Espresso.CORE
    ).forEach { androidTestImplementation(it) }

    // Debug build
    arrayOf(
        Dependencies.Debug.LEAK_CANARY
    ).forEach { debugImplementation(it) }

    // Main build
    val kotlin = arrayOf(
        Dependencies.Kotlin.STDLIB,
        Dependencies.Kotlin.Coroutines.ANDROID,
        Dependencies.Kotlin.Coroutines.CORE,
        Dependencies.Kotlin.Coroutines.PLAY
    )

    val annotationProcessors = arrayOf(
        Dependencies.Dagger.ANNOTATION_PROCESSOR,
        Dependencies.Dagger.COMPILER,
        Dependencies.Glide.COMPILER,
        Dependencies.Hilt.AX_KAPT,
        Dependencies.Hilt.KAPT,
        Dependencies.Room.AP
    )

    val dagger = arrayOf(
        Dependencies.Dagger.ANDROID,
        Dependencies.Dagger.DAGGER,
        Dependencies.Dagger.SUPPORT
    )
    val hilt = arrayOf(
        Dependencies.Hilt.CORE,
        Dependencies.Hilt.LIFECYCLE_VIEWMODEL,
        Dependencies.Hilt.WORK
    )

    val room = arrayOf(
        Dependencies.Room.KTX,
        Dependencies.Room.RUNTIME
    )

    val androidx = arrayOf(
        Dependencies.AndroidX.APPCOMPAT,
        Dependencies.AndroidX.CONSTRAINTLAYOUT,
        Dependencies.AndroidX.CORE_KTX,
        Dependencies.AndroidX.LIFECYCLE_RUNTIME,
        Dependencies.AndroidX.LIVEDATA_KTX,
        Dependencies.AndroidX.NAVIGATION_FRAGMENT,
        Dependencies.AndroidX.NAVIGATION_UI,
        Dependencies.AndroidX.RECYCLERVIEW,
        Dependencies.AndroidX.VIEWMODEL_KTX,
        Dependencies.AndroidX.WORK
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
        dagger,
        hilt,
        glide,
        google,
        kotlin,
        retrofit,
        room
    ).flatten()

    annotationProcessors.forEach { kapt(it) }
    implementations.forEach { implementation(it) }

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
        "${theme.primaryText}")
    buildConfigField("int",
        "COLOR_PARTY_${name}_ACCENT_TEXT".toUpperCase(),
        "${theme.accentText}")
    resValue("color", "party_${name}_primary", theme.primary)
    resValue("color", "party_${name}_accent", theme.accent)
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

fun bmalib(artifact: String) = project(":bmalib:$artifact")
