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
            "COMMONS_API_KEY" to LocalConfig.Api.Commons.API_KEY,
            asBuildConfig = true,
            asResValue = false
        )
    }
}

dependencies {
    val implementations = arrayOf(
        Dependencies.AndroidX.CORE_KTX,

        *coroutines,

        Dependencies.Retrofit.RETROFIT,
        Dependencies.Retrofit.Converter.MOSHI,
        Dependencies.Retrofit.Converter.TEXT,

        project(":core"),
        project(":network-core")
    )

    implementations.forEach(::implementation)
}
