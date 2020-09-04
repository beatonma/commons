import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.beatonma.commons.buildsrc.AllPartyThemes
import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.Git
import org.beatonma.commons.buildsrc.PartyColors
import org.beatonma.commons.buildsrc.data.ParliamentDotUkPartyIDs
import org.beatonma.commons.buildsrc.kts.extensions.*
import org.beatonma.commons.buildsrc.local.LocalConfig
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

        injectStrings(
            "GIT_SHA" to Git.sha(project),
            "GOOGLE_SIGNIN_CLIENT_ID" to LocalConfig.OAuth.Google.WEB_CLIENT_ID,
            "GOOGLE_MAPS_API_KEY" to LocalConfig.Api.Google.MAPS,
            "ACCOUNT_USERNAME_CHARACTERS" to Commons.Account.Username.ALLOWED_CHARACTERS,
            asBuildConfig = true,
            asResValue = true
        )

        injectInts(
            "SOCIAL_COMMENT_MAX_LENGTH" to Commons.Social.MAX_COMMENT_LENGTH,
            "THEME_TEXT_DARK" to PartyColors.TEXT_DARK,
            "THEME_TEXT_LIGHT" to PartyColors.TEXT_LIGHT,
            "ACCOUNT_USERNAME_MAX_LENGTH" to Commons.Account.Username.MAX_LENGTH,
            "ACCOUNT_USERNAME_MIN_LENGTH" to Commons.Account.Username.MIN_LENGTH,
            asBuildConfig = true,
            asResValue = true
        )

        manifestPlaceholders.putAll(mapOf(
            "googleMapsApiKey" to org.beatonma.commons.buildsrc.local.LocalConfig.Api.Google.MAPS
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
    }

    buildFeatures {
        compose = false
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
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.AX_COMPOSE
    }
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.4"
        useIR = true

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
    unitTest {
        annotationProcessors(
            Dependencies.Dagger.COMPILER,
            Dependencies.Dagger.ANNOTATION_PROCESSOR
        )

        implementations(
            project(":test"),
            Dependencies.Dagger.DAGGER,
            Dependencies.Kotlin.REFLECT,
            Dependencies.Kotlin.Coroutines.TEST,
            Dependencies.Test.JUNIT,
            Dependencies.Test.MOCKITO,
            Dependencies.Test.RETROFIT_MOCK,
            Dependencies.Test.OKHTTP_MOCK_SERVER,
            Dependencies.Test.AndroidX.CORE
        )
    }

    instrumentationTest {
        annotationProcessors(
            Dependencies.Dagger.COMPILER,
            Dependencies.Dagger.ANNOTATION_PROCESSOR
        )

        implementations(
            project(":test"),
            Dependencies.Hilt.TESTING,
            Dependencies.Kotlin.Coroutines.TEST,
            Dependencies.Test.AndroidX.CORE,
            Dependencies.Test.AndroidX.LIVEDATA,
            Dependencies.Test.AndroidX.RULES,
            Dependencies.Test.AndroidX.RUNNER,
            Dependencies.Test.AndroidX.Espresso.CONTRIB,
            Dependencies.Test.AndroidX.Espresso.CORE
        )
    }

    debug {
        implementations(
            Dependencies.Debug.LEAK_CANARY
        )
    }

    main {
        annotationProcessors(
            Dependencies.Dagger.ANNOTATION_PROCESSOR,
            Dependencies.Dagger.COMPILER,
            Dependencies.Glide.COMPILER,
            Dependencies.Hilt.AX_KAPT,
            Dependencies.Hilt.KAPT,
            Dependencies.Room.AP
        )

        implementations(
            Dependencies.Kotlin.STDLIB,
            Dependencies.Kotlin.Coroutines.ANDROID,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.Kotlin.Coroutines.PLAY,

            Dependencies.Dagger.ANDROID,
            Dependencies.Dagger.DAGGER,
            Dependencies.Dagger.SUPPORT,

            Dependencies.Hilt.CORE,
            Dependencies.Hilt.LIFECYCLE_VIEWMODEL,
            Dependencies.Hilt.WORK,

            Dependencies.Room.KTX,
            Dependencies.Room.RUNTIME,

            Dependencies.AndroidX.APPCOMPAT,
            Dependencies.AndroidX.CONSTRAINTLAYOUT,
            Dependencies.AndroidX.CORE_KTX,
            Dependencies.AndroidX.LIFECYCLE_RUNTIME,
            Dependencies.AndroidX.LIVEDATA_KTX,
            Dependencies.AndroidX.NAVIGATION_FRAGMENT,
            Dependencies.AndroidX.NAVIGATION_UI,
            Dependencies.AndroidX.RECYCLERVIEW,
            Dependencies.AndroidX.VIEWMODEL_KTX,
            Dependencies.AndroidX.WORK,

            Dependencies.Retrofit.RETROFIT,
            Dependencies.Retrofit.Converter.MOSHI,
            Dependencies.Retrofit.Converter.TEXT,

            Dependencies.Glide.CORE,

            Dependencies.Google.MATERIAL,
            Dependencies.Google.Play.AUTH,
            Dependencies.Google.Play.LOCATION,
            Dependencies.Google.Play.MAPS,
            Dependencies.Google.Play.MAPS_UTIL,

            project(":core"),
            project(":network-core"),
            project(":snommoc"),
            project(":data"),
            project(":repo")
        )
    }

//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
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
