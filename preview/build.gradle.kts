import org.beatonma.commons.buildsrc.Git
import org.beatonma.commons.buildsrc.kts.extensions.debug
import org.beatonma.commons.buildsrc.kts.extensions.instrumentationTest
import org.beatonma.commons.buildsrc.kts.extensions.main

plugins {
    id(Plugins.COMMONS_APPLICATION_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
    id(Plugins.COMMONS_COMPOSE_MODULE)
}

android {
    val git = Git.resolveData(project)

    defaultConfig {
//        injectStrings(
//                "ACCOUNT_USERNAME_CHARACTERS" to Commons.Account.Username.ALLOWED_CHARACTERS,
//                "GOOGLE_MAPS_API_KEY" to LocalConfig.Api.Google.MAPS,
//                "GOOGLE_SIGNIN_CLIENT_ID" to LocalConfig.OAuth.Google.WEB_CLIENT_ID,
//                asBuildConfig = true,
//                asResValue = true
//        )

//        injectInts(
//                "ACCOUNT_USERNAME_MAX_LENGTH" to Commons.Account.Username.MAX_LENGTH,
//                "ACCOUNT_USERNAME_MIN_LENGTH" to Commons.Account.Username.MIN_LENGTH,
//                "SOCIAL_COMMENT_MAX_LENGTH" to Commons.Social.MAX_COMMENT_LENGTH,
//                "THEME_TEXT_DARK" to PartyColors.TEXT_DARK,
//                "THEME_TEXT_LIGHT" to PartyColors.TEXT_LIGHT,
//                asBuildConfig = true,
//                asResValue = true
//        )

//        manifestPlaceholders.put(
//                "googleMapsApiKey" to LocalConfig.Api.Google.MAPS
//        )

        // Party colors as @color resources and build config constants
//        injectPartyThemes(AllPartyThemes)

        // Party IDs as build config constants
//        injectPartyIDs(ParliamentDotUkPartyIDs)

        testApplicationId = "org.beatonma.commons.test"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = git.branch
//            manifestPlaceholders.put(
//                    "appname" to "DEV:${git.branch}/CommonsPreview"
//            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        useIR = true

        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }
}

dependencies {
//    unitTest {
//        annotationProcessors(
//                Dependencies.Dagger.COMPILER,
//                Dependencies.Dagger.ANNOTATION_PROCESSOR
//        )
//
//        implementations(
//                project(":test"),
//                Dependencies.Dagger.DAGGER,
//                Dependencies.Kotlin.REFLECT,
//                Dependencies.Kotlin.Coroutines.TEST,
//                Dependencies.Test.JUNIT,
//                Dependencies.Test.MOCKITO,
//                Dependencies.Retrofit.MOCK,
//                Dependencies.Test.OKHTTP_MOCK_SERVER,
//                Dependencies.Test.AndroidX.CORE
//        )
//    }

    instrumentationTest {
        annotationProcessors(
            Dependencies.Dagger.COMPILER,
            Dependencies.Dagger.ANNOTATION_PROCESSOR
        )

        implementations(
            project(":test"),
            Dependencies.Dagger.Hilt.TESTING,
            Dependencies.Kotlin.Coroutines.TEST,
            Dependencies.Test.AndroidX.CORE,
            Dependencies.Test.JUNIT,
            Dependencies.Test.AndroidX.LIVEDATA,
            Dependencies.Test.AndroidX.RULES,
            Dependencies.Test.AndroidX.RUNNER,
            Dependencies.Test.AndroidX.Espresso.CONTRIB,
            Dependencies.Test.AndroidX.Espresso.CORE,
            Dependencies.AndroidX.Compose.TEST
        )
    }

    debug {
        implementations(
//            Dependencies.Debug.LEAK_CANARY,
            project(":themepreview")
        )
    }

    main {
        annotationProcessors(
            Dependencies.Glide.COMPILER
        )

        implementations(
            Dependencies.Kotlin.Coroutines.ANDROID,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.Kotlin.Coroutines.PLAY,

//                Dependencies.Dagger.Hilt.LIFECYCLE_VIEWMODEL,
//                Dependencies.Dagger.Hilt.WORK,

//                Dependencies.Room.RUNTIME,

            Dependencies.AndroidX.Compose.ANIMATION,
            Dependencies.AndroidX.Compose.MATERIAL_ICONS_CORE,
            Dependencies.AndroidX.Compose.MATERIAL_ICONS_EXTENDED,
            Dependencies.AndroidX.Compose.RUNTIME,
            Dependencies.AndroidX.Compose.UI,
//            Dependencies.AndroidX.Compose.COMPILER,

            Dependencies.AndroidX.APPCOMPAT,
            Dependencies.AndroidX.CONSTRAINTLAYOUT,
            Dependencies.AndroidX.CORE_KTX,
            Dependencies.AndroidX.LIFECYCLE_RUNTIME,
            Dependencies.AndroidX.LIVEDATA_KTX,
            Dependencies.AndroidX.NAVIGATION_FRAGMENT,
            Dependencies.AndroidX.NAVIGATION_UI,
            Dependencies.AndroidX.RECYCLERVIEW,
            Dependencies.AndroidX.VIEWMODEL_KTX,
//                Dependencies.AndroidX.WORK,

//                Dependencies.Retrofit.Converter.MOSHI,

            Dependencies.Glide.CORE,

            Dependencies.Google.MATERIAL,
//                Dependencies.Google.Play.AUTH,
//                Dependencies.Google.Play.LOCATION,
//                Dependencies.Google.Play.MAPS,
//                Dependencies.Google.Play.MAPS_UTIL,

            project(":core"),
//                project(":network-core"),
//                project(":snommoc"),
//                project(":persistence"),
//                project(":repo"),
            project(":theme"),
            project(":compose"),
            project(":svg")
        )
    }
}

//
//fun DefaultConfig.injectPartyIDs(ids: Map<String, Int>) =
//        ids.forEach { (party, parliamentdotuk) ->
//                buildConfigInt("${party}_PARLIAMENTDOTUK", parliamentdotuk)
//        }
//
//fun DefaultConfig.injectPartyThemes(partyThemes: Map<String, PartyColors>) =
//        partyThemes.forEach { injectPartyTheme(it.key, it.value) }
//
//fun DefaultConfig.injectPartyTheme(name: String, theme: PartyColors) {
//    buildConfigInt("COLOR_PARTY_${name}_PRIMARY", theme._primaryInt)
//    buildConfigInt("COLOR_PARTY_${name}_ACCENT", theme._accentInt)
//
//    buildConfigInt("COLOR_PARTY_${name}_PRIMARY_TEXT", theme.primaryText)
//    buildConfigInt("COLOR_PARTY_${name}_ACCENT_TEXT", theme.accentText)
//
//    resColor("party_${name}_primary", theme.primary)
//    resColor("party_${name}_accent", theme.accent)
//}
//
//fun <K, V> MutableMap<K, V>.put(
//        vararg from: Pair<K, V>
//) {
//    from.forEach { (key, value) -> this[key] = value }
//}
