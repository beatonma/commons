import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.kts.extensions.buildConfigStrings
import org.beatonma.commons.buildsrc.kts.extensions.main
import org.beatonma.commons.buildsrc.local.LocalConfig

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
}

android {
    defaultConfig {
        buildConfigStrings(
            "APPLICATION_ID" to Commons.APPLICATION_ID,
            "USER_AGENT_APP" to LocalConfig.UserAgent.NAME,
            "USER_AGENT_WEBSITE" to LocalConfig.UserAgent.WEBSITE,
            "USER_AGENT_EMAIL" to LocalConfig.UserAgent.EMAIL
        )
    }
}

dependencies {
    main {
        implementations(
            Dependencies.Kotlin.Coroutines.ANDROID,
            Dependencies.Kotlin.Coroutines.CORE,
            Dependencies.AndroidX.CORE_KTX,
            Dependencies.Retrofit.RETROFIT
        )
    }
}
