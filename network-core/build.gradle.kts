import org.beatonma.commons.buildsrc.Commons
import org.beatonma.commons.buildsrc.kts.extensions.coroutines
import org.beatonma.commons.buildsrc.kts.extensions.injectStrings
import org.beatonma.commons.buildsrc.local.LocalConfig

plugins {
    id(Plugins.COMMONS_LIBRARY_CONFIG)
    id(Plugins.COMMONS_HILT_MODULE)
}

android {
    defaultConfig {
        injectStrings(
            "APPLICATION_ID" to Commons.APPLICATION_ID,
            "USER_AGENT_APP" to LocalConfig.UserAgent.NAME,
            "USER_AGENT_WEBSITE" to LocalConfig.UserAgent.WEBSITE,
            "USER_AGENT_EMAIL" to LocalConfig.UserAgent.EMAIL,
            asBuildConfig = true,
            asResValue = false
        )
    }
}

dependencies {
    val implementations = arrayOf(
        Dependencies.AndroidX.CORE_KTX,

        *coroutines,

        Dependencies.Retrofit.RETROFIT
    )
    implementations.forEach(::implementation)
}
