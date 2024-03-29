import org.beatonma.commons.buildsrc.gradle.buildConfigStrings
import org.beatonma.commons.buildsrc.gradle.main
import org.beatonma.commons.buildsrc.local.LocalConfig

plugins {
    id(Plugins.Commons.COMMONS_LIBRARY_CONFIG)
    id(Plugins.Commons.COMMONS_HILT_MODULE)
}

android {
    defaultConfig {
        buildConfigStrings(
            "APPLICATION_ID" to org.beatonma.commons.buildsrc.config.Commons.APPLICATION_ID,
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
            Dependencies.Jetpack.CORE_KTX,
            Dependencies.Retrofit.RETROFIT
        )
    }
}
