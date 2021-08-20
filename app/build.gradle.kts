import com.android.build.api.dsl.DefaultConfig
import org.beatonma.commons.buildsrc.Git
import org.beatonma.commons.buildsrc.config.Commons
import org.beatonma.commons.buildsrc.data.AllPartyThemes
import org.beatonma.commons.buildsrc.data.ParliamentDotUkPartyIDs
import org.beatonma.commons.buildsrc.data.PartyColors
import org.beatonma.commons.buildsrc.gradle.*
import org.beatonma.commons.buildsrc.local.LocalConfig

plugins {
    id(Plugins.Commons.COMMONS_APPLICATION_CONFIG)
    id(Plugins.Commons.COMMONS_HILT_MODULE)
    id(Plugins.Commons.COMMONS_COMPOSE_MODULE)
}

android {
    val git = Git.resolveData(project)

    defaultConfig {
        injectAppConstants()
    }

    signingConfigs {
        create("release") {
            storeFile = file(LocalConfig.Signing.Keystore.PATH)
            storePassword = LocalConfig.Signing.Keystore.PASSWORD
            keyAlias = LocalConfig.Signing.Key.ALIAS
            keyPassword = LocalConfig.Signing.Key.PASSWORD
        }
    }

    buildTypes {
        debug {
            manifestPlaceholders.put(
                "appname" to "DEV:${git.branch}/Commons"
            )
        }
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }

    hilt {
        enableExperimentalClasspathAggregation = true
    }
}

dependencies {
    androidTestUtil("androidx.test:orchestrator:${Versions.Jetpack.Test.CORE}")

    unitTest {
        implementations(
            project(Modules.Test),
            Dependencies.Kotlin.REFLECT,
            Dependencies.Kotlin.Coroutines.TEST,
            Dependencies.Test.JUNIT,
            Dependencies.Test.MOCKITO,
            Dependencies.Retrofit.MOCK,
            Dependencies.Test.OKHTTP_MOCK_SERVER,
            Dependencies.Test.Jetpack.CORE,
        )
    }

    instrumentationTest {
        implementations(
            project(Modules.Test),
            Dependencies.Kotlin.Coroutines.TEST,
            Dependencies.Test.Jetpack.CORE,
            Dependencies.Test.JUNIT,
            Dependencies.Test.Jetpack.ARCH_CORE,
            Dependencies.Test.Jetpack.RULES,
            Dependencies.Test.Jetpack.RUNNER,
            Dependencies.Test.Jetpack.Espresso.CONTRIB,
            Dependencies.Test.Jetpack.Espresso.CORE,
        )
    }

    debug {
        implementations(
            Dependencies.Kotlin.REFLECT,
//            Dependencies.Debug.LEAK_CANARY,
            project(Modules.SampleData),
            project(Modules.ThemePreview),
        )
    }

    main {
        implementations(
            Dependencies.Kotlin.Coroutines.ANDROID,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.Kotlin.Coroutines.PLAY,

            Dependencies.Dagger.Hilt.NAV_COMPOSE,
            Dependencies.Dagger.Hilt.LIFECYCLE_VIEWMODEL,
            Dependencies.Dagger.Hilt.WORK,

            Dependencies.Room.RUNTIME,

            Dependencies.Jetpack.Compose.ANIMATION,
            Dependencies.Jetpack.Compose.LIVEDATA,
            Dependencies.Jetpack.Compose.MATERIAL_ICONS_CORE,
            Dependencies.Jetpack.Compose.MATERIAL_ICONS_EXTENDED,
            Dependencies.Jetpack.NAVIGATION_COMPOSE,
            Dependencies.Jetpack.LIFECYCLE_VIEWMODEL_COMPOSE,
            Dependencies.Jetpack.ACTIVITY_COMPOSE,

            Dependencies.Jetpack.ACTIVITY,
            Dependencies.Jetpack.FRAGMENT,
            Dependencies.Jetpack.APPCOMPAT,
            Dependencies.Jetpack.CORE_KTX,
            Dependencies.Jetpack.LIFECYCLE_RUNTIME,
            Dependencies.Jetpack.LIVEDATA_KTX,
            Dependencies.Jetpack.NAVIGATION_FRAGMENT,
            Dependencies.Jetpack.NAVIGATION_UI,
            Dependencies.Jetpack.SAVEDSTATE,
            Dependencies.Jetpack.VIEWMODEL_KTX,
            Dependencies.Jetpack.WORK,

            Dependencies.Retrofit.Converter.MOSHI,

            Dependencies.Coil.COIL,
            Dependencies.Coil.COIL_COMPOSE,
            Dependencies.Accompanist.INSETS,

            Dependencies.Google.MATERIAL,
            Dependencies.Google.Play.AUTH,
            Dependencies.Google.Play.LOCATION,
            Dependencies.Google.Maps.V3.MAPS,
            Dependencies.Google.Maps.V3.MAPS_UTIL,
            Dependencies.Google.Maps.V3.MAPS_KTX,
            Dependencies.Google.Maps.V3.MAPS_UTIL_KTX,

            project(Modules.Core),
            project(Modules.NetworkCore),
            project(Modules.Snommoc),
            project(Modules.UkParliament),
            project(Modules.Persistence),
            project(Modules.Repository),
            project(Modules.Theme),
            project(Modules.Compose),
            project(Modules.Svg),
        )
    }
}

/**
 * Inject values defined in buildsrc so they are accessible to the app at runtime.
 * Depending on use case, these may be injected as BuildConfig constants, XML resources,
 * or as values in the app manifest.
 */
fun DefaultConfig.injectAppConstants() {
    injectStrings(
        "ACCOUNT_USERNAME_CHARACTERS" to Commons.Account.Username.ALLOWED_CHARACTERS,
        "ACCOUNT_USERNAME_PATTERN" to Commons.Account.Username.REGEX,
        "GOOGLE_MAPS_API_KEY" to LocalConfig.Api.Google.MAPS,
        "GOOGLE_SIGNIN_CLIENT_ID" to LocalConfig.OAuth.Google.WEB_CLIENT_ID,
        asBuildConfig = true,
        asResValue = true
    )

    injectInts(
        "ACCOUNT_USERNAME_MAX_LENGTH" to Commons.Account.Username.MAX_LENGTH,
        "ACCOUNT_USERNAME_MIN_LENGTH" to Commons.Account.Username.MIN_LENGTH,
        "SOCIAL_COMMENT_MAX_LENGTH" to Commons.Social.MAX_COMMENT_LENGTH,
        "SOCIAL_COMMENT_MIN_LENGTH" to Commons.Social.MIN_COMMENT_LENGTH,
        "THEME_TEXT_DARK" to PartyColors.TEXT_DARK,
        "THEME_TEXT_LIGHT" to PartyColors.TEXT_LIGHT,
        asBuildConfig = true,
        asResValue = true
    )

    injectManifestValues()

    // Party colors as @color resources and build config constants
    injectPartyThemes()

    // Party IDs as build config constants
    injectPartyIDs()
}

fun DefaultConfig.injectManifestValues() {
    manifestPlaceholders.put(
        "googleMapsApiKey" to LocalConfig.Api.Google.MAPS
    )
}

fun DefaultConfig.injectPartyIDs() {
    ParliamentDotUkPartyIDs.forEach { (party, parliamentdotuk) ->
        buildConfigInt("${party}_PARLIAMENTDOTUK", parliamentdotuk)
    }
}

fun DefaultConfig.injectPartyThemes() {
    AllPartyThemes.forEach { (name, theme) ->
        buildConfigInt("COLOR_PARTY_${name}_PRIMARY", theme._primaryInt)
        buildConfigInt("COLOR_PARTY_${name}_ACCENT", theme._accentInt)

        buildConfigInt("COLOR_PARTY_${name}_PRIMARY_TEXT", theme.primaryText)
        buildConfigInt("COLOR_PARTY_${name}_ACCENT_TEXT", theme.accentText)

        resColor("party_${name}_primary", theme.primary)
        resColor("party_${name}_accent", theme.accent)
    }
}

fun <K, V> MutableMap<K, V>.put(
    vararg from: Pair<K, V>
) {
    from.forEach { (key, value) -> this[key] = value }
}
